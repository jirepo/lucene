package com.example;

import java.io.IOException;
import java.nio.file.Paths;

import com.example.lucene.NgramAnalyzer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.TieredMergePolicy;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;


/**
 * 검색 기초 설명을 위한 클래스 
 */
public class FileIndex {

  public static String INDEX_PATH = "E:/lucene";

  // https://devyongsik.tistory.com/349

  /**
   * IndexWriter 생성 
   * @return
   *   IndexWriter 인스턴스 
   * @throws IOException
   */
  public static IndexWriter createIndexWriter() throws IOException {
    NgramAnalyzer analyzer = new NgramAnalyzer();
    FSDirectory dir = FSDirectory.open(Paths.get(INDEX_PATH));
    IndexWriterConfig config = new IndexWriterConfig(analyzer);

    // OpenMode.CREATE - 색인시마다 기존 색인 삭제 후 재 색인
    // OpenMode.CREATE_OR_APPEND - 기존 색인이 없으면 만들고, 있으면 append 함. 
    // OpenMode.APPEND - 기존 색인에 추가
    config.setOpenMode(OpenMode.CREATE_OR_APPEND);
    TieredMergePolicy mergePolicy = new TieredMergePolicy();
    config.setMergePolicy(mergePolicy);
    IndexWriter indexWriter = new IndexWriter(dir, config);
    return indexWriter;
  }

  /**
   * 인덱스 문서 추가 
   * @param w  IndexWriter 인스턴스 
   * @param title 제목 필드 값 
   * @param isbn  isbn 필드 값 
   * @throws IOException
   */
  private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
    Document doc = new Document();
    doc.add(new TextField("title", title, Field.Store.YES));

    // use a string field for isbn because we don't want it tokenized
    doc.add(new StringField("isbn", isbn, Field.Store.YES));
    w.addDocument(doc);
  }

  /** 초기화  */
  private static void init() throws IOException {
    IndexWriter w = createIndexWriter();
    addDoc(w, "Lucene in Action", "193398817");
    addDoc(w, "Lucene for Dummies", "55320055Z");
    addDoc(w, "Managing Gigabytes", "55063554A");
    addDoc(w, "The Art of Computer Science", "9900333X");
    w.close();
  }

  /**
   * 검색 
   * @throws Exception
   */
  private static void search() throws Exception {
    FSDirectory index = FSDirectory.open(Paths.get(INDEX_PATH));
    StandardAnalyzer analyzer = new StandardAnalyzer();

    // 2. query
    // String querystr = args.length > 0 ? args[0] : "lucene";
    String querystr = "lucene";

    // the "title" arg specifies the default field to use
    // when no field is explicitly specified in the query.
    Query q = new QueryParser("title", analyzer).parse(querystr);

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
      System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
    }

    // reader can only be closed when there
    // is no need to access the documents any more.
    reader.close();
  }

  /** 메인  */
  public static void main(String[] args) throws Exception {
    init();
    search();
  }

}/// ~
