package com.example.blog;

import java.util.Date;

import com.example.lucene.LuceneIndexer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;

/**
 * Blog 인덱스 생성을 한다. 
 */
public class BlogIndexer {
  /** Indexer  */
  private LuceneIndexer indexer = null;
  /**
   * 생성자 
   * @param openMode OpenMode 값 
   * @throws Exception
   */
  public BlogIndexer(OpenMode openMode) throws Exception  {
    this.indexer = new LuceneIndexer(openMode); 
  }
  /**
   * IndexWriter를 닫는다. 
   * @throws Exception
   */
  public void close() throws Exception  {
    this.indexer.close();
  }
  /**
   * 문서를 추가한다  
   * @param id id 필드 값 
   * @param title title 필드 값 
   * @param contents  contents 필드 값 
   * @param author author 필드 값 
   * @param updDate  updDate 필드 값 
   * @throws Exception
   */
  public void addDocument(String id, String title, String contents, String author, Date updDate) throws Exception   {
    Document document = new Document();
    document.add(new TextField("id", id, Field.Store.YES));
    document.add(new TextField("title", title, Field.Store.YES));
    document.add(new TextField("author", author, Field.Store.YES));
    document.add(new TextField("contents", contents, Field.Store.YES));
    document.add(new TextField("updDate", updDate.toInstant().toString(), Field.Store.YES));
    
    indexer.add(document);
  }
}/// ~