package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Tests for the {@link Text} node that belongs to the succinct {@link Attr}.
 */
public class AttributeTextTest extends TextTest {

  @Override
  protected Text textNode() {
    Element book = (Element) getChildNode(document.getDocumentElement(), "book");
    return (Text) book.getAttributeNode("id").getFirstChild();
  }

  @Override
  protected String expectedText() {
    return "101";
  }

  /*
   * Node API.
   */

  @Override
  public void testGetParentNode() {
    assertEquals("id", text.getParentNode().getNodeName());
  }
}
