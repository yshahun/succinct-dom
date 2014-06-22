package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/**
 * Tests for the succinct {@link CDATASection} implementation.
 */
public class CDataTest extends AbstractCharacterDataTest {

  private static final String DATA = "An in-depth look at creating applications with XML.";

  private CDATASection cdata;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    cdata = cdata();
  }

  private CDATASection cdata() {
    Node book = getChildNode(document.getDocumentElement(), "book");
    Node markup = getChildNode(book, "markup");
    return (CDATASection) markup.getFirstChild();
  }

  @Override
  protected CharacterData characterData() {
    return cdata;
  }

  @Override
  protected Node node() {
    return cdata;
  }

  /*
   * Node API.
   */

  @Test
  public void testGetNodeName() {
    assertEquals("#cdata-section", cdata.getNodeName());
  }

  @Test
  public void testGetNodeType() {
    assertEquals(Node.CDATA_SECTION_NODE, cdata.getNodeType());
  }

  @Test
  public void testGetNodeValue() {
    assertEquals(DATA, cdata.getNodeValue());
  }

  @Test
  public void testSetNodeValue() {
    try {
      cdata.setNodeValue(DATA);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testGetParentNode() {
    assertEquals("markup", cdata.getParentNode().getNodeName());
  }

  @Test
  public void testGetChildNodes() {
    assertEquals(0, cdata.getChildNodes().getLength());
  }

  @Test
  public void testGetFirstChild() {
    assertNull(cdata.getFirstChild());
  }

  @Test
  public void testGetLastChild() {
    assertNull(cdata.getLastChild());
  }

  @Test
  public void testHasChildNodes() {
    assertFalse(cdata.hasChildNodes());
  }

  @Test
  public void testGetPreviousSibling() {
    assertNull(cdata.getPreviousSibling());
  }

  @Test
  public void testGetNextSibling() {
    assertNull(cdata.getNextSibling());
  }

  @Test
  public void testGetAttributes() {
    assertNull(cdata.getAttributes());
  }

  @Test
  public void testHasAttributes() {
    assertFalse(cdata.hasAttributes());
  }

  @Test
  public void testGetOwnerDocument() {
    assertNotNull(cdata.getOwnerDocument());
  }

  @Test
  public void testIsDefaultNamespace() {
    assertFalse(cdata.isDefaultNamespace(NS_CATALOG));
  }

  @Test
  public void testGetNamespaceURI() {
    assertNull(cdata.getNamespaceURI());
  }

  @Test
  public void testGetPrefix() {
    assertNull(cdata.getPrefix());
  }

  @Test
  public void testGetLocalName() {
    assertNull(cdata.getLocalName());
  }

  @Test
  public void testGetTextContent() {
    assertEquals(DATA, cdata.getTextContent());
  }

  @Test
  public void testSetTextContent() {
    try {
      cdata.setTextContent(DATA);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testIsEqualNode() {
    assertTrue(cdata.isEqualNode(cdata()));
    assertFalse(cdata.isEqualNode(cdata.getParentNode()));
    assertFalse(cdata.isEqualNode(null));
  }

  @Test
  public void testIsSameNode() {
    assertTrue(cdata.isSameNode(cdata()));
    assertFalse(cdata.isSameNode(cdata.getParentNode()));
    assertFalse(cdata.isSameNode(null));
  }

  /*
   * CharacterData API.
   */

  @Test
  public void testGetData() {
    assertEquals(DATA, cdata.getData());
  }

  @Test
  public void testGetLength() {
    assertEquals(DATA.length(), cdata.getLength());
  }

  @Test
  public void testSubstringData() {
    assertEquals(DATA, cdata.substringData(0, DATA.length()));
    assertEquals(DATA, cdata.substringData(0, DATA.length() * 2));
    assertEquals("look", cdata.substringData(12, 4));
    assertEquals("", cdata.substringData(0, 0));
  }

  @Test
  public void testSubstringData_withInvalidCount() {
    try {
      cdata.substringData(0, -1);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
    }
  }

  @Test
  public void testSubstringData_withInvalidOffset() {
    try {
      // Offset is greater than the data's length.
      cdata.substringData(DATA.length(), 1);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
    }
  }

  /*
   * Text API.
   */

  @Test(expected = UnsupportedOperationException.class)
  public void testGetWholeText() {
    cdata.getWholeText();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testIsElementContentWhitespace() {
    cdata.isElementContentWhitespace();
  }

  @Test
  public void testReplaceWholeText() {
    try {
      cdata.replaceWholeText(DATA);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testSplitText() {
    try {
      cdata.splitText(DATA.length() / 2);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  /*
   * Object API.
   */

  @Test
  public void testEquals() {
    assertTrue(cdata.equals(cdata()));
    assertFalse(cdata.equals(cdata.getParentNode()));
    assertFalse(cdata.equals(null));
    assertFalse(cdata.equals(new Object()));
  }
}
