package com.example;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.example.blog.BlogIndexer;
import com.example.lucene.LuceneIndexer;
import com.example.lucene.LuceneSearcher;
import com.example.lucene.SearchOption;
import com.example.lucene.SearchResult;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.junit.jupiter.api.Test;

public class BlogIndexingTest {

 
  /** 인덱스 생성 테스트 케이스 */
  @Test
  public void testLuceneIndexer() throws Exception  {
    int seq = 1000;
    String id = null; 
    String title = null; 
    String contents = null; 
    BlogIndexer indexer = new BlogIndexer(OpenMode.CREATE);
    for(int i=0; i < 900; i++) {
      id = String.valueOf(seq); 
      title = "주간보고 올려주세요.";
      contents = "잘 살아보자. 주간보고. 일일보고";
      indexer.addDocument(id, title, contents, "홍길동", Date.from(Instant.now()));
      seq++; 
    }
    indexer.close();
    System.out.println("Indexing closed.");
  }

  /** 검색 테스트 케이스  */
  @Test
  public void testSearch() throws Exception  {
    //String queryStr = "홍길동";
    //BlogSearch.search(queryStr);
    //List<Document> list = BlogSearch.searchFiles("title", queryStr);
    // List<Document> list = BlogSearch.searchFiles2(queryStr);
    // for(Document d : list) {
    //   System.out.println(d.get("title"));
    // }
    List<String> searchFields = Arrays.asList("title", "author", "contents");
    List<String> must = Arrays.asList("주간보고");
    //List<String> should = Arrays.asList("홍길동", "주간보고");
    List<String> should = Arrays.asList("홍길동", "박신혜");
    SearchOption options = new SearchOption();
    options.setMust(must);
    options.setShould(should);
    options.setSearchFields(searchFields);
    SearchResult result  = LuceneSearcher.search(options, 2, 10);
    List<Document> list = result.getDocuments();
    for(Document d : list) {
      System.out.println(d.get("id") + "/" + d.get("title") + "/" + d.get("contents") + "/" + d.get("author") + "/" + d.get("updDate"));
    }
  }//;

  /**
   * 인덱스 삭제 테스트 케이스 
   * @throws Exception
   */
  @Test
  public void testDeleteDoc() throws Exception { 
    String id = "1111";
    LuceneIndexer indexBase = new LuceneIndexer(OpenMode.CREATE_OR_APPEND); 
    indexBase.delete(id);
  }//:


  /** 인덱스 업데이트 테스트 케이스  */
  @Test
  public void testUpdateDoc() throws Exception { 
    String id = "1111";
    LuceneIndexer indexBase = new LuceneIndexer(OpenMode.CREATE_OR_APPEND); 
    indexBase.delete(id);
  }//:

}///~
