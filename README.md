# xss-encoder-wrapper

A Clojure library designed to provide strong output encoding to act as an effective defense against
Cross-Site Scripting (XSS) vulnerabilities.

This library exposes certain static methods from
the [OWASP Java Encoder library's](https://owasp.org/www-project-java-encoder/) `Encode` class
by wrapping certain selected important static methods that are part of
[org.owasp.encoder.Encode](https://www.javadoc.io/static/org.owasp.encoder/encoder/1.2.3/index.html?org/owasp/encoder/Encode.html).


Developers unfamiliar with XSS defenses and who would like an overview of XSS prevention using
this library should first acquaint themselves with the lessons described in the
[OWASP Cross Site Scripting Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html)
and the
[DOM based XSS Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/DOM_based_XSS_Prevention_Cheat_Sheet.html).

## _Request to do code review_
**@Paul** - Please look at ALL the source code and accompanying documentation and not just the
committed files in this PR for the 'initial-code-review' branch. (If I remember, I'll at least add
a comment to each of the files so they show up in the PR, but I may forget one or two that
you think is relevant. Obviously, I will delete this section as well as comments added only
for code review purposes so you needn't address those.)

## Installation
Add the following dependency to your `project.clj`:

`[xss-encoder-wrapper "0.1.0"]`

* **Question**: Do you think the first release should be 0.1.0 or 1.0.0? I generally prefer the latter when except when we wish to convey an Alpha or Beta release. Note regardless, that I will remove the '-SNAPSHOT' before the first _official_ release.
* _**What else needs to go in this section???**_

## Important design decisions

If you have played with the OWASP Java Encoder library's
[Encode](https://www.javadoc.io/static/org.owasp.encoder/encoder/1.2.3/org/owasp/encoder/Encode.html)
class at all, you will notice if you pass in an input string that is `null` as input to one of its static
methods, that those static methods will return the `java.lang.String` `"null"`. While
this is certainly better than throwing a `java.lang.NullPointerException`, I felt it would be
non-intuitive to Clojure developers unfamiliar with these Java quirks, if calling (for example),

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`(for-html nil)`

resulted in the string`"null"`. So I instead chose to have it return the empty string `""`, which
just seemed to be more intuitive and less likely to unintentionally break applications. Hopefully this decision
precludes the need to check if some input value is `nil` before invoking functions in this library.

This applies to _**all**_ the functions in this `xss-encoder-wrapper` library. In addition, if
any of them are called with the _empty_ string, the empty string `""` is returned. However, strings
that _only_ consist of whitespace _may_ get output encoded for certain functions as required for certain
XSS defenses.

Lastly, in order to not overly complicate things, not all the static methods defined in the `Encode` class are exposed in this library, but
only those considered the most commonly used, single argument versions of those deemed most
essential to XSS defense.

## Intended use cases
The primary use of `xss-encoder-wrapper` is to provide a Clojure interface to the most important static methods from
the [OWASP Java Encoder](https://owasp.org/www-project-java-encoder/) library's `Encode` class in order to assist
Clojure programmers to prevent XSS vulnerabilities by providing contextual output encoding using the various
`encoder/for-xyz` functions, where _xyz_ is 'html', 'html-attribute', 'javascript', 'css-url', etc. When using these output
encoder functions, it is important that you use the function that is appropriate for the context of where
the output will be rendered. (See next section for further details.)

Note that you only need to output encode _untrusted_ data  (see section 'What data should be considered "untrusted"?',
below) that is going to be rendered in a browser. Data that is _only_
consumed by another API (e.g., another REST-based web service) needn't be output ended. While that data might be untrusted,
it is the responsibility of the last web service or web application in the chain before it gets rendered as HTML
that is responsible for output encoding that.

## Using contextual encoding
Context is important because there are different escaping requirements for these
different contexts and different characters that need encoded. Developers who are new to XSS or
to defending against XSS vulnerabilities are highly encouraged to first read the
[OWASP Cross Site Scripting Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html)
and the
[DOM based XSS Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/DOM_based_XSS_Prevention_Cheat_Sheet.html).

### General hints
* Use these output encoding functions as close to the rendering as possible so the context will
be known.
* Do **NOT** try to use these functions in some sort of HTTP interceptor / servlet filter, as that is bound to fail.
* Try hard to avoid situations where you need to use two different encoders on the same untrusted
  input as order is extremely important and these are extremely brittle edge cases. If you find yourself
  in such a situation, consider refactoring your code instead.
* You generally should always being calling these functions on quoted strings. It is essential for some, e.g., `encoder/for-css-string`.
  See documentation associated with each of the Clojure functions for additional details.

Note that some of these things are touched on later in more detail in the section "Some important final words".

### General examples
If the output appears in an JavaScript context, you should use 'for-javascript'.
(Note: this includes all DOM JavaScript event handler attributes (A/K/A, script attributes) such as 'onfocus',
'onclick', 'onload', etc.).

If the output is to be rendered in an HTML attribute context (except for the aforementioned 'on*event*' type
event handler attributes or places where the attribute name is interpreted as a URL as with 'href' and 'src'), you would use 'for-html-attribute' (although 'for-html' would also work even though it
is a tad slower).

If you are encoding anywhere a URL is expected (e.g., URL component originating from the user that will be used in a
redirect, a 'href' attribute for anchor tag or a 'src' attribute on a <img> tag, etc.), then you should
use 'for-uri-component', or if the URL is in a CSS context, 'for-css-url'.

If encoding general CSS blocks, then use 'for-css-string'.

## What data should be considered "untrusted"?
So, what data should we consider "untrusted" in the context of your application? The correct
answer to that depends on your answer to the question "What is your threat model?". A proper
threat model will identify the trust boundaries of your application and anything coming from
_outside_ those trust boundaries is considered "untrusted".

However, unfortunately, most  projects haven't developed threat models, so the answer to
"what data is untrusted"? essentially boils down to _any data that originates or passes through your anything that is outside
the _direct_ control of your application must be considered untrusted_.

Of course, that is not completely practical  to treat everything as untrusted. In the context of
XSS defense though, we generally assume anything that is flows through or is created outside your
code base, is untrusted. The obvious example is "data originating from a customer", but that
should not be where you stop. So let's examine at some common examples that might make for a pragmatic
starting point if you don't have a threat model in hand:

| Data Source    | Trusted?     | Notes        |
| :------------- | :----------: | :----------- |
| External customers | No | This should be obvious. Never trust customer data, even if the user is authenticated and there is proof of identity. |
| Internal users | No! | The world is filled with disgruntled employees and contractors. |
| Application source code | Yes  | While there can be intentionally malicious code, but we have to start our assumptions somewhere given the lack of a threat model.    |
| Application config files   | Yes | Assumes restrictive file permissions, trusted DevOps and admins and secure infrastructure, but sure--as long as you haven't left restricted data such as service passwords and encryption keys in these. |
| Infrastructure tool chain   | Yes | Examples would be environment variables, Java system properties, or start-up command line arguments. (On the plus side, the least of your worries at that point will be XSS.)|
| Application Database   | No! | We might make exceptions if no other application had insert / update access to your DB and we implicitly trust all those with DBA privileges or data that your application was encrypted using an Authenticated Encryption mode (e.g., GCM, CCM, OCB) or digitally signed. |
| An external web service | No | Since it is not part of your infrastructure, then "no". An example would be the Google Maps API, although if your SOW / contract transfers liability for breaches resulting from the use of said API to the external company, then this _might_ be okay. |
| An internal web service | No! | Do you know if the data from that other internal web service is as security conscious as yours? Probably not, right?|
| Logs produced by the application that will be consumed in a browser | No! | Generally data entered into logs is not pre-scrubbed, but usually output encoding to prevent XSS in this case would be the responsibility of the SIEM, but an exemption would be if there is some sort of trouble-shooting log viewer built into you application. |

If the entries marked 'No!' did not surprise you, congratulations! You must be a security conscious developer and
a candidate for an application security champion!

## Some important final words

* **Where to output encode for HTML rendering:** Knowing _where_ to place the output encoding in your code is just as
important as knowing which context (HTML, HTML attribute, CSS, JavaScript, or URL) to use for the output encoding
and surprisingly the two are often related. In general, output encoding should be done just prior to the output
being rendered (that is, as close to the 'sink' as possible) because that is what determines what the appropriate
context is for the output encoding. In fact, doing output encoding on untrusted data that is stored and to be
used later--whether stored in an HTTP session or in a database--is almost always considered an anti-pattern.
An example of this is one gathers and stores some untrusted data item such as an email address from a user. A
developer thinks "let's output encode this and store the encoded data in the database, thus making the
untrusted data safe to use all the time, thus saving all of us developers all the encoding troubles later on".
On the surface, that sounds like a reasonable approach. The problem is how to know what output encoding to use,
not only for now, but for all possible future uses? It might be that the current application code base is only
using it in an HTML context that is displayed in an HTML report or shown in an HTML context in the user's profile.
But what if it is later used in a `mailto:` URL? Then instead of HTML encoding, it would need to have URL encoding.
Similarly, what if there is a later switch made to use AJAX and the untrusted email address gets used in a JavaScript
context? The complication is that even if you know with certainty today all the ways that an untrusted data item is
used in your application, it is generally impossible to predict all the contexts that it may be used in the future,
not only in your application, but in other applications that could access that data in the database.


* **Avoiding multiple _nested_ contexts:** A really tricky situation to get correct is when there are
multiple nested encoding contexts. But far, the most common place this seems to come up is untrusted
URLs used in JavaScript. How should you handle that? Well, the best way is to rewrite your code to avoid it!
An example of this that is well worth reading may be found at ESAPI-DEV mailing list archives: [URL encoding
within JavaScript](https://lists.owasp.org/pipermail/esapi-dev/2012-March/002090). Be sure to read the entire thread.
The question itself is too nuanced to be answered in a README file, but now, hopefully you are at least
aware of the potential pitfalls. There is little available research or examples
on how to do output encoding when multiple mixed encodings are required, although one that you may find useful is
[Automated Detecting and Repair of Cross-SiteScripting Vulnerabilities through Unit Testing](https://arxiv.org/pdf/1804.01862.pdf).
It at least discusses a few of the common errors involved in multiple mixed encoding contexts.


* **A word about unit testing:** Unit testing XSS defense is hard. You may be satisfied with stopped after you have
tested against the ubiquitous XSS test case of`</script>alert(1)</script>` or similar simplistic XSS attack payloads
and check if that is properly encoded in a unit test (or, you don't see an alert box popped in your browser), then
you consider it "problem fixed", and consider that testing sufficient. Unfortunately,
that minimalist testing may not always detect places where you used the wrong output encoder. We need to do
better. Fortunately, the aforementioned
[Automated Detecting and Repair of Cross-SiteScripting Vulnerabilities through Unit Testing](https://arxiv.org/pdf/1804.01862.pdf)
provides some insight on this. If you are really ambitious, an excellent resource for
XSS attack patterns is [BeEF - The Browser Exploitation Framework Project](https://beefproject.com/).

## Detailed example usage
See the test cases under test/encoder-test.clj

**TODO**: I will try to develop some additional more realistic examples as I learn more Clojure and Ring / Compojure.

## Questions
Send questions about this library to the author, [kevin.wall@rate.com](mailto:kevin.wall@rate.com), or
questions about XSS in general to the GRI Application Security team at [AppSec@rate.com](mailto:AppSec@rate.com).

## License

Copyright © 2021 Guaranteed Rate, Inc. -- All Rights Reserved

This program and the accompanying materials proprietary information of Guaranteed Rate, Inc.
