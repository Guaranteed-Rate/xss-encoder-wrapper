(ns xss-encoder-wrapper.encoder-test
  (:require [clojure.test :refer :all]
            [xss-encoder-wrapper.encoder :refer :all]))

;;
;;  These are pretty much minimal tests for sanity testing, which hopefully is all that is needed
;;  as the OWASP Java Encoder library has its own set of extensive unit tests as part of its code
;;  repo in GitHub.
;;



(deftest for-html-test
  (testing "for-html ..."
    (testing "calls that return empty string"
      (testing "for-html with nil and empty string..."
        (is (= (for-html "") ""))
        (is (= (for-html nil) ""))))
    (testing "Base caseL As per the Javadoc's 'Encoding Table' for Encode.forHtml(String)..."
      (is (= (for-html "&<>\"'") "&amp;&lt;&gt;&#34;&#39;")))
    (testing "non-trivial tests that do actual output encoding"
      (is (= (for-html "test \"hello\"<script>alert(1)</script> more. What's expected?")
             "test &#34;hello&#34;&lt;script&gt;alert(1)&lt;/script&gt; more. What&#39;s expected?"))
      (is (= (for-html "&lt;script&gt;") "&amp;lt;script&amp;gt;")))))

;; Encoding notes from
;;    https://www.javadoc.io/static/org.owasp.encoder/encoder/1.2.3/org/owasp/encoder/Encode.html#forCssString-java.lang.String-
;; * The following characters are encoded using hexadecimal encodings: U+0000 - U+001f, ", ', \, <, &, /, >, U+007f,
;;   line separator (U+2028), paragraph separator (U+2029).
; * Any character requiring encoding is encoded as \xxx where xxx is the shortest hexadecimal representation of its
;   Unicode code point (after decoding surrogate pairs if necessary). This encoding is never zero padded. Thus, for
;   example, the tab character is encoded as \9, not \0009.

(deftest for-css-string-test
  (testing "for-css-string ..."
    (testing "calls that return empty string"
      (testing "for-html with nil and empty string..."
        (is (= (for-html "") ""))
        (is (= (for-html nil) ""))))
    (testing "Other tests..."
      (is (= (for-css-string "<script/>\u0009\\ FOO&BAR\t\r\n'\"\u0012!@$%()=+{}[]")
             "\\3cscript\\2f\\3e \\9\\5c  FOO\\26 BAR\\9 \\d \\a\\27\\22\\12!@$%()=+{}[]")))))

(deftest for-css-url-test
  (testing "for-css-url ..."
    (testing "calls that return empty string"
      (testing "for-css-url with nil and empty string..."
        (is (= (for-html "") ""))
        (is (= (for-html nil) ""))))
    (testing "Other tests..."
      (is (= (for-css-url "background: url(http://example.com/dark-mode.jpg);")
             "background:\\20url\\28http:\\2f\\2f example.com\\2f dark-mode.jpg\\29;")))))

(deftest for-html-attribute-test
  (testing "for-html-attribute ..."
    (testing "calls that return empty string"
      (testing "for-html-attribute with nil and empty string..."
        (is (= (for-html "") ""))
        (is (= (for-html nil) ""))))
    (testing "Other tests..."
      (is (= (for-html-attribute "double-quote: \", single-quote: ' & smil\u00e9 \ud83d\ude00 <script-kiddies>")
             "double-quote: &#34;, single-quote: &#39; &amp; smilé 😀 &lt;script-kiddies>")))))

(deftest for-html-unquoted-attribute-test
  (testing "for-html-unquoted-attribute ..."
    (testing "calls that return empty string"
      (testing "for-html-unquoted-attribute with nil and empty string..."
        (is (= (for-html "") ""))
        (is (= (for-html nil) ""))))
    (testing "Other tests..."
      (is (= (for-html-unquoted-attribute " ") "&#32;"))
      (is (= (for-html-unquoted-attribute "double-quote: \", single-quote: ' & smil\u00e9 \ud83d\ude00 <script-kiddies>")
             "double-quote:&#32;&#34;,&#32;single-quote:&#32;&#39;&#32;&amp;&#32;smilé&#32;😀&#32;&lt;script-kiddies&gt;")))))

(deftest for-javascript-test
  (testing "for-javascript ..."
    (testing "calls that return empty string"
      (testing "for-javascript with nil and empty string..."
        (is (= (for-html "") ""))
        (is (= (for-html nil) ""))))
    (testing "Other tests..."
      (is (= (for-javascript "\b\t\n\f\r\"&'/\\abc123") "\\b\\t\\n\\f\\r\\x22\\x26\\x27\\/\\\\abc123")))))

(deftest for-uri-component-test
  (testing "for-uri-component ..."
    (testing "calls that return empty string"
      (testing "for-uri-component with nil and empty string..."
        (is (= (for-html "") ""))
        (is (= (for-html nil) ""))))
    (testing "Other tests..."
      (is (= (for-uri-component " ") "%20"))
      (is (= (for-uri-component "@") "%40"))                ;; @ *is* encoded despite what the Encode.forUriComponent(String) Javadoc says.
      (is (= (for-uri-component "!#$&'()*+,/:;=?@[]")
             "%21%23%24%26%27%28%29%2A%2B%2C%2F%3A%3B%3D%3F%40%5B%5D")))))

;; Using same tast as 'for-html' since Javadoc for Encode.forXml(String) refers to Encode.forHtml(String)
(deftest for-xml-test
  (testing "for-xml ..."
    (testing "calls that return empty string"
      (testing "for-xml with nil and empty string..."
        (is (= (for-html "") ""))
        (is (= (for-html nil) ""))))
    (testing "Other tests..."
      (is (= (for-html "&<>\"'") "&amp;&lt;&gt;&#34;&#39;")))))