package org.apache.lucene.demo.my;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class FieldSectionTest {
  
  private static final String INDEX_PATH = "F:\\apache\\solr-4.3.0-src\\solr-4.3.0\\test-index";
  
  public static void main(String[] args) throws IOException, ParseException {
    //write();
    query("starts ends starts");
  }
  
  private static void query(String s) throws IOException, ParseException {
    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(INDEX_PATH)));
    IndexSearcher searcher = new IndexSearcher(reader);
    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
    QueryParser parser = new QueryParser(Version.LUCENE_40, "f", analyzer);
    Query query = parser.parse(s);
    TopDocs result = searcher.search(query, null, 100);
    
    System.out.println("Total hits : " + result.totalHits);
    for(ScoreDoc scoreDoc : result.scoreDocs) {
      Document hitDoc = searcher.doc(scoreDoc.doc);
      System.out.println("doc[" + scoreDoc.doc + "] is " + hitDoc.get("f"));
    }
  }
  
  private static void write() throws IOException {
    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
    IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);
    iwc.setOpenMode(OpenMode.CREATE);
    Directory dir = FSDirectory.open(new File(INDEX_PATH));
    IndexWriter writer = new IndexWriter(dir, iwc);
    
    Document document = new Document();
    
    document.add(new Field("f","first ends", TextField.TYPE_STORED));
    document.add(new Field("f","starts two", TextField.TYPE_STORED));
    writer.addDocument(document);
    writer.close();
  }
}
