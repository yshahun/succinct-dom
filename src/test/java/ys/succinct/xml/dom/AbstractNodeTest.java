package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ys.succinct.xml.XmlBaseTest;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Common tests for a succinct {@link Node}.
 */
public abstract class AbstractNodeTest extends XmlBaseTest {

  protected Document document;
  protected Node anotherNode;

  protected DocumentBuilderFactory getFactory() {
    return getSuccinctFactory();
  }

  protected String getXml() {
    return XML;
  }

  protected Document getDocument() throws Exception {
    return build(getFactory(), getXml());
  }

  protected abstract Node node();

  @Before
  public void setUp() throws Exception {
    document = getDocument();
    anotherNode = mock(Node.class);
  }

  /*
   * DOM1.
   */

  @Test
  public void testAppendChild() {
    try {
      node().appendChild(anotherNode);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCloneNode() {
    node().cloneNode(false);
  }

  @Test
  public void testInsertBefore() {
    try {
      node().insertBefore(anotherNode, null);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNormalize() {
    node().normalize();
  }

  @Test
  public void testRemoveChild() {
    try {
      node().removeChild(node().getFirstChild());
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testReplaceChild() {
    try {
      node().replaceChild(anotherNode, node().getFirstChild());
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  /*
   * DOM2.
   */

  @Test
  public void testIsSupported() {
    assertFalse(node().isSupported(null, null));
  }

  @Test
  public void testSetPrefix() {
    try {
      node().setPrefix(null);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  /*
   * DOM3.
   */

  @Test
  public void testCompareDocumentPosition() {
    try {
      node().compareDocumentPosition(null);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NOT_SUPPORTED_ERR, e.code);
    }
  }

  @Test
  public void testGetBaseURI() {
    assertNull(node().getBaseURI());
  }

  @Test
  public void testGetFeature() {
    assertNull(node().getFeature("Core", "3.0"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testLookupNamespaceURI() {
    node().lookupNamespaceURI(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testLookupPrefix() {
    node().lookupPrefix(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetUserData() {
    node().getUserData("key");
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetUserData() {
    node().setUserData("key", "data", null);
  }
}
