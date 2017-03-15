cobiss-parser
===========
A Java library to parse COBISS files. The project follows the [Semantic Versioning Convention](http://semver.org/) and uses Apache 2.0 license.

[![Gitter Chat](http://img.shields.io/badge/chat-online-brightgreen.svg)](https://gitter.im/pdf-converter/)
[![Build Status](https://travis-ci.org/jmrozanec/cobiss-parser.png?branch=master)](https://travis-ci.org/jmrozanec/cobiss-parser)

**Download**

cobiss-parser is available on [Maven central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.bit-scout%22) repository.

    <dependency>
        <groupId>com.bit-scout</groupId>
        <artifactId>cobiss-parser</artifactId>
        <version>0.1.0-SNAPSHOT</version>
    </dependency>

**Features**
Allows to parse COBISS files and turn them into objects.
 
 
**Example**
 
 ```java
 
    List<DublinCore> metadata = CobissParser.parse(new File("sample-cobiss-file.txt"));
 ```

**Contribute & Support!**

Contributions are welcome! You can contribute by
 * starring and/or Flattring this repo!
 * requesting or adding new features.
 * enhancing existing code: ex.: provide more accurate description cases
 * testing
 * enhancing documentation
 * providing translations to support new locales
 * bringing suggestions and reporting bugs
 * spreading the word / telling us how you use it!

Support us donating once or by subscription through Flattr!

[![Flattr this!](https://api.flattr.com/button/flattr-badge-large.png)](https://flattr.com/submit/auto?user_id=jmrozanec&url=https://github.com/jmrozanec/cobiss-parser)
