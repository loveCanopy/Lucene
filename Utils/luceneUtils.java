package Utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import entity.Article;

/**
 * @author Sherlock
 * luceneUtils������
 */


public class luceneUtils {

	
	private static  Directory directory = null;
	public static Directory getDirectory() {
		return directory;
	}


	public static Version getVersion() {
		return version;
	}


	public static Analyzer getAnalyzer() {
		return analyzer;
	}


	public static MaxFieldLength getLength() {
		return length;
	}


	private static Version version=null;
	//�ִ���
    private	static Analyzer analyzer=null;
	//�ı���ֳ����ٴʻ�
	private static MaxFieldLength length=null;
	
	static{
		version=Version.LUCENE_20;
		analyzer=new StandardAnalyzer(version);
		 length=MaxFieldLength.LIMITED;
		 try {
				directory = FSDirectory.open(new File("e:\\indexdb"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	//˽�й��캯��  ����ֹnew ����
	private luceneUtils(){}
	
	
	/**
	 * Document ת��ΪArticle
	 * @param <T>
	 * @throws Exception 
	 * @throws IllegalAccessException 
	 * 
	 */
	public static <T>T DocToArt(Document document,Class clazz) throws IllegalAccessException, Exception{
		  T t=(T) clazz.newInstance();
		 java.lang.reflect.Field[] fields = clazz.getDeclaredFields(); //�õ����е�����
		 for( java.lang.reflect.Field field:fields){
			 field.setAccessible(true);
			 //Article������ֵ
			 String name=field.getName();
			 String value=document.get(name);//document���ֶ�����ֵ
			 BeanUtils.setProperty(t, name, value);
		     
	}
	
	          return t;
	}
	
	
	/**
	 * Articleת��Document 
	 * @param <T>
	 * @throws Exception 
	 * @throws NoSuchMethodException 
	 *
	 */
	public static Document ActToDoc(Object obj) throws NoSuchMethodException, Exception{
		
		Document document = new Document();
		
		Class clazz = obj.getClass();
		
		java.lang.reflect.Field[] reflectFields = clazz.getDeclaredFields();
		
		for(java.lang.reflect.Field reflectField : reflectFields){
			
			reflectField.setAccessible(true);
			
			String name = reflectField.getName();
			
			String methodName = "get" + name.substring(0,1).toUpperCase()+name.substring(1);
			
			Method method = clazz.getMethod(methodName,null);
			
			String value = method.invoke(obj,null).toString();
			
			document.add(new Field(name,value,Store.YES,Index.ANALYZED));
		}
		
		return document;
	}
	
	
	
}
