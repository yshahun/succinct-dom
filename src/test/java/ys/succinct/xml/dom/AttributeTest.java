package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Tests for the succinct {@link Attr} implementation.
 */
public class AttributeTest extends AbstractNodeTest {

  protected Attr attribute;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    attribute = attribute();
  }

  private Element element() {
    return (Element) getChildNode(document.getDocumentElement(), "book");
  }

  private Attr attribute() {
    return element().getAttributeNode("id");
  }

  @Override
  protected Node node() {
    return attribute;
  }

  /*
   * Node API.
   */

  @Test
  public void testGetNodeName() {
    assertEquals("id", attribute.getNodeName());
  }

  @Test
  public void testGetNodeType() {
    assertEquals(Node.ATTRIBUTE_NODE, attribute.getNodeType());
  }

  @Test
  public void testGetNodeValue() {
    assertEquals("101", attribute.getNodeValue());
  }

  @Test
  public void testSetNodeValue() {
    try {
      attribute.setNodeValue("101");
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testGetParentNode() {
    assertNull(attribute.getParentNode());
  }

  @Test
  public void testGetChildNodes() {
    assertEquals(1, attribute.getChildNodes().getLength());
  }

  @Test
  public void testGetFirstChild() {
    assertEquals(Node.TEXT_NODE, attribute.getFirstChild().getNodeType());
    assertEquals("101", attribute.getFirstChild().getNodeValue());
  }

  @Test
  public void testGetLastChild() {
    assertEquals(Node.TEXT_NODE, attribute.getFirstChild().getNodeType());
    assertEquals("101", attribute.getFirstChild().getNodeValue());
  }

  @Test
  public void testHasChildNodes() {
    assertTrue(attribute.hasChildNodes());
  }

  @Test
  public void testGetPreviousSibling() {
    assertNull(attribute.getPreviousSibling());
  }

  @Test
  public void testGetNextSibling() {
    assertNull(attribute.getNextSibling());
  }

  @Test
  public void testGetAttributes() {
    assertNull(attribute.getAttributes());
  }

  @Test
  public void testHasAttributes() {
    assertFalse(attribute.hasAttributes());
  }

  @Test
  public void testGetOwnerDocument() {
    assertEquals(document, attribute.getOwnerDocument());
  }

  @Test
  public void testIsDefaultNamespace() {
    assertFalse(document.isDefaultNamespace(NS_CATALOG));
  }

  @Test
  public void testGetNamespaceURI() {
    assertNull(attribute.getNamespaceURI());
  }

  @Test
  public void testGetPrefix() {
    assertNull(attribute.getPrefix());
  }

  @Test
  public void testGetLocalName() {
    assertNull(attribute.getLocalName());
  }

  @Test
  public void testGetTextContent() {
    assertEquals("101", attribute.getTextContent());
  }

  @Test
  public void testSetTextContent() {
    try {
      attribute.setTextContent("101");
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testIsEqualNode() {
    assertTrue(attribute.isEqualNode(attribute()));
    assertFalse(attribute.isEqualNode(attribute.getOwnerElement()));
    assertFalse(attribute.isEqualNode(null));
  }

  @Test
  public void testIsSameNode() {
    assertTrue(attribute.isSameNode(attribute()));
    assertFalse(attribute.isSameNode(attribute.getOwnerElement()));
    assertFalse(attribute.isSameNode(null));
  }

  /*
   * Attr API.
   */

  @Test
  public void testGetName() {
    assertEquals("id", attribute.getName());
  }

  @Test
  public void testGetSpecified() {
    assertTrue(attribute.getSpecified());
  }

  @Test
  public void testGetValue() {
    assertEquals("101", attribute.getValue());
  }

  @Test
  public void testSetValue() {
    try {
      attribute.setValue("101");
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testGetOwnerElement() {
    assertEquals(element(), attribute.getOwnerElement());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetSchemaTypeInfo() {
    attribute.getSchemaTypeInfo();
  }

  @Test
  public void testIsId() {
    assertFalse(attribute.isId());
  }

  /*
   * Object API.
   */

  @Test
  public void testEquals() {
    assertTrue(attribute.equals(attribute()));
    assertFalse(attribute.equals(attribute.getOwnerElement()));
    assertFalse(attribute.equals(new Object()));
    assertFalse(attribute.equals(null));
  }
}
