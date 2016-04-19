package App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Ignore;
import org.junit.Test;

import Dao.ArticleDao;
import Utils.luceneUtils;
import entity.Article;

public class SecondApp {

	
	ArticleDao articleDao=new ArticleDao();
	@Test 
	public void add(){
		Article article=new Article(1, "培训", "北大青鸟是一个培训机构");
		
		articleDao.add(article);
	}
	@Test
	public void find() throws Exception{
		String keywords="培训";
		articleDao.find(keywords);
	}
	@Test 
	public void addAll(){
		List<Article> list=new ArrayList<Article>();
		list.add(new Article(2, "培训", "清华青鸟是一个培训机构"));
		list.add(new Article(3, "培训", "吉大青鸟是一个培训机构"));
		list.add(new Article(4, "培训", "武大青鸟是一个培训机构"));
		list.add(new Article(5, "培训", "浙大青鸟是一个培训机构"));
		articleDao.addAll(list);
	}
	@Test 
	public void delete(){
		String id="2";
		articleDao.delete(id);
	}
	@Test 
	public void deleteAll() throws Exception{
		articleDao.deleteAll();
	}
	@Test 
	public void update() throws Exception{
		String id="2";
		Article article=new Article(1, "培训", "及格培训");
		articleDao.update(id,article);
	}
	
	
}
