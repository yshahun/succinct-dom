/*
 * Copyright 2014 Yauheni Shahun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ys.succinct.xml.dom;

import org.w3c.dom.Node;

import ys.succinct.util.CompactIntArray;
import ys.succinct.util.IntBitSet;

/**
 * An ordered store of the information about the following types of the XML nodes:
 * <ul>
 * <li>{@link Node#DOCUMENT_NODE}
 * <li>{@link Node#ELEMENT_NODE}
 * <li>{@link Node#COMMENT_NODE}
 * <li>{@link Node#CDATA_SECTION_NODE}.
 * </ul>
 * <p>
 * The store holds the qualified names of the elements primarily as well as the types of the
 * supported nodes.
 *
 * @author Yauheni Shahun
 */
public class ElementStore {

  private static final int MAX_TYPE_CODE = Node.NOTATION_NODE;

  /**
   * The compact array whose values are interpreted as:
   * <ul>
   * <li>The node type if the value is less than or equal to {@link #MAX_TYPE_CODE}
   * <li>The index of the element name if the value is greater than {@link #MAX_TYPE_CODE}.
   * </ul>
   */
  private final CompactIntArray nameTypeCodes;
  /**
   * The store of the unique local names of the elements.
   */
  private final OrderedStore nameStore;

  /**
   * The compact array that points to the indexes of the unique namespace URIs and prefixes of the
   * elements. The ordinal indexes of the namespace URIs and prefixes are interleaved i.e. (0,1),
   * (2,3), etc.
   */
  private final CompactIntArray namespaceIndexes;
  /**
   * The store of the unique namespace URIs and prefixes of the elements.
   */
  private final OrderedStore namespaceStore;

  /**
   * Constructs an {@link ElementStore}.
   *
   * @param nameIndexes the compact array of the codes that point either to the node type or to the
   *        index of the element name
   * @param nameStore the store of the unique local names of the elements
   * @param namespaceIndexes the compact array that points to the indexes of the unique namespace
   *        URIs and prefixes of the elements
   * @param namespaceStore the store of the unique namespace URIs and prefixes of the elements
   */
  public ElementStore(CompactIntArray nameIndexes, OrderedStore nameStore,
      CompactIntArray namespaceIndexes, OrderedStore namespaceStore) {
    this.nameTypeCodes = nameIndexes;
    this.nameStore = nameStore;
    this.namespaceIndexes = namespaceIndexes;
    this.namespaceStore = namespaceStore;
  }

  /**
   * Returns the local part of the element's qualified name.
   *
   * @param index the 0-based index of the element
   */
  public String getName(int index) {
    return nameStore.getString(getNameCode(index));
  }

  /**
   * Returns the element's qualified name i.e. the local name together with the prefix if it has
   * been recognized during parsing. Otherwise, the prefix can be already included into the name
   * part.
   *
   * @param index the 0-based index of the element
   */
  public String getQName(int index) {
    String name = nameStore.getString(getNameCode(index));
    String prefix = getPrefix(index);
    return (prefix != null) ? (prefix + ":" + name) : name;
  }

  /**
   * Returns the element's namespace URI if it exists.
   *
   * @param index the 0-based index of the element
   */
  public String getNamespaceUri(int index) {
    int nameIndex = namespaceIndexes.getInt(index * 2);
    return namespaceStore.getString(nameIndex);
  }

  /**
   * Returns the element's namespace prefix if it has been recognized during parsing, otherwise
   * {@code null}.
   *
   * @param index the 0-based index of the element
   */
  public String getPrefix(int index) {
    int nameIndex = namespaceIndexes.getInt(index * 2 + 1);
    return namespaceStore.getString(nameIndex);
  }

  /**
   * Returns the type of the node.
   *
   * @param index the 0-based index of the node
   */
  public int getType(int index) {
    int code = nameTypeCodes.getInt(index);
    return (code > MAX_TYPE_CODE) ? Node.ELEMENT_NODE : code;
  }

  /**
   * Returns the index of the element name.
   *
   * @throws IllegalStateException if the requested index doesn't correspond to the element node
   */
  private int getNameCode(int index) {
    int code = nameTypeCodes.getInt(index);
    if (code > MAX_TYPE_CODE) {
      return code; // it's element's name index.
    } else {
      throw new IllegalStateException("Name is not supported for the type " + code);
    }
  }

  /**
   * A builder that helps to construct an {@link ElementStore}.
   */
  public static class Builder {

    private IntBitSet nameTypeCodes = new IntBitSet();
    private IntBitSet namespaceIndexes = new IntBitSet();
    private NameStore.Builder nameBuilder = new NameStore.Builder(MAX_TYPE_CODE);
    private NameStore.Builder namespaceBuilder = new NameStore.Builder();
    private int nameCount;

    /**
     * Adds an information about the node to the store. The qualified name is optional as not all
     * the nodes have it.
     *
     * @param namespaceUri the namespace URI or {@code null}
     * @param prefix the namespace prefix or {@code null}
     * @param name the local name or {@code null}
     * @param type the {@link Node} type
     */
    public void addNode(String namespaceUri, String prefix, String name, short type) {
      if (type == Node.ELEMENT_NODE) {
        nameTypeCodes.setInt(nameCount, nameBuilder.addName(name)); // Add name index.
      } else {
        nameTypeCodes.setInt(nameCount, type); // Add node type.
      }
      // Always track the namespaces for the consistent indexing.
      namespaceIndexes.setInt(nameCount * 2, namespaceBuilder.addName(namespaceUri));
      namespaceIndexes.setInt(nameCount * 2 + 1, namespaceBuilder.addName(prefix));
      nameCount++;
    }

    /**
     * Adds the node without a name (i.e. other than {@link Node#ELEMENT_NODE}) to the store.
     *
     * @param type the {@link Node} type
     */
    public void addNode(short type) {
      addNode(null, null, null, type);
    }

    /**
     * Builds the store using the node data that the builder has collected.
     *
     * @return the {@link ElementStore} instance
     */
    public ElementStore build() {
      return new ElementStore(
          new CompactIntArray(nameTypeCodes.toIntArray(nameCount * 32), nameBuilder.getMaxIndex()),
          nameBuilder.build(),
          new CompactIntArray(
              namespaceIndexes.toIntArray(nameCount * 2 * 32), namespaceBuilder.getMaxIndex()),
          namespaceBuilder.build());
    }
  }
}
