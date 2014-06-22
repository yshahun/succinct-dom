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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An ordered store for the family of the XML names. XML names are element/attribute names,
 * namespaces, prefixes. As the names are typically repeated multiple times within the XML document,
 * the store holds unique content only.
 *
 * @author Yauheni Shahun
 */
public class NameStore implements OrderedStore {

  private String[] names;

  /**
   * Constructs a name store.
   *
   * @param names the array of names
   */
  private NameStore(String[] names) {
    this.names = names;
  }

  @Override
  public String getString(int index) {
    return names[index];
  }

  @Override
  public int getLength(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getSubstring(int index, int offset, int count) {
    throw new UnsupportedOperationException();
  }

  /**
   * A builder that helps to construct a {@link NameStore}.
   */
  public static class Builder {

    /**
     * Index that is reserved for the null names.
     */
    private static final int NULL_NAME_INDEX = 0;

    private final List<String> names = new ArrayList<>();
    private final Map<String, Integer> uniqueNameMap = new HashMap<>();

    /**
     * Constructs a builder that will track the names starting from the index 1.
     */
    public Builder() {
      this(0);
    }

    /**
     * Constructs a builder that will track the names starting from the index next to the given
     * count.
     *
     * @param reservedCount the number of reserved indexes
     */
    public Builder(int reservedCount) {
      int count = reservedCount + 1;
      for (int i = 0; i < count; i++) {
        names.add(null);
      }
    }

    /**
     * Adds the given name to the store. If the name has been already added before, the previously
     * assigned index is returned. In case of {@code null} (what is the legal value) the special
     * reserved index is used.
     *
     * @param name the name value, or {@code null}
     * @return the index that the name has been given
     */
    public int addName(String name) {
      if (name == null) {
        return NULL_NAME_INDEX;
      }

      Integer nameIndex = uniqueNameMap.get(name);
      if (nameIndex == null) {
        nameIndex = names.size();
        names.add(name);
        uniqueNameMap.put(name, nameIndex);
      }
      return nameIndex;
    }

    /**
     * Reserves the index for the entity with no name.
     */
    public int addEmptyName() {
      names.add(null);
      return names.size() - 1;
    }

    /**
     * Returns the maximum index that has been tracked so far.
     */
    public int getMaxIndex() {
      return names.size() - 1;
    }

    /**
     * Builds the store using the names that the builder has collected.
     */
    public NameStore build() {
      return new NameStore(names.toArray(new String[names.size()]));
    }
  }
}
