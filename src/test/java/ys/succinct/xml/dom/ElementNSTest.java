package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Tests for the succinct {@link Element} implementation with the namespace support.
 */
public class ElementNSTest extends ElementTest {

  private Element elementNS;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    elementNS = (Element) getChildNode(element, "price");
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
    assertEquals(NS_CATALOG, element.getNamespaceURI());
    assertEquals(NS_PRICE, elementNS.getNamespaceURI());
  }

  @Override
  @Test
  public void testGetPrefix() {
    assertNull(element.getPrefix());
    assertEquals("p", elementNS.getPrefix());
  }

  @Override
  @Test
  public void testGetLocalName() {
    assertEquals("book", element.getLocalName());
    assertEquals("price", elementNS.getLocalName());
  }

  @Override
  @Test
  public void testIsDefaultNamespace() {
    assertTrue(element.isDefaultNamespace(NS_CATALOG));
    assertFalse(element.isDefaultNamespace(XMLConstants.XML_NS_URI));
    assertTrue(elementNS.isDefaultNamespace(NS_CATALOG));
    assertFalse(elementNS.isDefaultNamespace(XMLConstants.XML_NS_URI));
  }
}
