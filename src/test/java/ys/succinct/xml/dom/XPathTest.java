package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ys.succinct.xml.XmlBaseTest;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Tests for XPath evaluation on the succinct DOM.
 */
public class XPathTest extends XmlBaseTest {

  private static XPathFactory xpathFactory;

  @BeforeClass
  public static void setUp() {
    xpathFactory = XPathFactory.newInstance();
  }

  @Test
  public void testNode() throws Exception {
    assertTrue(getNode(".") instanceof Document);
    assertEquals("title", getNode("/catalog/book/title").getNodeName());
    assertEquals("XML Developer's Guide", getNode("/catalog/book/title/text()").getNodeValue());
    assertEquals("price", getNode("//price").getNodeName());
    assertNull(getNode("price"));
    assertEquals(" Simple XML with the elements, attributes and comments. ",
        getNode("//comment()").getNodeValue());
  }

  @Test
  public void testNodeList() throws Exception {
    assertEquals(12, getNodeList("/catalog/book").getLength());
    assertEquals(1, getNodeList("/catalog/book[@id='107']").getLength());
  }

  @Test
  public void testAttributes() throws Exception {
    assertEquals(Node.ATTRIBUTE_NODE, getNode("/catalog/book/@id").getNodeType());
    assertEquals(12, getNodeList("/catalog/book[@id]").getLength());
    assertEquals("book", getNode("/catalog/book[@id='101']").getNodeName());
  }

  private static Node getNode(String expression) throws Exception {
    return (Node) xpathFactory.newXPath().evaluate(expression, getDom(), XPathConstants.NODE);
  }

  private static NodeList getNodeList(String expression) throws Exception {
    return (NodeList) xpathFactory.newXPath().evaluate(expression, getDom(), XPathConstants.NODESET);
  }

  private static Document getDom() throws Exception {
    return build(getSuccinctFactory(), XML);
  }
}
