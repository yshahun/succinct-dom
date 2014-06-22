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

import ys.succinct.util.IntBitSet;

/**
 * An ordered store for the XML text content (node values).
 *
 * @author Yauheni Shahun
 */
public class ValueStore implements OrderedStore {

  private final int[] offsets;
  private final OffsetStore textStore;

  /**
   * Constructs a store.
   *
   * @param offsets the array of the offset values
   * @param textStore the underlying {@link OffsetStore} that the offset values match to
   */
  private ValueStore(int[] offsets, OffsetStore textStore) {
    this.offsets = offsets;
    this.textStore = textStore;
  }

  @Override
  public String getString(int index) {
    return textStore.getString(offsets[index], offsets[index + 1]);
  }

  @Override
  public int getLength(int index) {
    return offsets[index + 1] - offsets[index];
  }

  @Override
  public String getSubstring(int index, int offset, int count) {
    int startIndex = offsets[index] + offset;
    int endIndex = offsets[index + 1]; // exclusive
    if (startIndex >= endIndex) {
      return null;
    } else {
      return textStore.getString(startIndex, Math.min(startIndex + count, endIndex));
    }
  }

  /**
   * A builder that helps to construct a {@link ValueStore}.
   */
  public static class Builder {

    private ArrayOffsetStore.Builder textBuilder = new ArrayOffsetStore.Builder();
    private IntBitSet offsets = new IntBitSet();
    private int valueCount;

    /**
     * Adds the content value to the store.
     *
     * @param chars the character array
     * @param start the index in the array that the value starts from
     * @param length the length of the value
     */
    public void addValue(char[] chars, int start, int length) {
      int offset = textBuilder.addChars(chars, start, length);
      offsets.setInt(valueCount++, offset);
    }

    /**
     * Adds the string value to the store.
     *
     * @param value the value
     */
    public void addValue(String value) {
      int offset = textBuilder.addString(value);
      offsets.setInt(valueCount++, offset);
    }

    /**
     * Builds the store using the values that the builder has collected.
     *
     * @return the {@link ValueStore} instance
     */
    public ValueStore build() {
      // Add the virtual value to track the total offset to avoid the range check in the get calls.
      offsets.setInt(valueCount++, textBuilder.addString(""));
      return new ValueStore(offsets.toIntArray(valueCount * 32), textBuilder.build());
    }
  }
}
