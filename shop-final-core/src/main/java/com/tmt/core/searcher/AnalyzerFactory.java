package com.tmt.core.searcher;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.tmt.core.utils.Lists;

/**
 * 创建分词器
 * @author root
 */
public class AnalyzerFactory {

	// 单例
	private static Analyzer analyzer;
	
	/**
	 * 创建默认的分词器
	 * @return
	 */
	public static Analyzer newAnalyzer() {
		// 只保留一份
		if (analyzer != null) {return analyzer;}
		
		// 初始化
		synchronized (AnalyzerFactory.class) {
			// 默认的IK 分词器
			if(analyzer == null) {
				try {
					analyzer = (org.apache.lucene.analysis.Analyzer) Class.forName("org.wltea.analyzer.lucene.IKAnalyzer").newInstance();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {}
			}
			
			// 默认的ANSJ 分词器
			if(analyzer == null) {
				try {
					analyzer = (org.apache.lucene.analysis.Analyzer) Class.forName("org.ansj.analyer.lucene.AnsjAnalyzer").newInstance();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return analyzer;
	}
	
	/**
	 * 得到分词结果
	 * @param query
	 * @return
	 */
	public static List<String> getKeywords(String query) {
		TokenStream stream = null;
        try{
        	Analyzer analyzer = AnalyzerFactory.newAnalyzer();
        	
        	stream = analyzer.tokenStream("content", new StringReader(query));
            CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
            stream.reset();
 
            List<String> words = Lists.newArrayList();
            while (stream.incrementToken()) {
                words.add(term.toString());
            }
            return words;
        }catch (IOException ex){}finally{  
            IOUtils.closeQuietly(stream);
        }  
        return Lists.newArrayList();
	}
}