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

import ys.succinct.util.CompactIntArray;
import ys.succinct.util.IntBitSet;

/**
 * An ordered store of the information about the qualified names of the XML nodes. The qualified
 * name consists of the local name, namespace URI and prefix.
 *
 * @author Yauheni Shahun
 */
public class QNameStore {

  /**
   * The compact array that points to the indexes of the unique local names.
   */
  private final CompactIntArray nameIndexes;
  /**
   * The store of the unique local names.
   */
  private final OrderedStore nameStore;

  /**
   * The compact array that points to the indexes of the unique namespace URIs and prefixes. The
   * ordinal indexes of the namespace URIs and prefixes are interleaved i.e. (0,1), (2,3), etc.
   */
  private final CompactIntArray namespaceIndexes;
  /**
   * The store of the unique namespace URIs and prefixes.
   */
  private final OrderedStore namespaceStore;

  /**
   * Constructs a store for the qualified names of the XML nodes.
   *
   * @param nameIndexes the compact array that points to the indexes of the unique local names
   * @param nameStore the store of the unique local names
   * @param namespaceIndexes the compact array that points to the indexes of the unique namespace
   *        URIs and prefixes
   * @param namespaceStore the store of the unique namespace URIs and prefixes
   */
  private QNameStore(CompactIntArray nameIndexes, OrderedStore nameStore,
      CompactIntArray namespaceIndexes, OrderedStore namespaceStore) {
    this.nameIndexes = nameIndexes;
    this.nameStore = nameStore;
    this.namespaceIndexes = namespaceIndexes;
    this.namespaceStore = namespaceStore;
  }

  /**
   * Returns the local part of the qualified name.
   *
   * @param index the 0-based index of the node
   */
  public String getName(int index) {
    return nameStore.getString(nameIndexes.getInt(index));
  }

  /**
   * Returns the qualified name i.e. the local name together with the prefix if there is one.
   *
   * @param index the 0-based index of the node
   */
  public String getQName(int index) {
    String name = nameStore.getString(nameIndexes.getInt(index));
    String prefix = getPrefix(index);
    return (prefix != null) ? (prefix + ":" + name) : name;
  }

  /**
   * Returns the namespace URI if it exists.
   *
   * @param index the 0-based index of the node
   */
  public String getNamespaceUri(int index) {
    int nameIndex = namespaceIndexes.getInt(index * 2);
    return namespaceStore.getString(nameIndex);
  }

  /**
   * Returns the namespace prefix if it exists.
   *
   * @param index the 0-based index of the node
   */
  public String getPrefix(int index) {
    int nameIndex = namespaceIndexes.getInt(index * 2 + 1);
    return namespaceStore.getString(nameIndex);
  }

  /**
   * A builder that helps to construct a {@link QNameStore}.
   */
  public static class Builder {

    private IntBitSet nameIndexes = new IntBitSet();
    private IntBitSet namespaceIndexes = new IntBitSet();
    private NameStore.Builder nameBuilder = new NameStore.Builder();
    private NameStore.Builder namespaceBuilder = new NameStore.Builder();
    private int nameCount;

    /**
     * Adds the qualified name of the node to the store.
     *
     * @param namespaceUri the namespace URI or {@code null}
     * @param prefix the namespace prefix or {@code null}
     * @param name the local name or {@code null}
     */
    public void addName(String namespaceUri, String prefix, String name) {
      nameIndexes.setInt(nameCount, nameBuilder.addName(name));
      namespaceIndexes.setInt(nameCount * 2, namespaceBuilder.addName(namespaceUri));
      namespaceIndexes.setInt(nameCount * 2 + 1, namespaceBuilder.addName(prefix));
      nameCount++;
    }

    /**
     * Reserves the position for the node with no name.
     */
    public void addEmptyName() {
      addName(null, null, null);
    }

    /**
     * Builds the store using the qualified names that the builder has collected.
     *
     * @return the {@link QNameStore} instance
     */
    public QNameStore build() {
      return new QNameStore(
          new CompactIntArray(nameIndexes.toIntArray(nameCount * 32), nameBuilder.getMaxIndex()),
          nameBuilder.build(),
          new CompactIntArray(
              namespaceIndexes.toIntArray(nameCount * 2 * 32), namespaceBuilder.getMaxIndex()),
          namespaceBuilder.build());
    }
  }
}
