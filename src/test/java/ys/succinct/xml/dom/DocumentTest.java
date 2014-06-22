package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Tests for the succinct {@link Document} implementation.
 */
public class DocumentTest extends AbstractNodeTest {

  private static final String DATA = "data";
  private static final String NAME = "name";
  private static final String URI = "http://example.com";

  @Override
  protected Node node() {
    return document;
  }

  /*
   * Node API.
   */

  @Test
  public void testGetNodeName() {
    assertEquals("#document", document.getNodeName());
  }

  @Test
  public void testGetNodeValue() {
    assertNull(document.getNodeValue());
  }

  @Test
  public void testGetNodeType() {
    assertEquals(Node.DOCUMENT_NODE, document.getNodeType());
  }

  @Test
  public void testGetParentNode() {
    assertNull(document.getParentNode());
  }

  @Test
  public void testGetFirstChild() {
    assertEquals(Node.COMMENT_NODE, document.getFirstChild().getNodeType());
  }

  @Test
  public void testGetLastChild() {
    assertEquals(Node.ELEMENT_NODE, document.getLastChild().getNodeType());
  }

  @Test
  public void testGetNextSibling() {
    assertNull(document.getNextSibling());
  }

  @Test
  public void testGetPreviousSibling() {
    assertNull(document.getPreviousSibling());
  }

  @Test
  public void testHasChildNodes() {
    assertTrue(document.hasChildNodes());
  }

  @Test
  public void testGetChildNodes() {
    assertEquals(2, document.getChildNodes().getLength());
  }

  @Test
  public void testGetAttributes() {
    assertNull(document.getAttributes());
  }

  @Test
  public void testHasAttributes() {
    assertFalse(document.hasAttributes());
  }

  @Test
  public void testGetOwnerDocument() {
    assertNull(document.getOwnerDocument());
  }

  @Test
  public void testIsDefaultNamespace() {
    assertFalse(document.isDefaultNamespace(NS_CATALOG));
  }

  @Test
  public void testGetNamespaceURI() {
    assertNull(document.getNamespaceURI());
  }

  @Test
  public void testGetPrefix() {
    assertNull(document.getPrefix());
  }

  @Test
  public void testGetLocalName() {
    assertNull(document.getLocalName());
  }

  @Test
  public void testGetTextContent() {
    assertNull(document.getTextContent());
  }

  @Test
  public void testIsEqualNode() throws Exception {
    assertTrue(document.isEqualNode(getDocument()));
    assertFalse(document.isEqualNode(document.getDocumentElement()));
    assertFalse(document.isEqualNode(null));
  }

  @Test
  public void testIsSameNode() throws Exception {
    assertTrue(document.isSameNode(document.getDocumentElement().getOwnerDocument()));
    assertFalse(document.isSameNode(getDocument()));
    assertFalse(document.isSameNode(document.getDocumentElement()));
    assertFalse(document.isSameNode(null));
  }

  /*
   * Document API.
   */

  @Test
  public void testGetDoctype() {
    assertNull(document.getDoctype());
  }

  @Test
  public void testGetImplementation() {
    assertNull(document.getImplementation());
  }

  @Test
  public void testGetDocumentElement() {
    Element root = document.getDocumentElement();
    assertNotNull(root);
    assertEquals(Node.ELEMENT_NODE, root.getNodeType());
    assertEquals("catalog", root.getTagName());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateElement() {
    document.createElement(NAME);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateDocumentFragment() {
    document.createDocumentFragment();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateTextNode() {
    document.createTextNode(DATA);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateComment() {
    document.createComment(DATA);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateCDATASection() {
    document.createCDATASection(DATA);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateProcessingInstruction() {
    document.createProcessingInstruction(NAME, DATA);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateAttribute() {
    document.createAttribute(NAME);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateEntityReference() {
    document.createEntityReference(NAME);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateElementNS() {
    document.createElementNS(URI, NAME);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateAttributeNS() {
    document.createAttributeNS(URI, NAME);
  }

  @Test
  public void testGetElementsByTagName() {
    // TODO: Add test when it's implemented.
  }

  @Test
  public void testGetElementsByTagNameNS() {
    // TODO: Add test when it's implemented.
  }

  @Test
  public void testGetElementById() {
    // TODO: Add test when it's implemented.
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testImportNode() {
    document.importNode(anotherNode, true);
  }

  @Test
  public void testGetInputEncoding() {
    assertEquals("UTF-8", document.getInputEncoding());
  }

  @Test
  public void testGetXmlEncoding() {
    assertEquals("UTF-8", document.getXmlEncoding());
  }

  @Test
  public void testGetXmlStandalone() {
    assertFalse(document.getXmlStandalone());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetXmlStandalone() {
    document.setXmlStandalone(true);
  }

  @Test
  public void testGetXmlVersion() {
    assertEquals("1.0", document.getXmlVersion());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetXmlVersion() {
    document.setXmlVersion("1.1");
  }

  @Test
  public void testGetStrictErrorChecking() {
    assertTrue(document.getStrictErrorChecking());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetStrictErrorChecking() {
    document.setStrictErrorChecking(false);
  }

  @Test
  public void testGetDocumentURI() {
    assertTrue(document.getDocumentURI().endsWith(XML));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetDocumentURI() {
    document.setDocumentURI(URI);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAdoptNode() {
    document.adoptNode(anotherNode);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDomConfig() {
    document.getDomConfig();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNormalizeDocument() {
    document.normalizeDocument();
  }

  @Test
  public void testRenameNode() {
    try {
      document.renameNode(document.getDocumentElement(), URI, NAME);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NOT_SUPPORTED_ERR, e.code);
    }
  }
}
