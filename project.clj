(defproject com.guaranteedrate/xss-encoder-wrapper "1.0.0"
  :description "Provide Clojure wrapper functions to the OWASP Java Encoder library's the important
               'Encode' static methods as a defense against Cross-Site Scripting (XSS) and accompanying
               extensive general documentation related to XSS defense."

  :url "http://github.com/Guaranteed-Rate/xss-encoder-wrapper"

  :license
  {:name "The MIT License"
   :url "https://mit-license.org/"}

  :author "kevin.wall@rate.com"

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.owasp.encoder/encoder "1.2.3"]]

  :deploy-repositories
  [["releases" :clojars]
   ["snapshots" :clojars]]

  :repl-options {:init-ns com.guaranteedrate/xss-encoder-wrapper.encoder})
