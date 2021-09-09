package com.example;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.example.lucene.NgramAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 검색을 설명하기 위힌 테스트 코드
 */
public class BlogSearch {

  public static String INDEX_PATH = "E:/lucene";

  public static List<Document> searchFiles(String inField, String queryString) throws Exception {
    // StandardAnalyzer analyzer = new StandardAnalyzer();
    NgramAnalyzer analyzer = new NgramAnalyzer();
    Query query = new QueryParser(inField, analyzer).parse(queryString);
    Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_PATH));
    IndexReader indexReader = DirectoryReader.open(indexDirectory);
    IndexSearcher searcher = new IndexSearcher(indexReader);
    TopDocs topDocs = searcher.search(query, 10);
    // List<ScoreDoc> list = Arrays.asList(topDocs.scoreDocs);
    List<Document> list = new ArrayList<Document>();
    for (ScoreDoc sdoc : topDocs.scoreDocs) {
      Document d = searcher.doc(sdoc.doc);
      list.add(d);
    }
    indexReader.close();
    return list;
    // return topDocs.scoreDocs.stream()
    // .map(scoreDoc -> searcher.doc(scoreDoc.doc))
    // .collect(Collectors.toList());
  }//:



  public static List<Document> searchFiles2(String queryString) throws Exception {
    Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_PATH));
    IndexReader indexReader = DirectoryReader.open(indexDirectory);
    IndexSearcher searcher = new IndexSearcher(indexReader);
    //Query query = new QueryParser(inField, analyzer).parse(queryString);
    Query q1 = new TermQuery(new Term("title", queryString));
    Query q2 = new TermQuery(new Term("contents", queryString));
    Query q3 = new TermQuery(new Term("author", queryString));
    BooleanQuery.Builder builder = new BooleanQuery.Builder();
    builder.add(q1, BooleanClause.Occur.SHOULD);
    builder.add(q2, BooleanClause.Occur.SHOULD);
    builder.add(q3, BooleanClause.Occur.SHOULD);
    
    TopDocs topDocs = searcher.search(builder.build(), 10);
    // List<ScoreDoc> list = Arrays.asList(topDocs.scoreDocs);
    List<Document> list = new ArrayList<Document>();
    for (ScoreDoc sdoc : topDocs.scoreDocs) {
      Document d = searcher.doc(sdoc.doc);
      list.add(d);
    }
    indexReader.close();
    return list;
    // return topDocs.scoreDocs.stream()
    // .map(scoreDoc -> searcher.doc(scoreDoc.doc))
    // .collect(Collectors.toList());
  }//:
 

  public static void search(String queryStr) throws Exception {
    FSDirectory index = FSDirectory.open(Paths.get(INDEX_PATH));
    // StandardAnalyzer analyzer = new StandardAnalyzer();
    NgramAnalyzer analyzer = new NgramAnalyzer();

    // 2. query
    // String querystr = args.length > 0 ? args[0] : "lucene";
    // String querystr = "lucene";

    // the "title" arg specifies the default field to use
    // when no field is explicitly specified in the query.
    Query q = new QueryParser("title", analyzer).parse(queryStr);

    // 3. search
    int hitsPerPage = 10;
    IndexReader reader = DirectoryReader.open(index);
    IndexSearcher searcher = new IndexSearcher(reader);
    TopDocs docs = searcher.search(q, hitsPerPage);
    ScoreDoc[] hits = docs.scoreDocs;
    // 4. display results
    System.out.println("Found " + hits.length + " hits.");
    for (int i = 0; i < hits.length; ++i) {
      int docId = hits[i].doc;
      Document d = searcher.doc(docId);
      System.out.println((i + 1) + ". " + d.get("contents") + "\t" + d.get("title") + "\t" + d.get("id"));
    }

    // reader can only be closed when there
    // is no need to access the documents any more.
    reader.close();
  }

}
