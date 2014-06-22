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

/**
 *
 * A simple {@link OffsetStore} that is backed by the character array and stores the content as is.
 *
 * @author Yauheni Shahun
 */
public class ArrayOffsetStore implements OffsetStore {

  private final char[] content;

  /**
   * Constructs a store.
   *
   * @param content the entire content concatenated into the character array
   */
  private ArrayOffsetStore(char[] content) {
    this.content = content;
  }

  @Override
  public String getString(int startIndex, int endIndex /* exclusive */) {
    return new String(content, startIndex, endIndex - startIndex);
  }

  /**
   * A builder that helps to construct a {@link ArrayOffsetStore}.
   */
  public static class Builder {

    private final StringBuilder buffer = new StringBuilder();

    /**
     * Adds the character content to the store.
     *
     * @param chars the character array
     * @param start the index in the array that the content starts from
     * @param length the length of the content
     * @return the offset value for the content in the store
     */
    public int addChars(char[] chars, int start, int length) {
      int offset = buffer.length();
      buffer.append(chars, start, length);
      return offset;
    }

    /**
     * Adds the given string to the store.
     *
     * @param s the content
     * @return the offset value for the content in the store
     */
    public int addString(String s) {
      int offset = buffer.length();
      buffer.append(s);
      return offset;
    }

    /**
     * Builds the store using the content that the builder has collected.
     *
     * @return the {@link ArrayOffsetStore} instance
     */
    public ArrayOffsetStore build() {
      char[] text = new char[buffer.length()];
      buffer.getChars(0, buffer.length(), text, 0);
      return new ArrayOffsetStore(text);
    }
  }
}
