package com.tmt.common.searcher;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.apache.lucene.search.vectorhighlight.FragListBuilder;
import org.apache.lucene.search.vectorhighlight.FragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.ScoreOrderFragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.SimpleFragListBuilder;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.tmt.common.entity.IdEntity;
import com.tmt.common.persistence.ScorePage;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.PropertiesLoader;
import com.tmt.common.utils.RegexpUtil;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.common.utils.StringUtil3;

/**
 * lunene 查询维护
 * 其中IndexWriter采用了单例模式，
 * 确保始终只有一个对象实例，因为Lucene限制了索引写操作是阻塞的，
 * 即同一时刻只能有一个IndexWriter在执行写操作，直到indexWriter释放lock，
 * 而索引读的操作是可以并发进行的
 * 
 * 优化技巧
 * 1. 尽量使用本地文件系统，如果需要使用远程文件系统，在本地创建，发送到远程
 * 2. 在索引期间复用单一的IndexWriter
 * 3. 关闭没有使用的功能
 * 4. 关闭不查询的字段
 * 5. IndexWriterConfig maxBufferedDeleteTerms 设置为48M 如果有可能设置更多
 * 6. 
 * @author root
 */
public abstract class BaseSearcher<T extends IdEntity<Long>> {
	
	// 定义几种使用场景
	public static final byte scene_ONE = 1; // 列表
	public static final byte scene_TWO = 2; // 详情
	
	// 存储路径
	public static final String LUCENE_INDEX_PATH;
	public static final TermQuery QUERY_ALL_TERM;
	static {
		PropertiesLoader propertiesLoader = new PropertiesLoader("/searcher/lucene-store.properties");
		LUCENE_INDEX_PATH = propertiesLoader.getProperty("lucene.diskStore");
		
		QUERY_ALL_TERM = new TermQuery(new Term("_A", "*"));
	}
	
	public final String[] COLORED_PRE_TAGS = {
		    "<b class=\"HL-null\">",
		    "<b class=\"HL-red\">",
			"<b class=\"HL-yellow\">",
			"<b class=\"HL-lawngreen\">",
			"<b class=\"HL-aquamarine\">",
			"<b class=\"HL-magenta\">",
			"<b class=\"HL-palegreen\">",
			"<b class=\"HL-coral\">", 
			"<b class=\"HL-wheat\">",
			"<b class=\"HL-khaki\">", 
			"<b class=\"HL-lime\">",
			"<b class=\"HL-deepskyblue\">",
			"<b class=\"HL-deeppink\">",
			"<b class=\"HL-salmon\">",
			"<b class=\"HL-peachpuff\">",
			"<b class=\"HL-violet\">",
			"<b class=\"HL-mediumpurple\">",
			"<b class=\"HL-palegoldenrod\">",
			"<b class=\"HL-darkkhaki\">",
			"<b class=\"HL-springgreen\">",
			"<b class=\"HL-turquoise\">",
			"<b class=\"HL-powderblue\">" };
	public final String[] COLORED_POST_TAGS = { "</b>" };
	public final char[] INTERVAL_CHARS = new char[]{'~', '～'};// 间隔查询字符
	public final String DIVIDE = ",";
	public final String ID_DIVIDE = ".";
	public final String HL_FRAGMENT_DIVIDE = "#\\.";
	public final String ID = "id";
	public final String CREATE_DATE = "createDate";
	public ThreadPoolTaskExecutor taskExecutor = SpringContextHolder.getBean(ThreadPoolTaskExecutor.class);
	private Analyzer analyzer;
	private Directory directory;
	private volatile DirectoryReader reader = null; // 可以不用关闭
	private IndexWriter writer = null;// 需要关闭（不然必须关闭系统）这个必须关闭，非常重要
	private final Lock writerLock = new ReentrantLock();
	private final Lock readerLock = new ReentrantLock();
	
	// 非常重要 --- 刷新IndexReader时，防止关闭正在使用的IndexReader
	private final Map<Long, IndexReader> STALE_INDEX_READERS = new ConcurrentHashMap<Long, IndexReader>();
	// 低版本的IndexReader的存活时间 5s 
    private final int STALE_INDEXREADER_SURVIVAL_TIME = 10000;
	
