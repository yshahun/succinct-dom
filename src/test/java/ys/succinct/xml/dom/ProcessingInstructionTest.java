package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

/**
 * Tests for the succinct {@link ProcessingInstruction} implementation.
 */
public class ProcessingInstructionTest extends AbstractNodeTest {

  private static final String DATA = "author asc";

  private ProcessingInstruction pi;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    pi = getProcessingInstruction();
  }

  private ProcessingInstruction getProcessingInstruction() {
    return (ProcessingInstruction) document.getDocumentElement().getPreviousSibling();
  }

  @Override
  protected String getXml() {
    return XML_PI;
  }

  @Override
  protected Node node() {
    return pi;
  }

  /*
   * Node API.
   */

  @Test
  public void testGetNodeName() {
    assertEquals("sort", pi.getNodeName());
  }

  @Test
  public void getNodeValue() {
    assertEquals(DATA, pi.getNodeValue());
  }

  @Test
  public void testSetNodeValue() {
    try {
      pi.setNodeValue(DATA);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testGetNodeType() {
    assertEquals(Node.PROCESSING_INSTRUCTION_NODE, pi.getNodeType());
  }

  @Test
  public void testGetParentNode() {
    assertEquals(document, pi.getParentNode());
  }

  @Test
  public void testGetChildNodes() {
    assertEquals(0, pi.getChildNodes().getLength());
  }

  @Test
  public void testGetFirstChild() {
    assertNull(pi.getFirstChild());
  }

  @Test
  public void testGetLastChild() {
    assertNull(pi.getLastChild());
  }

  @Test
  public void testHasChildNodes() {
    assertFalse(pi.hasChildNodes());
  }

  @Test
  public void testGetPreviousSibling() {
    assertEquals(Node.COMMENT_NODE, pi.getPreviousSibling().getNodeType());
  }

  @Test
  public void testGetNextSibling() {
    assertEquals("catalog", pi.getNextSibling().getNodeName());
  }

  @Test
  public void testGetAttributes() {
    assertNull(pi.getAttributes());
  }

  @Test
  public void testHasAttributes() {
    assertFalse(pi.hasAttributes());
  }

  @Test
  public void testGetOwnerDocument() {
    assertEquals(document, pi.getOwnerDocument());
  }

  @Test
  public void testIsDefaultNamespace() {
    assertFalse(pi.isDefaultNamespace(NS_CATALOG));
  }

  @Test
  public void testGetNamespaceURI() {
    assertNull(pi.getNamespaceURI());
  }

  @Test
  public void testGetPrefix() {
    assertNull(pi.getPrefix());
  }

  @Test
  public void testGetLocalName() {
    assertNull(pi.getLocalName());
  }

  @Test
  public void testGetTextContent() {
    assertEquals(DATA, pi.getTextContent());
  }

  @Test
  public void testSetTextContent() {
    try {
      pi.setTextContent(DATA);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testIsEqualNode() {
    assertTrue(pi.isEqualNode(getProcessingInstruction()));
    assertFalse(pi.isEqualNode(pi.getParentNode()));
    assertFalse(pi.isEqualNode(null));
  }

  @Test
  public void testIsSameNode() {
    assertTrue(pi.isSameNode(getProcessingInstruction()));
    assertFalse(pi.isSameNode(pi.getParentNode()));
    assertFalse(pi.isSameNode(null));
  }

  /*
   * ProcessingInstruction API.
   */

  @Test
  public void testGetTarget() {
    assertEquals("sort", pi.getTarget());
  }

  @Test
  public void testGetData() {
    assertEquals(DATA, pi.getData());
  }

  @Test
  public void testSetData() {
    try {
      pi.setData(DATA);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }
}
