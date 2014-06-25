Summary
-----------
The library provides succinct implementation of DOM API according to O'Neil Delpratt's thesis
"Space Efficient In-Memory Representation of XML Documents" (2008). The succinct DOM is based on the static succinct data structures (such as bit vectors, balanced parentheses). Therefore, mutations are not allowed i.e. the DOM is read-only.

The DOM implementation supports XML documents that consist of up to 1073741823 nodes. Practically, this is enough to handle the XML documents whose size is measured in gigabytes.
Requirements
------------
The library requires:

 - Java 1.7 and higher
 - [succinct-util](https://github.com/yshahun/succinct-util) library whose succinct data structures are used to represent the succinct DOM

Documentation
-------------
API documentation is available [here](http://yshahun.github.io/succinct-dom/apidocs/index.html).
Installation
------------
Download and build the succinct-util:

`git clone https://github.com/yshahun/succinct-util.git
mvn clean install`

Download and build the succinct-dom:

`git clone https://github.com/yshahun/succinct-dom.git
mvn clean install`
Getting Started
---------------
Get an instance of the succinct DocumentBuilderFactory:
```java
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(
    SuccinctDocumentBuilderFactory.class.getName(), YourClass.class.getClassLoader());
```
Parse the XML and use the Document as usual:
```java
Document document = factory.newDocumentBuilder().parse("data.xml");
...
```
License
-------
Licensed under the Apache License 2.0.
References
----------
- Delpratt, O'Neil. Space efficient in-memory representation of XML documents, University of Leicester, 2008.
- Delpratt, O., Raman, R., Rahman, N. Engineering Succinct DOM, University of Leicester, 2008.