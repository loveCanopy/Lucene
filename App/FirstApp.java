package App;

import java.io.File;
import java.io.IOException;


import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.junit.Ignore;
import org.junit.Test;

import entity.Article;
public class FirstApp {

	@Test  @Ignore
	public void test() {
		
		Article article=new Article(1, "培训", "北大青鸟是一个培训机构");
		Document  document=new Document();
		
		/*
		 * xid属性值是否由原始记录存入词汇表
		 * Store.YES 存
		 * Store.NO 不存
		 * 一般非xid数据都存 ,百度搜素1 ,无意义
		 * 
		 * 是否将Xid属性值进行分词策略
		 * Index.ANALYZED 表示该属性值分词存储，即词汇拆分
		 * ...
		 * */
		
		document.add(new Field("xid",String.valueOf(article.getId()),Store.YES,Index.ANALYZED));
		document.add(new Field("xtitle",article.getTitle(),Store.YES,Index.ANALYZED));
		document.add(new Field("xcontent",article.getContent(),Store.YES,Index.ANALYZED));
		//Document写入lucene索引库中
		Directory directory = null;
		try {
			directory = FSDirectory.open(new File("e:\\indexdb"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//lucene版本
		Version version=Version.LUCENE_20;
		//分词器
		Analyzer analyzer=new StandardAnalyzer(version);
		//文本拆分出多少词汇
		MaxFieldLength length=MaxFieldLength.LIMITED;
		IndexWriter indexWriter;
		try {
			indexWriter = new IndexWriter(directory, analyzer,length );
			indexWriter.addDocument(document);
			indexWriter.close();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test @Ignore
	public void search(){
		
		String keywords="大";
		Directory directory = null;
		List<Article> list=new ArrayList<Article>();
		try {
			directory = FSDirectory.open(new File("e:\\indexdb"));
			IndexSearcher indexSearcher=new IndexSearcher(directory);
			Version version=Version.LUCENE_20;
		  
		    String f="xcontent";
			Analyzer a=new StandardAnalyzer(version);
			//查询器  按xcontent查询 指定版本  拆分器
			QueryParser parser=new QueryParser(version, f, a);
			Query query = null;
			try {
				query = parser.parse(keywords);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 int n=100;
			TopDocs topdocs = indexSearcher.search(query, n);
			for(int i=0;i<topdocs.scoreDocs.length;i++){
				 ScoreDoc scoreDoc = topdocs.scoreDocs[i];
				 int no=scoreDoc.doc;
				 Document doc = indexSearcher.doc(no);
				 //Document===>Article
				 String xid = doc.get("xid");
				 String xtitle = doc.get("xtitle");
				 String xcontent = doc.get("xcontent");
			     Article article=new Article(Integer.valueOf(xid), xtitle, xcontent);
			     list.add(article);
			}
			
			for(Article article:list){
				System.out.println(article);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
}
