package ys.succinct.xml.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests for {@link NameStore}.
 */
public class NameStoreTest {

  @Test
  public void testGetString() {
    NameStore.Builder builder = new NameStore.Builder();
    builder.addName("catalog");
    builder.addEmptyName();
    builder.addName("book");
    NameStore store = builder.build();

    assertNull(store.getString(0));
    assertEquals("catalog", store.getString(1));
    assertNull(store.getString(2));
    assertEquals("book", store.getString(3));
  }
}
