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
 * A store for the character content that can be read by the known offset. This implies that the
 * entire content is created by concatenating the individual contents. Subclasses may define
 * additional operations on the content, e.g. compression.
 *
 * @author Yauheni Shahun
 */
public interface OffsetStore {

  /**
   * Gets the character content by the given offset values.
   *
   * @param startIndex the index that the content starts from
   * @param endIndex the index that the content is adjacent to (exclusive)
   * @return the string value
   */
  String getString(int startIndex, int endIndex);
}
