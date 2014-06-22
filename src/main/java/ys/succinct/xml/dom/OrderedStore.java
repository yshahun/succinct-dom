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
 * An ordered store of the character content.
 *
 * @author Yauheni Shahun
 */
public interface OrderedStore {

  /**
   * Returns the content string.
   *
   * @param index the 0-based index of the content
   */
  String getString(int index);

  /**
   * Returns the content length.
   *
   * @param index the 0-based index of the content
   */
  int getLength(int index);

  /**
   * Gets a substring of the content.
   *
   * @param index the 0-based index of the content
   * @param offset the position within the content that the substring starts from
   * @param count the length of the substring
   * @return the substring value, or {@code null} if the offset is out of bound
   */
  String getSubstring(int index, int offset, int count);
}
