(ns xss-encoder-wrapper.encoder
  "The primary use of `xss-encoder-wrapper` is to provide a Clojure interface to the most important static
  methods from the OWASP Java Encoder library's 'Encode' class in order to assist preventing XSS vulnerabilities
  by providing contextual output encoding using the various 'encoder' functions. When using these 'encoder' functions,
  it is important that you use the one for the appropriate context where the output will be rendered.

  For an overview of XSS prevention, the users of this library should first acquaint themselves with the lessons
  described in https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html
  and https://cheatsheetseries.owasp.org/cheatsheets/DOM_based_XSS_Prevention_Cheat_Sheet.html.

  See https://github.com/Guaranteed-Rate/xss-encoder-wrapper#readme for additional details."
  (:import [org.owasp.encoder Encode]))

(defn for-html
  "Encodes for (X)HTML text content and text attributes. It is NOT suitable for script attributes, such as
  'onclick', 'onload', etc. (Use 'for-javascript' for that.) If used with HTML text attributes, the attributes
  themselves MUST be quoted.

  See https://www.javadoc.io/static/org.owasp.encoder/encoder/1.2.3/org/owasp/encoder/Encode.html#forHtml-java.lang.String-
  for further details."
  [unstrusted-string]
  (if (empty? unstrusted-string)
    ""
    (Encode/forHtml unstrusted-string)))

(defn for-css-string
  "Encodes for CSS strings. The context MUST be surrounded by quotation characters (either single or double quotes).
  It is safe for use in both style blocks and attributes in HTML.

  See https://www.javadoc.io/static/org.owasp.encoder/encoder/1.2.3/org/owasp/encoder/Encode.html#forCssString-java.lang.String-
  for further details."
  [unstrusted-string]
  (if (empty? unstrusted-string)
    ""
    (Encode/forCssString unstrusted-string)))

(defn for-css-url
  "Encodes for CSS URL contexts. The context must be surrounded by \"url(\" and \")\".
  It is safe for use in both style blocks and attributes in HTML.

  The CSS URL string value itself MUST be quoted with single or double quotes for this to be safe.

  Note: this does not do any checking on the quality or safety of the URL itself. The caller should ensure that the
  URL is safe for embedding (e.g. input validation) by other means.

  See https://www.javadoc.io/static/org.owasp.encoder/encoder/1.2.3/org/owasp/encoder/Encode.html#forCssUrl-java.lang.String-
  for further details."
  [unstrusted-string]
  (if (empty? unstrusted-string)
    ""
    (Encode/forCssUrl unstrusted-string)))


;; here

(defn for-html-attribute
  "Output encode for an HTML text attributes, but is NOT suitable for script attributes or attributes interpreted
  as URLs (e.g., 'href', 'src') by the browser. (You should use 'for-javascript' and 'for-uri-component' for
  those two cases respectively.)

  The HTML text attribute value itself MUST be quoted with single or double quotes. (For encoding unquoted HTML text
  attributes, use 'for-html-unquoted-attribute' instead, although for compliance with XHTML specs, attributes should
  always be quoted.)

  If all you are output encoding is an HTML text attribute value, this function is slightly more efficient
  than 'for-html'.

  See https://www.javadoc.io/static/org.owasp.encoder/encoder/1.2.3/org/owasp/encoder/Encode.html#forHtmlAttribute-java.lang.String-
  for further details.
  "
  [unstrusted-string]
  (if (empty? unstrusted-string)
    ""
    (Encode/forHtmlAttribute unstrusted-string)))

(defn for-html-unquoted-attribute
  "Output encode for un unquoted HTML text attributes. This is NOT suitable for script attributes. (Use 'for-javascript' for that
  purpose.)

  Note however that quoting attributes and using 'for-html-attributes' should generally be preferred to make the markup
  XHTML compliant.
  , attributes should

  See https://www.javadoc.io/static/org.owasp.encoder/encoder/1.2.3/org/owasp/encoder/Encode.html#forHtmlUnquotedAttribute-java.lang.String-
  for further details.
  "
  [unstrusted-string]
  (if (empty? unstrusted-string)
    ""
    (Encode/forHtmlUnquotedAttribute unstrusted-string)))

(defn for-javascript
  "Output encode for an JavaScript string. It is safe for use in HTML script attributes
  (such as onclick, onload, etc.), script blocks, JSON files, and JavaScript source.

  The caller MUST provide the surrounding quotation characters for the string.

  See https://www.javadoc.io/static/org.owasp.encoder/encoder/1.2.3/org/owasp/encoder/Encode.html#forJavaScript-java.lang.String-
  for further details."
  [unstrusted-string]
  (if (empty? unstrusted-string)
    ""
    (Encode/forJavaScript unstrusted-string)))

(defn for-uri-component
  "Performs percent-encoding for a component of a URI, such as a query parameter name or value, path,
  or query-string. In particular this method insures that special characters in the component do not get
  interpreted as part of another component. Besides the obvious URI components mentioned as part of '<a>' tag for the
  'href' attribute, you would want to use this for other places where URI components can appear such as the 'src'
  attribute of many other HTML tags such as '<img>'.


  See https://www.javadoc.io/static/org.owasp.encoder/encoder/1.2.3/org/owasp/encoder/Encode.html#forUriComponent-java.lang.String-
  for further details."
  [unstrusted-string]
  (if (empty? unstrusted-string)
    ""
    (Encode/forUriComponent unstrusted-string)))

(defn for-xml
  "Output encode for an XML context.

  See https://www.javadoc.io/static/org.owasp.encoder/encoder/1.2.3/org/owasp/encoder/Encode.html#forXml-java.lang.String-
  for further details."
  [unstrusted-string]
  (if (empty? unstrusted-string)
    ""
    (Encode/forXml unstrusted-string)))






