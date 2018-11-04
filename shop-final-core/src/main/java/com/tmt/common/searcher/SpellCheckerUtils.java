package com.tmt.common.searcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.tmt.common.utils.Lists;

/**
 * 查询建议
 * @author root
 */
public class SpellCheckerUtils {

	//查询器
	private static _SpellChecker _SpellChecker = null;
	
	/**
	 * 清除索引
	 */
	public static void destory() {
		getSpellChecker().destory();
	}
	
	/**
	 * 添加匹配的索引(如果需要关闭dic 请自己关闭)
	 * @param dic --- 已经存在的索引
	 * @param field --- 对应的字段
	 */
	public static void indexDictionary(Directory dic, String field) {
		getSpellChecker().indexDictionary(dic, field);
	}
	
	/**
	 * 添加匹配的索引(如果需要关闭dic 请自己关闭)
	 * @param dic --- 已经存在的索引
	 * @param field --- 对应的字段
	 */
	public static void indexDictionary(Directory dic, String ...fields) {
		for(String field: fields) {
		   getSpellChecker().indexDictionary(dic, field);
		}
	}
	
	/**
	 * 返回建议的字段
	 * @param word
	 * @param numSug
	 * @param accuracy
	 * @return
	 */
	public static List<String> suggestSimilar(String word, int numSug) {
		return suggestSimilar(word, numSug, 0.5f);
	}
	
	/**
	 * 返回建议的字段
	 * @param word
	 * @param numSug
	 * @param accuracy -- 相相似度默认0.5
	 * @return
	 */
	public static List<String> suggestSimilar(String word, int numSug, float accuracy) {
		List<String> suggests = Lists.newArrayList();
		String[] _suggests = getSpellChecker().suggestSimilar(word, numSug, accuracy);
		if (_suggests != null && _suggests.length != 0) {
			suggests.addAll(Lists.newArrayList(_suggests));
		}
		return suggests;
	}
	
	private static _SpellChecker getSpellChecker() {
		if(_SpellChecker == null) {
		   _SpellChecker = new _SpellChecker();
		}
		return _SpellChecker;
	}
	
	//查询匹配器
	private static class _SpellChecker{
		private Analyzer analyzer = null;
		private SpellChecker spellChecker = null;
		private Directory directory;
		private _SpellChecker() {
			try {
				analyzer = AnalyzerFactory.newAnalyzer();
				directory = FSDirectory.open(Paths.get(new StringBuilder(BaseSearcher.LUCENE_INDEX_PATH).append(File.separator).append("spell").toString()));
				spellChecker = new SpellChecker(directory);
			} catch (IOException e) {}
		}
		/**
		 * 删除所有数据
		 */
		private void destory() {
			IndexWriter writer = null;
			try{
				IndexWriterConfig config = new IndexWriterConfig(this.getAnalyzer());
				config.setOpenMode(OpenMode.CREATE_OR_APPEND);
				writer = new IndexWriter(this.directory, config);
				boolean exist = DirectoryReader.indexExists(this.directory);
				if(exist){writer.deleteAll();}
				writer.commit(); 
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(writer != null) {
				   try {writer.close();} catch (IOException e) {e.printStackTrace();}
				   writer = null;
				}
			}
		}
		
		private void indexDictionary(Directory dic, String field) {
            IndexReader oriIndex;
			try {
				IndexWriterConfig config = new IndexWriterConfig(analyzer);
	  		    config.setOpenMode(OpenMode.CREATE_OR_APPEND);
				oriIndex = DirectoryReader.open(dic);
				LuceneDictionary dict = new LuceneDictionary(oriIndex, field); //已存在索引的字段
		        spellChecker.indexDictionary(dict, config, true);
		        oriIndex.close();
			} catch (Exception e) {e.printStackTrace();}
		}
		private String[] suggestSimilar(String word, int numSug, float accuracy) {
			try {
				return spellChecker.suggestSimilar(word, numSug, accuracy);
			} catch (IOException e) {}
			return null;
		}
		private Analyzer getAnalyzer() {
			return analyzer;
		}
	}
}