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
 * An ordered collection of known {@link Node}s.
 *
 * @author Yauheni Shahun
 */
public class FixedNodeList implements NodeList {

  /**
   * The instance of the empty {@link NodeList}.
   */
  public static final NodeList EMPTY = new FixedNodeList();

  private final Node[] nodes;

  /**
   * Constructs an empty list.
   */
  private FixedNodeList() {
    nodes = new Node[0];
  }

  /**
   * Constructs the list of nodes.
   */
  public FixedNodeList(Node... nodes) {
    this.nodes = new Node[nodes.length];
    for (int i = 0; i < nodes.length; i++) {
      this.nodes[i] = nodes[i];
    }
  }

  @Override
  public Node item(int index) {
    if (index >= 0 && index < nodes.length) {
      return nodes[index];
    }
    return null;
  }

  @Override
  public int getLength() {
    return nodes.length;
  }
}
