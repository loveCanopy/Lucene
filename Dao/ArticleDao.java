package Dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.LockObtainFailedException;

import Utils.luceneUtils;
import entity.Article;

public class ArticleDao {

	//增加一个
	public void add(Article article ){
		try {
			Document actToDoc = luceneUtils.ActToDoc(article);
			IndexWriter indexWriter=new IndexWriter(luceneUtils.getDirectory(), luceneUtils.getAnalyzer(), luceneUtils.getLength());
		    indexWriter.addDocument(actToDoc);
		    indexWriter.close();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	//增加多个
    public void addAll(List<Article> list ){
		for(Article article:list){
			add(article);
		}
		
	}
	
	//删除一个
    public void delete(String id){
    	
    	try {
			
			IndexWriter indexWriter=new IndexWriter(luceneUtils.getDirectory(), luceneUtils.getAnalyzer(), luceneUtils.getLength());
		    indexWriter.deleteDocuments(new Term("id",id));
		    indexWriter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
    }
	//删除多个
    
    public void deleteAll() throws Exception, LockObtainFailedException, IOException{
    	IndexWriter indexWriter = new IndexWriter(luceneUtils.getDirectory(),luceneUtils.getAnalyzer(),luceneUtils.getLength());
		indexWriter.deleteAll();
		indexWriter.close();
    	
    }
	
	//修改
	public void update(String id,Article article ) throws NoSuchMethodException, Exception{
		Document actToDoc = luceneUtils.ActToDoc(article);
		IndexWriter indexWriter=new IndexWriter(luceneUtils.getDirectory(), luceneUtils.getAnalyzer(), luceneUtils.getLength());
        indexWriter.updateDocument(new Term("id",id),actToDoc); 
        indexWriter.close();
    	
	
	}
	
	//查询
	public void find(String keywords) throws Exception{
		List<Article> articleList = new ArrayList<Article>();
		QueryParser queryParser = new QueryParser(luceneUtils.getVersion(),"content",luceneUtils.getAnalyzer());
		Query query = queryParser.parse(keywords);
		IndexSearcher indexSearcher = new IndexSearcher(luceneUtils.getDirectory());
		TopDocs topDocs = indexSearcher.search(query,100);
		for(int i=0;i<topDocs.scoreDocs.length;i++){
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			int no = scoreDoc.doc;
			Document document = indexSearcher.doc(no);
			Article article = luceneUtils.DocToArt(document,Article.class);
			articleList.add(article);
		}
		for(Article a : articleList){
			System.out.println( a );
		}
	}
	
}
