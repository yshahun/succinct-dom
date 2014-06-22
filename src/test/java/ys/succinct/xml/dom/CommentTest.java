package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/**
 * Tests for the succinct {@link Comment} implementation.
 */
public class CommentTest extends AbstractCharacterDataTest {

  private static final String COMMENT = " Simple XML with the elements, attributes and comments. ";

  private Comment comment;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    comment = comment();
  }

  private Comment comment() {
    return (Comment) document.getFirstChild();
  }

  @Override
  protected CharacterData characterData() {
    return comment;
  }

  @Override
  protected Node node() {
    return comment;
  }

  /*
   * Node API.
   */

  @Test
  public void testGetNodeName() {
    assertEquals("#comment", comment.getNodeName());
  }

  @Test
  public void testGetNodeType() {
    assertEquals(Node.COMMENT_NODE, comment.getNodeType());
  }

  @Test
  public void testGetNodeValue() {
    assertEquals(COMMENT, comment.getNodeValue());
  }

  @Test
  public void testSetNodeValue() {
    try {
      comment.setNodeValue(COMMENT);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testGetParentNode() {
    assertEquals(Node.DOCUMENT_NODE, comment.getParentNode().getNodeType());
  }

  @Test
  public void testGetChildNodes() {
    assertEquals(0, comment.getChildNodes().getLength());
  }

  @Test
  public void testGetFirstChild() {
    assertNull(comment.getFirstChild());
  }

  @Test
  public void testGetLastChild() {
    assertNull(comment.getLastChild());
  }

  @Test
  public void testHasChildNodes() {
    assertFalse(comment.hasChildNodes());
  }

  @Test
  public void testGetPreviousSibling() {
    assertNull(comment.getPreviousSibling());
  }

  @Test
  public void testGetNextSibling() {
    assertEquals("catalog", comment.getNextSibling().getNodeName());
  }

  @Test
  public void testGetAttributes() {
    assertNull(comment.getAttributes());
  }

  @Test
  public void testHasAttributes() {
    assertFalse(comment.hasAttributes());
  }

  @Test
  public void testGetOwnerDocument() {
    assertEquals(document, comment.getOwnerDocument());
  }

  @Test
  public void testIsDefaultNamespace() {
    assertFalse(comment.isDefaultNamespace(NS_CATALOG));
  }

  @Test
  public void testGetNamespaceURI() {
    assertNull(comment.getNamespaceURI());
  }

  @Test
  public void testGetPrefix() {
    assertNull(comment.getPrefix());
  }

  @Test
  public void testGetLocalName() {
    assertNull(comment.getLocalName());
  }

  @Test
  public void testGetTextContent() {
    assertEquals(COMMENT, comment.getTextContent());
  }

  @Test
  public void testSetTextContent() {
    try {
      comment.setTextContent(COMMENT);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testIsEqualNode() {
    assertTrue(comment.isEqualNode(comment()));
    assertFalse(comment.isEqualNode(comment.getParentNode()));
    assertFalse(comment.isEqualNode(null));
  }

  @Test
  public void testIsSameNode() {
    assertTrue(comment.isSameNode(comment()));
    assertFalse(comment.isSameNode(comment.getParentNode()));
    assertFalse(comment.isSameNode(null));
  }

  /*
   * CharacterData API.
   */

  @Test
  public void testGetData() {
    assertEquals(COMMENT, comment.getData());
  }

  @Test
  public void testGetLength() {
    assertEquals(COMMENT.length(), comment.getLength());
  }

  @Test
  public void testSubstringData() {
    assertEquals(COMMENT, comment.substringData(0, COMMENT.length()));
    assertEquals(COMMENT, comment.substringData(0, COMMENT.length() * 2));
    assertEquals("XML", comment.substringData(8, 3));
    assertEquals("", comment.substringData(0, 0));
  }

  @Test
  public void testSubstringData_withInvalidCount() {
    try {
      comment.substringData(0, -1);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
    }
  }

  @Test
  public void testSubstringData_withInvalidOffset() {
    try {
      // Offset is greater than the data's length.
      comment.substringData(COMMENT.length(), 1);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.INDEX_SIZE_ERR, e.code);
    }
  }

  /*
   * Object API.
   */

  @Test
  public void testEquals() {
    assertTrue(comment.equals(comment()));
    assertFalse(comment.equals(comment.getParentNode()));
    assertFalse(comment.equals(null));
    assertFalse(comment.equals(new Object()));
  }
}