	// 5秒后关闭
	private void closeStaleIndexReaders() {
		if(STALE_INDEX_READERS.isEmpty()) {return;}
		Iterator<Entry<Long, IndexReader>> entrys = STALE_INDEX_READERS.entrySet().iterator();
		while (entrys.hasNext()) {  
			Entry<Long, IndexReader> entry = entrys.next();  
			if ((System.currentTimeMillis() - entry.getKey()) >= STALE_INDEXREADER_SURVIVAL_TIME) {  
				IndexReader _reader = null;
				try {
					_reader = entry.getValue();
					_reader.close();
				} catch (IOException e) {} finally {
					entrys.remove();
				}
			}
		}
	}
	private void saveStaleIndexReaders(IndexReader reader) {
		STALE_INDEX_READERS.put(System.currentTimeMillis(), reader);
	}
	public BaseSearcher() {
		try{
			this.directory = this.openFSDirectory();
		}catch(Exception e){}
	}
	
	//------------- 工具方法 ---------------
	public FSDirectory openFSDirectory() throws IOException {
		return FSDirectory.open(Paths.get(new StringBuilder(LUCENE_INDEX_PATH).append(this.getModule()).toString()));
	}
	
	/**
	 * 不能关闭
	 */
	public void closeWriter() {
		if(writer != null) {
		   try {writer.close();} catch (IOException e) {e.printStackTrace();}
		   writer = null;
		}
	}
	public IndexWriter getIndexWriter() throws IOException {
		try{
			writerLock.lock();
			if(null == writer){
	          IndexWriterConfig config = new IndexWriterConfig(this.getAnalyzer());
			  config.setOpenMode(OpenMode.CREATE_OR_APPEND);
			  config.setMaxBufferedDeleteTerms(48);
	          writer = new IndexWriter(this.directory, config);
	        }
		}catch(Exception e) {e.printStackTrace();} finally {writerLock.unlock();}
		return writer;
	}
	public IndexReader getIndexReader() throws IOException {
		try{
			readerLock.lock();
			closeStaleIndexReaders();
			if(reader == null ) {
	           reader = DirectoryReader.open(directory);
	        } else {
	           DirectoryReader tr = DirectoryReader.openIfChanged(reader) ;
	           if(tr != null && reader != tr) {
	        	  //保存旧版本，防止关闭正在使用的
	        	  saveStaleIndexReaders(reader);
	              reader = tr;
	           }
	        }
		} finally {readerLock.unlock();}
		return reader;
	}
	public Directory getDirectory() {
		return directory;
	}
	//------------- 操作方法 ---------------
	/**
	 * 优化
	 */
	public synchronized void merge() {
		try{
			IndexWriter writer = this.getIndexWriter();
			writer.forceMerge(2);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.closeWriter();
		}
	}
	
