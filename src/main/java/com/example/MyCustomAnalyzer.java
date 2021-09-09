package com.example;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.CapitalizationFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;


/**
 * 커스텀 애널라이저 샘플. 
 */
public class MyCustomAnalyzer extends Analyzer  {
  @Override
  protected TokenStreamComponents createComponents(String fieldName) {
      CharArraySet stopWords = StopFilter.makeStopSet("is", "com", "lucene");
      StandardTokenizer src = new StandardTokenizer();
      //TokenStream result = new StandardFilter(src); // StandardFilter deprecated
      TokenStream result = new LowerCaseFilter(src);
      result = new StopFilter(result,  stopWords);
      result = new PorterStemFilter(result);
      result = new CapitalizationFilter(result);
      return new TokenStreamComponents(src, result);
  }
}


