package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Tests for the succinct {@link Element} implementation.
 */
public class ElementTest extends AbstractNodeTest {

  protected Element element;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    element = element();
  }

  private Element element() {
    return element(document);
  }

  private Element element(Document document) {
    return (Element) getChildNode(document.getDocumentElement(), "book");
  }

  @Override
  protected Node node() {
    return element;
  }

  /*
   * Node API.
   */

  @Test
  public void testGetNodeName() {
    assertEquals("book", element.getNodeName());
  }

  @Test
  public void testGetNodeType() {
    assertEquals(Node.ELEMENT_NODE, element.getNodeType());
  }

  @Test
  public void testGetNodeValue() {
    assertNull(element.getNodeValue());
  }

  @Test
  public void testGetParentNode() {
    assertEquals("catalog", element.getParentNode().getNodeName());
  }

  @Test
  public void testGetFirstChild() {
    assertEquals(Node.TEXT_NODE, element.getFirstChild().getNodeType());
  }

  @Test
  public void testGetLastChild() {
    assertEquals(Node.TEXT_NODE, element.getLastChild().getNodeType());
  }

  @Test
  public void testGetChildNodes() {
    assertEquals(15, element.getChildNodes().getLength());
  }

  @Test
  public void testHasChildNodes() {
    assertTrue(element.hasChildNodes());
  }

  @Test
  public void testGetPreviousSibling() {
    assertEquals(Node.TEXT_NODE, element.getPreviousSibling().getNodeType());
  }

  @Test
  public void testGetNextSibling() {
    assertEquals(Node.TEXT_NODE, element.getNextSibling().getNodeType());
  }

  @Test
  public void testGetAttributes() {
    assertEquals(1, element.getAttributes().getLength());
  }

  @Test
  public void testHasAttributes() {
    assertTrue(element.hasAttributes());
  }

  @Test
  public void testGetOwnerDocument() {
    assertEquals(document, element.getOwnerDocument());
  }

  @Test
  public void testIsDefaultNamespace() {
    assertFalse(element.isDefaultNamespace(NS_CATALOG));
  }

  @Test
  public void testGetNamespaceURI() {
    assertNull(element.getNamespaceURI());
  }

  @Test
  public void testGetPrefix() {
    assertNull(element.getPrefix());
  }

  @Test
  public void testGetLocalName() {
    assertNull(element.getLocalName());
  }

  @Test
  public void testGetTextContent() {
    assertFalse(element.getTextContent().isEmpty());
  }

  @Test
  public void testSetTextContent() {
    try {
      element.setTextContent("text");
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testIsEqualNode() {
    assertTrue(element.isEqualNode(element()));
    assertFalse(element.isEqualNode(element.getParentNode()));
    assertFalse(element.isEqualNode(null));
  }

  @Test
  public void testIsSameNode() {
    assertTrue(element.isSameNode(element()));
    assertFalse(element.isSameNode(element.getParentNode()));
    assertFalse(element.isSameNode(null));
  }

  /*
   * Element API.
   */

  @Test
  public void testGetTagName() {
    assertEquals("book", element.getTagName());
  }

  @Test
  public void testGetAttribute() {
    assertEquals("101", element.getAttribute("id"));
    assertEquals("", element.getAttribute("lang"));
  }

  @Test
  public void testSetAttribute() {
    try {
      element.setAttribute("name", "value");
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testRemoveAttribute() {
    try {
      element.removeAttribute("name");
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testGetAttributeNode() {
    assertEquals("101", element.getAttributeNode("id").getNodeValue());
    assertNull(element.getAttributeNode("lang"));
  }

  @Test
  public void testSetAttributeNode() {
    try {
      element.setAttributeNode(element.getAttributeNode("id"));
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testRemoveAttributeNode() {
    try {
      element.removeAttributeNode(element.getAttributeNode("id"));
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testGetAttributeNS() {
    assertEquals("101", element.getAttributeNS(null, "id"));
    assertEquals("", element.getAttributeNS("http://example.com", "id"));
  }

  @Test
  public void testSetAttributeNS() {
    try {
      element.setAttributeNS("uri", "name", "value");
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testRemoveAttributeNS() {
    try {
      element.removeAttributeNS("uri", "name");
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testGetAttributeNodeNS() {
    assertEquals("101", element.getAttributeNodeNS(null, "id").getNodeValue());
    assertNull(element.getAttributeNodeNS("http://example.com", "id"));
  }

  @Test
  public void testSetAttributeNodeNS() {
    try {
      element.setAttributeNodeNS(element.getAttributeNode("id"));
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testHasAttribute() {
    assertTrue(element.hasAttribute("id"));
    assertFalse(element.hasAttribute("lang"));
  }

  @Test
  public void testHasAttributeNS() {
    assertTrue(element.hasAttributeNS(null, "id"));
    assertFalse(element.hasAttributeNS("http://example.com","id"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetSchemaTypeInfo() {
    element.getSchemaTypeInfo();
  }

  @Test
  public void testSetIdAttribute() {
    try {
      element.setIdAttribute("id", true);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testSetIdAttributeNS() {
    try {
      element.setIdAttributeNS(null, "id", true);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testSetIdAttributeNode() {
    try {
      element.setIdAttributeNode(element.getAttributeNode("id"), true);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  /*
   * Object API.
   */

  @Test
  public void testEquals() throws Exception {
    assertTrue(element.equals(element()));
    assertFalse(element.equals(element(build(getSuccinctFactory(), XML))));
    assertFalse(element.equals(element.getParentNode()));
    assertFalse(element.equals(new Object()));
    assertFalse(element.equals(null));
  }
}
