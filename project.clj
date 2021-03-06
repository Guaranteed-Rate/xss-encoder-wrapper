(defproject com.guaranteedrate/xss-encoder-wrapper "1.0.0"
  :description "Provide Clojure wrapper functions to the OWASP Java Encoder library's the important
               'Encode' static methods as a defense against Cross-Site Scripting (XSS) and accompanying
               extensive general documentation related to XSS defense."

  :url "http://github.com/Guaranteed-Rate/xss-encoder-wrapper"

  :license
  {:name "The 3-Clause BSD License"
   :url "https://opensource.org/licenses/BSD-3-Clause"}

  :author "kevin.wall@rate.com"

  :pom-addition
    [:developers
     [:developer
      [:name "Kevin Wall"]
      [:url "https://github.com/kwwall-gri"]
      [:email "kevin.wall@rate.com"]
      [:timezone "-5"]]]

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.owasp.encoder/encoder "1.2.3"]]

  :deploy-repositories [["releases" {:url "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                                   :creds :gpg}
                       "snapshots" {:url "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                                    :creds :gpg}]]

  :repl-options {:init-ns xss-encoder-wrapper.encoder})
