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
		
		Article article=new Article(1, "��ѵ", "����������һ����ѵ����");
		Document  document=new Document();
		
		/*
		 * xid����ֵ�Ƿ���ԭʼ��¼����ʻ��
		 * Store.YES ��
		 * Store.NO ����
		 * һ���xid���ݶ��� ,�ٶ�����1 ,������
		 * 
		 * �Ƿ�Xid����ֵ���зִʲ���
		 * Index.ANALYZED ��ʾ������ֵ�ִʴ洢�����ʻ���
		 * ...
		 * */
		
		document.add(new Field("xid",String.valueOf(article.getId()),Store.YES,Index.ANALYZED));
		document.add(new Field("xtitle",article.getTitle(),Store.YES,Index.ANALYZED));
		document.add(new Field("xcontent",article.getContent(),Store.YES,Index.ANALYZED));
		//Documentд��lucene��������
		Directory directory = null;
		try {
			directory = FSDirectory.open(new File("e:\\indexdb"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//lucene�汾
		Version version=Version.LUCENE_20;
		//�ִ���
		Analyzer analyzer=new StandardAnalyzer(version);
		//�ı���ֳ����ٴʻ�
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
		
		String keywords="��";
		Directory directory = null;
		List<Article> list=new ArrayList<Article>();
		try {
			directory = FSDirectory.open(new File("e:\\indexdb"));
			IndexSearcher indexSearcher=new IndexSearcher(directory);
			Version version=Version.LUCENE_20;
		  
		    String f="xcontent";
			Analyzer a=new StandardAnalyzer(version);
			//��ѯ��  ��xcontent��ѯ ָ���汾  �����
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
