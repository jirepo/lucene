package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.lucene.NgramAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.junit.jupiter.api.Test;

public class AnalyzerTest {

  private static final String SAMPLE_TEXT = "This is baeldung.com Lucene Analyzers test";
  private static final String FIELD_NAME = "1sssampleName";
  // get the standard lucene stopword set
  // private Set<?> stopWords = StopAnalyzer.PER_FIELD_REUSE_ST



  /**
   * Analyzer가 어떻게 text를 분석하는지 보여주기 위한 메서드이다. 
   * @param text  분석할 문자열 
   * @param analyzer  Analyzer 인스턴스 
   * @return
   *   토큰을 리스트에 담아서 반환한다. 
   * @throws IOException
   */
  public List<String> analyze(String text, Analyzer analyzer) throws IOException {
    List<String> result = new ArrayList<String>();
    TokenStream tokenStream = analyzer.tokenStream(FIELD_NAME, text);
    CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
    tokenStream.reset();
    while (tokenStream.incrementToken()) {
      result.add(attr.toString());
    }
    return result;
  }//:


  /**
   * StandardAnalyzer의 분석결과를 출력한다. 
   * @throws IOException
   */
  @Test
  public void testStandardAnalyze() throws IOException {
    String source = "언어별로 사랑한다는말을하고싶어서 단어의 변화는 모두 다르기 ANAF <html><body></body></html> Love is Blue. 때문에 언어별로 Analyzer를 특화시켜서 사용해야 한다";
    //List<String> result = analyze(SAMPLE_TEXT, new StandardAnalyzer());
    List<String> result = analyze(source, new StandardAnalyzer());
    for (String item : result) {
      System.out.println(item);
    }
  }

  /**
   * StopAnalyzer의 분석결과를 출력한다. 
   * @throws IOException
   */
  @Test
  public void testStopAnalyzer() throws IOException {
    CharArraySet stopWords = StopFilter.makeStopSet("is", "com", "lucene");
    List<String> result = analyze(SAMPLE_TEXT, new StopAnalyzer(stopWords));
    for (String item : result) {
      System.out.println(item);
    }
  }

  /**
   * WhiteSpaceAnalyzer의 분석결과를 출력한다. 
   * @throws IOException
   */
  @Test
  public void testWhiteSpaceAnalyzer() throws IOException {
    List<String> result = analyze(SAMPLE_TEXT, new WhitespaceAnalyzer());
    for (String item : result) {
      System.out.println(item);
    }
  }

  /**
   * KeyWordAnalyzer의 분석결과를 출력한다. 
   * @throws IOException
   */
  @Test
  public void testKeywordAnalyzer() throws IOException {
    List<String> result = analyze(SAMPLE_TEXT, new KeywordAnalyzer());
    for (String item : result) {
      System.out.println(item);
    }
  }

  /**
   * EnglishAnalyzer의 분석결과를 출력한다. 
   * @throws IOException
   */
  @Test
  public void testEnglishAnalyzer() throws IOException {
    List<String> result = analyze(SAMPLE_TEXT, new EnglishAnalyzer());
    for (String item : result) {
      System.out.println(item);
    }
  }


  /**
   * CustomAnzlyer Builder를 사용한 분석결과를 출력한다. 
   * @throws IOException
   */
  @Test
  public void testCustomAnalyzerBuilder() throws IOException {
    Analyzer analyzer = CustomAnalyzer.builder()
        .withTokenizer("standard")
        .addTokenFilter("lowercase")
        .addTokenFilter("stop")
        .addTokenFilter("porterstem")
        .addTokenFilter("capitalization")
        .build();
    List<String> result = analyze(SAMPLE_TEXT, analyzer);
    for (String item : result) {
      System.out.println(item);
    }
  }



  /**
   * MyCustomAnalyzer를 테스트하기 위한 테스트 케이스.  InMemoryLuceneInde 클래스도 사라져서 테스트할 수 없음.
   */
  @Test
  public void givenTermQuery_whenUseCustomAnalyzer_thenCorrect() {
    // InMemoryLuceneInde 클래스도 사라져서 테스트할 수 없음
        // InMemoryLuceneIndex luceneIndex = new InMemoryLuceneIndex(new RAMDirectory(), new MyCustomAnalyzer());
    // luceneIndex.indexDocument("introduction", "introduction to lucene");
    // luceneIndex.indexDocument("analyzers", "guide to lucene analyzers");
    // Query query = new TermQuery(new Term("body", "Introduct"));

    // List<Document> documents = luceneIndex.searchIndex(query);
    // assertEquals(1, documents.size());
  }


  /**
   * TokenStream이 생성될때 ArrtibuteSrouce에 보고자 하는 정보를 등록해 놓으면 그때 그때의 snap shot을 확인할 수 있다. 
   * @throws Exception
   */
  @Test 
  public void testAttribute() throws Exception  {
    String source = "This is baeldung Lucene Analyzers test. ";
    Analyzer analyzer = new StandardAnalyzer();
    TokenStream tokenStream = analyzer.tokenStream("sample", source);
    CharTermAttribute attr1 = tokenStream.addAttribute(CharTermAttribute.class);
    OffsetAttribute attr2   = tokenStream.addAttribute(OffsetAttribute.class);
    //PositionLengthAttribute attr3 = tokenStream.addAttribute(PositionLengthAttribute.class);
    tokenStream.reset();
    while (tokenStream.incrementToken()) {
      System.out.println(attr1.toString());
      System.out.println(attr2.startOffset() + ":" + attr2.endOffset());
    }
    analyzer.close();
  }




  /**
   * NgramAnzlyer 테스트 케이스. NgramAnzlyer의 분석결과를 출력한다. 
   * @throws Exception
   */
  @Test 
  public void testNgramAnzlyer() throws Exception  {
    // StopFilter –> LowerCaseFilter –> KoreanFilter  –> KoreanTokenizer
    String source = "언어별로 사랑한다는말을하고싶어서 단어의 변화는 모두 다르기 ANAF <html><body></body></html> Love is Blue. 때문에 언어별로 Analyzer를 특화시켜서 사용해야 한다";
    Analyzer analyzer = new NgramAnalyzer();
    TokenStream tokenStream = analyzer.tokenStream("sample", source);
    CharTermAttribute attr1 = tokenStream.addAttribute(CharTermAttribute.class);
    //PositionLengthAttribute attr3 = tokenStream.addAttribute(PositionLengthAttribute.class);
    tokenStream.reset();
    while (tokenStream.incrementToken()) {
      System.out.println(attr1.toString());
    }
    analyzer.close();
  }


  /**
   * Nori 라이브러리의 KoreanAnalyzer의 분석결과를 출력한다. 
   * @throws Exception
   */
  @Test 
  public void testNori() throws Exception  {
    String source = "언어별로 단어의 변화는 모두  LOVE <html> <body></body></html>Java English 다르기 때문에 언어별로 Analyzer를 특화시켜서 사용해야 한다";
    Analyzer analyzer = new KoreanAnalyzer();
    TokenStream tokenStream = analyzer.tokenStream("sample", source);
    CharTermAttribute attr1 = tokenStream.addAttribute(CharTermAttribute.class);
    //PositionLengthAttribute attr3 = tokenStream.addAttribute(PositionLengthAttribute.class);
    tokenStream.reset();
    while (tokenStream.incrementToken()) {
      System.out.println(attr1.toString());
    }
    analyzer.close();
  }

}///~
