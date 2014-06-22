package ys.succinct.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ys.succinct.xml.parser.SuccinctDocumentBuilderFactory;
import ys.succinct.xml.resource.XmlConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Base class for testing DOM and XML handling.
 */
public abstract class XmlBaseTest implements XmlConstants {

  /*
   * Utility methods to work with resources.
   */

  public static InputSource getResourceAsURL(String path) {
    URL url = XmlBaseTest.class.getClassLoader().getResource(path);
    if (url == null) {
      throw new IllegalArgumentException(String.format("Resource [%s] isn't found.", path));
    }
    return new InputSource(url.toString());
  }

  public static InputSource getResourceAsByteStream(String path) {
    InputStream stream = XmlBaseTest.class.getClassLoader().getResourceAsStream(path);
    if (stream == null) {
      throw new IllegalArgumentException(String.format("Resource [%s] isn't found.", path));
    }
    URL url = XmlBaseTest.class.getClassLoader().getResource(path);
    InputSource is = new InputSource(stream);
    is.setSystemId(url.toString());
    return is;
  }

  public static InputSource getResourceAsCharacterStream(String path) {
    InputStream stream = XmlBaseTest.class.getClassLoader().getResourceAsStream(path);
    if (stream == null) {
      throw new IllegalArgumentException(String.format("Resource [%s] isn't found.", path));
    }
    URL url = XmlBaseTest.class.getClassLoader().getResource(path);
    InputSource is = new InputSource(new InputStreamReader(stream));
    is.setSystemId(url.toString());
    return is;
  }

  /*
   * Utility methods to work with XML and DOM.
   */

  public static DocumentBuilderFactory getDefaultFactory() {
    return DocumentBuilderFactory.newInstance();
  }

  public static DocumentBuilderFactory getSuccinctFactory() {
    return DocumentBuilderFactory.newInstance(
        SuccinctDocumentBuilderFactory.class.getName(), XmlBaseTest.class.getClassLoader());
  }

  public static DocumentBuilderFactory withNamespaces(DocumentBuilderFactory factory) {
    factory.setNamespaceAware(true);
    return factory;
  }

  public static Document build(DocumentBuilderFactory factory, String xmlFile)
      throws SAXException, IOException, ParserConfigurationException {
    return factory.newDocumentBuilder().parse(getResourceAsURL(xmlFile));
  }

  public static Node getChildNode(Node node, String targetName) {
    for (Node x = node.getFirstChild(); x != null; x = x.getNextSibling()) {
      String name = x.getLocalName();
      if (name == null) {
        name = x.getNodeName();
      }
      if (name.equals(targetName)) {
        return x;
      }
    }
    return null;
  }
}
