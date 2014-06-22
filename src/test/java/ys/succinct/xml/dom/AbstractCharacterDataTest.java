package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;

/**
 * Tests for the succinct {@link CharacterData}.
 */
public abstract class AbstractCharacterDataTest extends AbstractNodeTest {

  private static final String DATA = "data";

  protected abstract CharacterData characterData();

  @Test
  public void testSetData() {
    try {
      characterData().setData(DATA);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testAppendData() {
    try {
      characterData().appendData(DATA);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testInsertData() {
    try {
      characterData().insertData(0, DATA);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testDeleteData() {
    try {
      characterData().deleteData(0, 1);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }

  @Test
  public void testReplaceData() {
    try {
      characterData().replaceData(0, DATA.length(), DATA);
      fail();
    } catch (DOMException e) {
      assertEquals(DOMException.NO_MODIFICATION_ALLOWED_ERR, e.code);
    }
  }
}
