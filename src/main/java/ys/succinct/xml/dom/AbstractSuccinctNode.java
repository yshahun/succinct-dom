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

/**
 * A succinct representation of {@link Node} that is identified by its position(s) in the
 * {@link SuccinctDom}.
 *
 * @author Yauheni Shahun
 */
public abstract class AbstractSuccinctNode extends AbstractReadonlyNode {

  protected final SuccinctDom dom;
  /**
   * The index of the node in the balanced parentheses representation of the DOM tree (unless it's
   * not changed in the subclass).
   */
  protected final int index;
  /**
   * The index of the node in the secondary structure. The nature of this structure is defined in
   * the subclasses.
   */
  protected final int ordinalIndex;

  /**
   * Constructs a succinct node.
   *
   * @param dom the succinct DOM
   * @param index the index of the node in the balanced parentheses
   */
  public AbstractSuccinctNode(SuccinctDom dom, int index) {
    this.dom = dom;
    this.index = index;
    this.ordinalIndex = -1;
  }

  /**
   * Constructs a succinct node.
   *
   * @param dom the succinct DOM
   * @param index the index of the node in the balanced parentheses
   * @param ordinalIndex the index of the node in the secondary structure
   */
  public AbstractSuccinctNode(SuccinctDom dom, int index, int ordinalIndex) {
    this.dom = dom;
    this.index = index;
    this.ordinalIndex = ordinalIndex;
  }

  @Override
  public boolean isDefaultNamespace(String namespaceURI) {
    return dom.isDefaultNamespace(namespaceURI);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((dom == null) ? 0 : dom.hashCode());
    result = prime * result + index;
    result = prime * result + ordinalIndex;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    AbstractSuccinctNode other = (AbstractSuccinctNode) obj;
    return dom == other.dom && index == other.index && ordinalIndex == other.ordinalIndex;
  }
}
