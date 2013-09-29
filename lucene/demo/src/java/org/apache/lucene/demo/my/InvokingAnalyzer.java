package org.apache.lucene.demo.my;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
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

public class InvokingAnalyzer {
  public static void main(String[] args) throws IOException {
    Version matchVersion = Version.LUCENE_CURRENT; // Substitute desired Lucene version for XY
    Analyzer analyzer = new StandardAnalyzer(matchVersion); // or any other analyzer
    TokenStream ts = analyzer.tokenStream("myfield", new StringReader("some text goes here"));
    OffsetAttribute offsetAtt = ts.addAttribute(OffsetAttribute.class);
    
    try {
      ts.reset(); // Resets this stream to the beginning. (Required)
      while (ts.incrementToken()) {
        // Use AttributeSource.reflectAsString(boolean)
        // for token stream debugging.
        System.out.println("token: " + ts.reflectAsString(true));

        System.out.println("token start offset: " + offsetAtt.startOffset());
        System.out.println("  token end offset: " + offsetAtt.endOffset());
      }
      ts.end();   // Perform end-of-stream operations, e.g. set the final offset.
    } finally {
      ts.close(); // Release resources associated with this stream.
    }
  }
}
