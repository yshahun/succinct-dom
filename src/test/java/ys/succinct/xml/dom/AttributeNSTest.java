package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Tests for the succinct {@link Attr} implementation with the namespace support.
 */
public class AttributeNSTest extends AttributeTest {

  private Attr attributeNS;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    Node book = getChildNode(document.getDocumentElement(), "book");
    Element description = (Element) getChildNode(book, "description");
    attributeNS = description.getAttributeNode("xml:lang");
  }

  @Override
  protected DocumentBuilderFactory getFactory() {
    return withNamespaces(super.getFactory());
  }

  @Override
  protected String getXml() {
    return XML_NS;
  }

  /*
   * Node API.
   */

  @Override
  @Test
  public void testGetNamespaceURI() {
    assertNull(attribute.getNamespaceURI());
    assertEquals(XMLConstants.XML_NS_URI, attributeNS.getNamespaceURI());
  }

  @Override
  @Test
  public void testGetPrefix() {
    assertNull(attribute.getPrefix());
    assertEquals("xml", attributeNS.getPrefix());
  }

  @Override
  @Test
  public void testGetLocalName() {
    assertEquals("id", attribute.getLocalName());
    assertEquals("lang", attributeNS.getLocalName());
  }

  @Override
  @Test
  public void testIsDefaultNamespace() {
    assertTrue(attribute.isDefaultNamespace(NS_CATALOG));
    assertFalse(attribute.isDefaultNamespace(XMLConstants.XML_NS_URI));
    assertTrue(attributeNS.isDefaultNamespace(NS_CATALOG));
    assertFalse(attributeNS.isDefaultNamespace(XMLConstants.XML_NS_URI));
  }
}