	/**
	 * 判断是否存在
	 * @return
	 */
	public Boolean exists(String id) {
		try {
			IndexSearcher searcher = this.getSearcher();
			TopDocs docs = searcher.search(this.idQuery(id), 1);
			ScoreDoc[] hits = docs.scoreDocs;
			int len = hits.length;
			if (len == 1) {
		        return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (IOException e1) {
			return Boolean.FALSE; 
		}
	}
	
	/**
	 * 获取一条数据
	 * @param id
	 * @return
	 */
	public <E> E get(String id, Byte context) {
		try {
			IndexSearcher searcher = this.getSearcher();
			TopDocs docs = searcher.search(this.idQuery(id), 1);
			ScoreDoc[] hits = docs.scoreDocs;
			int len = hits.length;
			if(len == 1) {
		       Document doc = searcher.doc(hits[0].doc);
		       Map<String, String> values = Maps.newHashMap();
			   return this.render(doc, values, context);
			}
			return null;
		} catch (IOException e1) {
			return null; 
		}
	}
	
	/**
	 * 获取一条数据
	 * @param id
	 * @return
	 */
	private ScoreDoc getScoreDoc(String id, BooleanQuery query, Sort sort, IndexSearcher searcher) {
		if (!StringUtil3.isNotBlank(id)) {return null;}
		try {
			BooleanQuery.Builder builder = new BooleanQuery.Builder();
			builder.add(query, BooleanClause.Occur.MUST);
			builder.add(this.idQueryNoBoost(id), BooleanClause.Occur.MUST);
			TopDocs docs = searcher.search(builder.build(), 1, sort);
			ScoreDoc[] hits = docs.scoreDocs;
			int len = hits.length;
			if(len == 1) {return hits[0];}
			return null;
		} catch (IOException e1) {return null;}
	}
	
	/**
	 * 删除所有数据
	 */
	public synchronized void destory() {
		try{
			IndexWriter writer = this.getIndexWriter();
			boolean exist = DirectoryReader.indexExists(this.directory);
			if(exist){writer.deleteAll();}
			writer.commit(); 
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			this.closeWriter();
		}
	}
	
	/**
	 * 删除
	 * @param entity
	 */
	public synchronized void delete(T entity) {
		try{
			IndexWriter writer = this.getIndexWriter();
			boolean exist = DirectoryReader.indexExists(this.directory);
			if(exist) {
			   writer.deleteDocuments(this.idQuery(entity.getId()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			this.closeWriter();
		}
	}
	
	/**
	 * 删除
	 * @param entity
	 */
	public synchronized void _delete(Long id) {
		try{
			IndexWriter writer = this.getIndexWriter();
			boolean exist = DirectoryReader.indexExists(this.directory);
			if(exist) {
			   writer.deleteDocuments(this.idQuery(id));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			this.closeWriter();
		}
	}
	
	/**
	 * 多个 - 保存索引
	 */
	public synchronized void delete(List<T> entitys) {
		try{
			IndexWriter writer = this.getIndexWriter();
			boolean exist = DirectoryReader.indexExists(this.directory);
			if (exist) {
			   for(T t: entitys) {
				   writer.deleteDocuments(this.idQuery(t.getId()));
			   }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			this.closeWriter();
		}
	}
	
	/**
	 * 多个 - 保存索引
	 */
	public synchronized void _delete(List<Long> entitys) {
		try{
			IndexWriter writer = this.getIndexWriter();
			boolean exist = DirectoryReader.indexExists(this.directory);
			if (exist) {
			   for(Long t: entitys) {
				   writer.deleteDocuments(this.idQuery(t));
			   }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			this.closeWriter();
		}
	}
	
	/**
	 * 单个 - 保存索引
	 */
	public synchronized void save(T entity) {
		try{
			IndexWriter writer = this.getIndexWriter();
			boolean exist = DirectoryReader.indexExists(this.directory);
			if (exist) {
			    writer.deleteDocuments(this.idQuery(entity.getId()));
			}
			writer.addDocument(this.createDocumentWrap(entity));
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			this.closeWriter();
		}
	}
	
	/**
	 * 没有关闭Writer 请手动关闭
	 * 使用前请锁住，保证不会有其他线程关闭
	 * @param entity
	 */
	public void saveContinue(T entity) {
		try{
			IndexWriter writer = this.getIndexWriter();
			boolean exist = DirectoryReader.indexExists(this.directory);
			if (exist) {
			    writer.deleteDocuments(this.idQuery(entity.getId()));
			}
			writer.addDocument(this.createDocumentWrap(entity));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 多个 - 保存索引
	 */
	public synchronized void save(List<T> entitys) {
		try{
			IndexWriter writer = this.getIndexWriter();
			boolean exist = DirectoryReader.indexExists(this.directory);
			if (exist) {
			    for(T t: entitys) {
				    writer.deleteDocuments(this.idQuery(t.getId()));
			    }
			}
			for(T t: entitys) {
				writer.addDocument(this.createDocumentWrap(t));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.closeWriter();
		}
	}
	
	/**
	 * 查询数据
	 * @param builder
	 * @param sort
	 * @param page
	 * @param hfields ---- 需要高亮显示的字段
	 * @return
	 */
	public <E> ScorePage searchPage(BooleanQuery.Builder builder, Sort sort, ScorePage page, String[] hfields, Byte context) {
		builder.add(QUERY_ALL_TERM, BooleanClause.Occur.MUST);
		BooleanQuery query = builder.build();
		int pageNo = page.getParam().getPageIndex();
		int pageSize = page.getParam().getPageSize();
		try {
			IndexSearcher searcher = this.getSearcher();
			//快速高亮
			FastVectorHighlighter fast = this.supportFastHighlight() && hfields != null&& hfields.length != 0? this.createFastVectorHighlighter():null;;
			FieldQuery _query = fast != null?fast.getFieldQuery(query):null;
			//普通高亮
			Highlighter normal = hfields != null&& hfields.length != 0?this.createHighlighter(query):null;
			TopDocs docs = null;
			ScoreDoc after = this.getScoreDoc(page.getAfter(), query, sort, searcher);
			if(after != null) {
			   pageNo = 1; // 置为第一页
			   docs = searcher.searchAfter(after, query, pageSize, sort);
			} else {
			   docs = searcher.search(query, pageNo * pageSize, sort);
			}
			ScoreDoc[] hits = docs.scoreDocs;
			int endIndex = pageNo * pageSize;
			int len = hits.length;
			if (endIndex > len) { endIndex = len;}
			List<E> ts = Lists.newArrayList();
			for(int i = (pageNo - 1) * pageSize; docs != null && i < endIndex; i++) {
				Document doc = searcher.doc(hits[i].doc);
				E t = this.renderWrap(hfields, fast, _query, query, normal, searcher, hits[i], doc, context);
				ts.add(t);
			}
			page.setData(ts);
	        page.getParam().setRecordCount(docs.totalHits);
	        return page;
		} catch (IOException e1) { page.setData(Lists.newArrayList());  return page;}
	}
	
	/**
	 * 查询数据
	 * @param query --- 多查询条件
	 * @param sort
	 * @param page
	 * @return
	 */
	public List<T> searchList(BooleanQuery.Builder builder, Sort sort, int pageSize, String[] hfields, Byte context) {
		builder.add(QUERY_ALL_TERM, BooleanClause.Occur.MUST);
		BooleanQuery query = builder.build();
		try {
			IndexSearcher searcher = this.getSearcher();
			//快速高亮
			FastVectorHighlighter fast = this.supportFastHighlight() && hfields != null&& hfields.length != 0? this.createFastVectorHighlighter():null;;
			FieldQuery _query = fast != null?fast.getFieldQuery(query):null;
			//普通高亮
			Highlighter normal = hfields != null&& hfields.length != 0?this.createHighlighter(query):null;
			TopDocs docs = searcher.search(query, pageSize, sort);
			ScoreDoc[] hits = docs.scoreDocs;
			int endIndex = pageSize;
			int len = hits.length;
			if (endIndex > len) { endIndex = len;}
			List<T> ts = Lists.newArrayList();
			for(int i = 0; docs != null && i < endIndex; i++) {
				Document doc = searcher.doc(hits[i].doc);
				T t = this.renderWrap(hfields, fast, _query, query, normal, searcher, hits[i], doc, context);
				ts.add(t);
			}
	        return ts;
		} catch (IOException e1) {return Lists.newArrayList();}
	}
	
	/**
	 * 查询数据 -- 支持排序
	 * @param query --- 多查询条件
	 * @param sort
	 * @param page
	 * @return
	 */
	public <E> E searchOne(BooleanQuery.Builder builder, Sort sort, Byte context) {
		builder.add(QUERY_ALL_TERM, BooleanClause.Occur.MUST);
		BooleanQuery query = builder.build();
		try {
			IndexSearcher searcher = this.getSearcher();
			TopDocs docs = searcher.search(query, 1, sort);
			ScoreDoc[] hits = docs.scoreDocs;
			int len = hits.length;
			if(len == 1) {
		       Document doc = searcher.doc(hits[0].doc);
		       Map<String, String> values = Maps.newHashMap();
			   return this.render(doc, values, context);
			}
			return null;
		} catch (IOException e1) {return null;}
	}
	
	/**
	 * 查询一个（不支持排序 -- 请确认只有一条这样的记录）
	 * @param builder
	 * @param sort
	 * @param pageSize
	 * @param hfields
	 * @return
	 */
	public <E> E searchOne(BooleanQuery.Builder builder, Byte context) {
		try {
			IndexSearcher searcher = this.getSearcher();
			TopDocs docs = searcher.search(builder.build(), 1);
			ScoreDoc[] hits = docs.scoreDocs;
			int len = hits.length;
			if(len == 1) {
		       Document doc = searcher.doc(hits[0].doc);
		       Map<String, String> values = Maps.newHashMap();
			   return this.render(doc, values, context);
			}
			return null;
		} catch (IOException e1) {
			return null; 
		}
	}
	
	/**
	 * 下一个对象
	 * @param builder
	 * @param sort --- 排序条件一定要能唯一的确定顺序，才能实现上一条和下一条
	 * @param scoreId
	 * @return
	 */
	public ValueWrapper prevAndNext(BooleanQuery.Builder builder, Sort sort, String currId, String[] hfields) {
		builder.add(QUERY_ALL_TERM, BooleanClause.Occur.MUST);
		BooleanQuery query = builder.build();
		ValueWrapper value = new ValueWrapper();
		try {
			IndexSearcher searcher = this.getSearcher();
			ScoreDoc curr = this.getScoreDoc(currId, query, sort, searcher);
			if(curr == null) {return value;}
			Document doc = searcher.doc(curr.doc);
			//下一个
			TopDocs docs = searcher.searchAfter(curr, query, 1, sort);
			ScoreDoc[] hits = docs.scoreDocs;
			if(hits != null && hits.length == 1) {
			   doc = searcher.doc(hits[0].doc);
			   value.setNext(doc.get(ID));
			}
			//上一个（将查询条件反转） --- 必须有查询条件
			Sort _sort = this.getReverseSort(sort);
			docs = this.getSearcher().searchAfter(curr, query, 1, _sort);
			hits = docs.scoreDocs;
			if (hits != null && hits.length == 1) {
			    doc = searcher.doc(hits[0].doc);
			    value.setPrev(doc.get(ID));
			}
	        return value;
		} catch (Exception e1) {return value;}
	}
	
	/**
	 * 高亮某个实体
	 * @param builder
	 * @param sort
	 * @param currId
	 * @param hfields
	 * @return
	 */
	public <E> E highlighter(BooleanQuery.Builder builder, Sort sort, String currId, String[] hfields, Byte context) {
		builder.add(QUERY_ALL_TERM, BooleanClause.Occur.MUST);
		builder.add(this.idQueryNoBoost(currId), BooleanClause.Occur.MUST);
		BooleanQuery query = builder.build();
		try {
			IndexSearcher searcher = this.getSearcher();
			TopDocs docs = searcher.search(query, 1, sort);
			ScoreDoc[] hits = docs.scoreDocs;
			int len = hits.length;
			if(len == 1) {
			   QueryScorer queryScorer = new QueryScorer(query);
			   Formatter formatter = new SimpleHTMLFormatter(COLORED_PRE_TAGS[1], COLORED_POST_TAGS[0]);
			   Highlighter h = new Highlighter(formatter, queryScorer);
			   Document doc = searcher.doc(hits[0].doc);
			   Map<String, String> fastValues = Maps.newHashMap();
			   for(String field: hfields) {
					if(fastValues.containsKey(field)) {continue;}
					try {
						Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer, doc.get(field).length());
						h.setTextFragmenter(fragmenter);
						String value = h.getBestFragment(this.getAnalyzer().tokenStream(field, doc.get(field)), doc.get(field));
						if(null != value) {
						   fastValues.put(field, value);
						}
					} catch (Exception e) {}
			    }
			   return this.render(doc, fastValues, context);
			}
		} catch (IOException e) {return null;}
		return null;
	}
	
	//反转查询条件(不另外添加条件)
	private Sort getReverseSort(Sort sort) {
		List<SortField> _fields = Lists.newArrayList();
		SortField[] fields = sort.getSort();
		for(SortField field: fields) {
			_fields.add(new SortField(field.getField(), field.getType(), !field.getReverse()));
		}
		SortField[] rfields = new SortField[_fields.size()];
		rfields = _fields.toArray(rfields);
		return new Sort(rfields);
	}
	
	/**
	 * 获取数量，无条件则获取全部数量
	 * @param builder
	 * @return
	 */
	public Integer countByQuery(BooleanQuery.Builder builder) {
		try {
			builder.add(QUERY_ALL_TERM, BooleanClause.Occur.MUST);
			BooleanQuery query = builder.build();
			IndexSearcher searcher = this.getSearcher();
			TotalHitCountCollector collector =  new TotalHitCountCollector();
			searcher.search(query, collector);
			return collector.getTotalHits();
		} catch (IOException e1) {return 0;}
	}
	
	/**
	 * 实例化：会高亮显示必要的列
	 * @return
	 * @throws IOException 
	 */
	protected <E> E renderWrap(String[] fields, FastVectorHighlighter fast, FieldQuery fquery, Query query, Highlighter normal, IndexSearcher searcher, ScoreDoc sDoc, Document doc, Byte context) throws IOException {
		Map<String, String> fastValues = Maps.newHashMap();
		if (fast != null && !StringUtil3.contains(query.toString(), "spanNear")) { //执行高亮,高亮不支持SpanNear
			for(String field: fields) {
			   try {
				   String[] values = fast.getBestFragments(fquery, searcher.getIndexReader(), sDoc.doc, field, this.getHighlightChars(fields), this.getHighlightFragments(fields));
				   if(null != values && values.length != 0) {
					  String value = StringUtil3.join(values, HL_FRAGMENT_DIVIDE);
					  fastValues.put(field, value);
				   }
			   } catch (Exception e) {}
		    }
		}
		if (normal != null) { //普通高亮,每个字数不能过多，不然都连在一起了
			for(String field: fields) {
				if(fastValues.containsKey(field)) {continue;}
				try {
					String value = normal.getBestFragments(this.getAnalyzer().tokenStream(field, doc.get(field)), doc.get(field), this.getHighlightFragments(fields), HL_FRAGMENT_DIVIDE);
					if(null != value) {
					   fastValues.put(field, value);
					}
				} catch (Exception e) {}
		    }
		}
		return this.render(doc, fastValues, context);
	}
	
	/**
	 * 获得排序条件
	 * @param fields
	 * @param hasCondition -- 是否有查询条件
	 * @return
	 */
	public Sort getSort(List<SortField> fields) {
	    if(fields.size() != 0) {
	       SortField[] _fields = new SortField[fields.size()];
	 	   return new Sort(fields.toArray(_fields));
	    }
	    return Sort.RELEVANCE;
	}
	
	/**
	 * 获得分词器
	 * 1. 优先使用IKAnalyzer
	 * 2. 如果找不到则使用AnsjAnalyzer
	 * @return
	 */
	protected Analyzer getAnalyzer() {
		if (analyzer != null) {
			return analyzer;
		}
		analyzer = AnalyzerFactory.newAnalyzer();
		return analyzer;
	}
	
	/**
	 * 实现近实时查询，不关闭reader，但是Index有变化时，重新获取reader
	 * 多线程查询
	 * @return
	 * @throws IOException 
	 */
	private IndexSearcher getSearcher() throws IOException {
		IndexReader reader = this.getIndexReader();
		return new IndexSearcher(reader, taskExecutor.getThreadPoolExecutor());
	}
	
	/**
	 * id 的查询条件
	 * @param id
	 * @return
	 */
	private Query idQuery(Serializable id) {
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		if (id != null && StringUtil3.isNotBlank(id.toString())) {
			TermQuery q = new TermQuery(new Term(ID, id.toString()));
			builder.add(q, BooleanClause.Occur.MUST);
		}
		return builder.build();
	}
	
	/**
	 * id 的查询条件
	 * @param id
	 * @return
	 */
	private Query idQueryNoBoost(String id) {
		return new BoostQuery(new TermQuery(new Term(ID, id)),0);
	}
	
	/**
	 * 添加默认的字段
	 * @param t
	 * @return
	 */
	private Document createDocumentWrap(T t) {
		Document doc = this.createDocument(t);
		doc.add(new Field("_A", "*", StringField.TYPE_STORED));
		return doc;
	}
	
	/**
	 * 高亮的字段类型
	 * @return
	 */
	public FieldType vectorField(FieldType _type) {
		 FieldType type = new FieldType(_type);   
		 type.setStoreTermVectorOffsets(true);//记录相对增量  
	     type.setStoreTermVectorPositions(true);//记录位置信息  
	     type.setStoreTermVectors(true);//存储向量信息  
	     type.freeze();//阻止改动信息  
	     return type;
	}
	
	/**
	 * 返回需要快速高亮显示的字段
	 * @return
	 */
	protected boolean supportFastHighlight() {return Boolean.FALSE;}
	
	/**
	 * 创建快速高亮
	 * 注意查询的字段中必须包含要高亮的字段
	 * 且必须是存储的。否则获取不到高亮数据
	 * @param query
	 * @return
	 */
	protected FastVectorHighlighter createFastVectorHighlighter() {
	   FragListBuilder fragListBuilder = new SimpleFragListBuilder();
	   FragmentsBuilder fragmentsBuilder = new ScoreOrderFragmentsBuilder(COLORED_PRE_TAGS, COLORED_POST_TAGS);
	   return new FastVectorHighlighter(true,true, fragListBuilder, fragmentsBuilder);
	}
	
	/**
	 * 创建普通的高亮器
	 * 高亮的字段不一定在查询里面
	 * @param query
	 * @return
	 */
	protected Highlighter createHighlighter(Query query) {
		QueryScorer queryScorer = new QueryScorer(query);
		Formatter formatter = new SimpleHTMLFormatter(COLORED_PRE_TAGS[1], COLORED_POST_TAGS[0]);
		Highlighter h = new Highlighter(formatter, queryScorer);
		Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
        h.setTextFragmenter(fragmenter);
        return h;
	}
	
	/**
	 * 多字段的查询解释器
	 * 关键词查询
	 * 1. 判断是否需要间隔查询
	 * 2. 判断是否需要
	 * 注意：快速高亮不支持 SpanNearQuery(普通高亮支持但慢), 但支持 PhraseQuery（这个貌似够了）
	 *     请保留这段注释代码
	 *     单很悲催，PhraseQuery 必须要求查询字段有索引信息，所以还是切换到 - SpanNearQuery
	 * @return
	 */
	protected Query queryParser(String query, String[] fields) {
		if (StringUtil3.isNotBlank(query)) {
			//分割查询 ～
			if(StringUtil3.containsAny(query, INTERVAL_CHARS)) {
			   BooleanQuery.Builder builder = new BooleanQuery.Builder();
			   String[] groups = RegexpUtil.newRegexpMatcher("([^~～]*)[~～](.*)").getArrayGroups(query);
			   String _query = QueryParser.escape(groups[1]);
			   int slop = RegexpUtil.isNumber(groups[2])?Integer.parseInt(groups[2]):100;
			   List<String> keys = AnalyzerFactory.getKeywords(_query);
			   for(String field: fields) {
				   //PhraseQuery.Builder _b = new PhraseQuery.Builder();
				   //List<Term> terms = Lists.newArrayList();
				   List<SpanQuery> terms = Lists.newArrayList();
				   for(String key: keys) {
					   terms.add(new SpanTermQuery(new Term(field, key)));
					   //terms.add(new Term(field,key));
					   //_b.add(new Term(field,key));
				   }
				   //_b.setSlop(slop);
				   builder.add(terms.size() == 1? terms.get(0):new SpanNearQuery(terms.toArray(new SpanQuery[]{}), slop, false), BooleanClause.Occur.SHOULD);
				   //builder.add(terms.size() == 1? new TermQuery(terms.get(0)):_b.build(), BooleanClause.Occur.SHOULD);
			   }
			   return builder.build();
			}
			//默认使用多字段的解析（支持AND OR +）
			try {
				String _query = QueryParser.escape(query);
				MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, this.getAnalyzer());
				parser.setDefaultOperator(QueryParser.Operator.AND);
				return parser.parse(_query);
			} catch (ParseException e) {e.printStackTrace();}
		}
		return QUERY_ALL_TERM;
	}
	
	/**
	 * 包裹查询,查询ID DIVIDE
	 * @param query
	 * @return
	 */
	protected String idsQuery(String id) {
		return StringUtil3.format("*,%s,*", id);
	}
	
	/**
	 * 适用于 WildcardQuery 
	 * @return
	 */
	protected String idsField(String value) {
		if (StringUtil3.isBlank(value)) {
			return "";
		}
		return StringUtil3.format(",%s,", value);
	}
	
	/**
	 * 返回需要高亮显示的字段
	 * @return
	 */
	protected String[] getHighlights() {return null;}
	
	/**
	 * 返回高亮的字符数
	 * @return
	 */
	protected Integer getHighlightChars(String[] fields){return 100;}
	
	/**
	 * 返回高亮的组数
	 * @return
	 */
	protected Integer getHighlightFragments(String[] fields){return 5;}
	
	/**
	 * 指定模块
	 * @return
	 */
	protected abstract String getModule();
	
	/**
	 * 如何检索字段
	 * @param t
	 * @return
	 */
	protected abstract Document createDocument(T t);
	
	/**
	 * 实例化数据
	 * @param doc  --- 原始数据
	 * @param formate -- 高亮格式化
	 * @param scene -- 使用场景
	 * @return
	 */
	protected abstract <E> E render(Document doc, Map<String, String> fastValues, Byte scene);
	
	
	/**
	 * 刷新实体
	 * @param id
	 */
	protected abstract void refresh(List<Long> updates);
}