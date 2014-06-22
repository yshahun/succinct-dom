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

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

/**
 * A succinct {@link ProcessingInstruction} node. Internal {@code ordinalIndex} is treated as the
 * index of the node among all the non-text nodes.
 *
 * @author Yauheni Shahun
 */
public class ProcessingInstructionImpl
    extends AbstractSuccinctNode implements ProcessingInstruction {

  /**
   * Reverse offset of the "target" index in the attribute bit string.
   */
  private static final int TARGET_REVERSE_OFFSET = 2;
  /**
   * Reverse offset of the "data" index in the attribute bit string.
   */
  private static final int DATA_REVERSE_OFFSET = 1;

  /**
   * Constructs a succinct processing instruction.
   *
   * @param dom the succinct DOM
   * @param index the index of the node in the balanced parentheses
   * @param ordinalIndex the index of the node among all the non-text nodes
   */
  public ProcessingInstructionImpl(SuccinctDom dom, int index, int ordinalIndex) {
    super(dom, index, ordinalIndex);
  }

  /*
   * Node API.
   */

  @Override
  public String getNodeName() {
    return dom.getPseudoAttribute(ordinalIndex, TARGET_REVERSE_OFFSET);
  }

  @Override
  public String getNodeValue() throws DOMException {
    return dom.getPseudoAttribute(ordinalIndex, DATA_REVERSE_OFFSET);
  }

  @Override
  public void setNodeValue(String nodeValue) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setNodeValue");
  }

  @Override
  public short getNodeType() {
    return Node.PROCESSING_INSTRUCTION_NODE;
  }

  @Override
  public Node getParentNode() {
    return dom.getParentNode(index);
  }

  @Override
  public NodeList getChildNodes() {
    return FixedNodeList.EMPTY;
  }

  @Override
  public Node getFirstChild() {
    return null;
  }

  @Override
  public Node getLastChild() {
    return null;
  }

  @Override
  public Node getPreviousSibling() {
    return dom.getPreviousSibling(index);
  }

  @Override
  public Node getNextSibling() {
    return dom.getNextSibling(index);
  }

  @Override
  public NamedNodeMap getAttributes() {
    return null;
  }

  @Override
  public Document getOwnerDocument() {
    return dom.getDocument();
  }

  @Override
  public boolean hasChildNodes() {
    return false;
  }

  @Override
  public String getNamespaceURI() {
    return null;
  }

  @Override
  public String getPrefix() {
    return null;
  }

  @Override
  public String getLocalName() {
    return null;
  }

  @Override
  public boolean hasAttributes() {
    return false;
  }

  @Override
  public String getTextContent() throws DOMException {
    return dom.getPseudoAttribute(ordinalIndex, DATA_REVERSE_OFFSET);
  }

  @Override
  public void setTextContent(String textContent) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setTextContent");
  }

  /*
   * ProcessingInstruction API.
   */

  @Override
  public String getTarget() {
    return dom.getPseudoAttribute(ordinalIndex, TARGET_REVERSE_OFFSET);
  }

  @Override
  public String getData() {
    return dom.getPseudoAttribute(ordinalIndex, DATA_REVERSE_OFFSET);
  }

  @Override
  public void setData(String data) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setData");
  }
}
