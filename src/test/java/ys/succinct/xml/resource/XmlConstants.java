package ys.succinct.xml.resource;

/**
 * XML constants for tests.
 */
public interface XmlConstants {

  static final String BASE_DIR = XmlConstants.class.getPackage().getName().replace('.', '/') + "/";

  static final String XML = BASE_DIR + "books.xml";
  static final String XML_DTD = BASE_DIR + "books-dtd.xml";
  static final String XML_NS = BASE_DIR + "books-ns.xml";
  static final String XML_PI = BASE_DIR + "books-pi.xml";
  static final String XML_XSD = BASE_DIR + "books-xsd.xml";

  static final String NS_CATALOG = "http://example.com/catalog";
  static final String NS_PRICE = "http://example.com/price";
}
