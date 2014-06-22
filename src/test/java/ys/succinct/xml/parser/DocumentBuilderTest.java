package ys.succinct.xml.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import ys.succinct.xml.XmlBaseTest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Tests for the succinct {@link DocumentBuilder}.
 */
public class DocumentBuilderTest extends XmlBaseTest {

  private DocumentBuilderFactory factory;

  @Before
  public void setUp() throws Exception {
    factory = getSuccinctFactory();
  }

  @Test
  public void testParse_fromURL() throws Exception {
    assertNotNull(factory.newDocumentBuilder().parse(getResourceAsURL(XML)));
  }

  @Test
  public void testParse_fromByteStream() throws Exception {
    assertNotNull(factory.newDocumentBuilder().parse(getResourceAsByteStream(XML)));
  }

  @Test
  public void testParse_fromCharacterStream() throws Exception {
    assertNotNull(factory.newDocumentBuilder().parse(getResourceAsCharacterStream(XML)));
  }

  @Test
  public void testIsNamespaceAware() throws Exception {
    // XML namespaces support is off by default.
    assertFalse(factory.newDocumentBuilder().isNamespaceAware());

    factory.setNamespaceAware(true);
    assertTrue(factory.newDocumentBuilder().isNamespaceAware());

    factory.setNamespaceAware(false);
    assertFalse(factory.newDocumentBuilder().isNamespaceAware());
  }

  @Test
  public void testIsValidating() throws Exception {
    assertFalse(factory.newDocumentBuilder().isValidating());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetEntityResolver() throws Exception {
    factory.newDocumentBuilder().setEntityResolver(mock(EntityResolver.class));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSetErrorHandler() throws Exception {
    factory.newDocumentBuilder().setErrorHandler(mock(ErrorHandler.class));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewDocument() throws Exception {
    factory.newDocumentBuilder().newDocument();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetDOMImplementation() throws Exception {
    factory.newDocumentBuilder().getDOMImplementation();
  }
}
