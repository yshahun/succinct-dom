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
import org.w3c.dom.NodeList;

/**
 * An ordered collection of the succinct {@link Node}s that are available lazily.
 *
 * @author Yauheni Shahun
 */
public class NodeListImpl implements NodeList {

  private final SuccinctDom dom;
  /**
   * The indexes of the nodes in the balanced parentheses.
   */
  private final int[] nodeIndexes;

  /**
   * Constructs a succinct {@link NodeList}.
   *
   * @param dom the succinct DOM
   * @param nodeIndexes the array of the indexes of the nodes in the collection
   */
  public NodeListImpl(SuccinctDom dom, int[] nodeIndexes) {
    this.dom = dom;
    this.nodeIndexes = nodeIndexes;
  }

  @Override
  public Node item(int index) {
    if (index < 0 || index >= nodeIndexes.length) {
      return null;
    }
    return dom.getNode(nodeIndexes[index]);
  }

  @Override
  public int getLength() {
    return nodeIndexes.length;
  }
}
