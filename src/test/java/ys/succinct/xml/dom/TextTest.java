package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Tests for the succinct {@link Text} implementation.
 */
public class TextTest extends AbstractCharacterDataTest {

  protected Text text;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    text = textNode();
  }

  protected Text textNode() {
    Node book = getChildNode(document.getDocumentElement(), "book");
    Node title = getChildNode(book, "title");
    return (Text) title.getFirstChild();
  }

  protected String expectedText() {
    return "XML Developer's Guide";
  }

  @Override
  protected Node node() {
    return text;
  }

  @Override
  protected CharacterData characterData() {
    return text;
  }

  /*
   * Node API.
   */

  @Test
  public void testGetNodeName() {
    assertEquals("#text", text.getNodeName());
  }

  @Test
  public void testGetNodeType() {
    assertEquals(Node.TEXT_NODE, text.getNodeType());
  }

  @Test
  public void testGetNodeValue() {
    assertEquals(expectedText(), text.getNodeValue());
  }

  @Test
  public void testSetNodeValue() {
    try {
      text.setNodeValue(expectedText());
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testGetParentNode() {
    assertEquals("title", text.getParentNode().getNodeName());
  }

  @Test
  public void testGetChildNodes() {
    assertEquals(0, text.getChildNodes().getLength());
  }

  @Test
  public void testGetFirstChild() {
    assertNull(text.getFirstChild());
  }

  @Test
  public void testGetLastChild() {
    assertNull(text.getLastChild());
  }

  @Test
  public void testHasChildNodes() {
    assertFalse(text.hasChildNodes());
  }

  @Test
  public void testGetPreviousSibling() {
    assertNull(text.getPreviousSibling());
  }

  @Test
  public void testGetNextSibling() {
    assertNull(text.getNextSibling());
  }

  @Test
  public void testGetAttributes() {
    assertNull(text.getAttributes());
  }

  @Test
  public void testHasAttributes() {
    assertFalse(text.hasAttributes());
  }

  @Test
  public void testGetOwnerDocument() {
    assertEquals(document, text.getOwnerDocument());
  }

  @Test
  public void testIsDefaultNamespace() {
    assertFalse(text.isDefaultNamespace(NS_CATALOG));
  }

  @Test
  public void testGetNamespaceURI() {
    assertNull(text.getNamespaceURI());
  }

  @Test
  public void testGetPrefix() {
    assertNull(text.getPrefix());
  }

  @Test
  public void testGetLocalName() {
    assertNull(text.getLocalName());
  }

  @Test
  public void testGetTextContent() {
    assertEquals(expectedText(), text.getTextContent());
  }

  @Test
  public void testSetTextContent() {
    try {
      text.setTextContent(expectedText());
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testIsEqualNode() {
    assertTrue(text.isEqualNode(textNode()));
    assertFalse(text.isEqualNode(text.getParentNode()));
    assertFalse(text.isEqualNode(null));
  }

  @Test
  public void testIsSameNode() {
    assertTrue(text.isSameNode(textNode()));
    assertFalse(text.isSameNode(text.getParentNode()));
    assertFalse(text.isSameNode(null));
  }

  /*
   * CharacterData API.
   */

  @Test
  public void testGetData() {
    assertEquals(expectedText(), text.getData());
  }

  @Test
  public void testGetLength() {
    assertEquals(expectedText().length(), text.getLength());
  }

  @Test
  public void testSubstringData() {
    assertEquals(expectedText(), text.substringData(0, expectedText().length()));
    assertEquals(expectedText(), text.substringData(0, expectedText().length() * 2));
    assertEquals(expectedText().substring(0, expectedText().length() / 2),
        text.substringData(0, expectedText().length() / 2));
    assertEquals("", text.substringData(0, 0));
  }

  @Test
  public void testSubstringData_withInvalidCount() {
    try {
      text.substringData(0, -1);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
    }
  }

  @Test
  public void testSubstringData_withInvalidOffset() {
    try {
      // Offset is greater than the data's length.
      text.substringData(expectedText().length(), 1);
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
    text.getWholeText();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testIsElementContentWhitespace() {
    text.isElementContentWhitespace();
  }

  @Test
  public void testReplaceWholeText() {
    try {
      text.replaceWholeText(expectedText());
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testSplitText() {
    try {
      text.splitText(expectedText().length() / 2);
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
    assertTrue(text.equals(textNode()));
    assertFalse(text.equals(text.getParentNode()));
    assertFalse(text.equals(new Object()));
    assertFalse(text.equals(null));
  }
}
