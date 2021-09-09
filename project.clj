;; CODE REVIEW - @paul - Please suggest anything else that should go here or anything that is here that's amiss.

(defproject xss-encoder-wrapper "0.1.0-SNAPSHOT"
  :description "Provide Clojure wrapper functions to the OWASP Java Encoder library's the important
               'Encode' static methods as a defense against Cross-Site Scripting (XSS)."

  :url "http://github.com/Giaranteed-Rate/xss-encoder-wrapper"

  :author "kevin.wall@rate.com"

  :repositories
  [["releases" {:url "s3p://polaris-maven/releases/" :no-auth true}
    "snapshots" {:url "s3p://polaris-maven/snapshots/" :no-auth true}]]

  :plugins [[s3-wagon-private "1.3.4"]]

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.owasp.encoder/encoder "1.2.3"]]

  :repl-options {:init-ns xss-encoder-wrapper.encoder}
  )
