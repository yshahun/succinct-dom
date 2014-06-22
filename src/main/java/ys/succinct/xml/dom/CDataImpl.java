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

import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * A succinct {@link CDATASection} node. Internal {@code ordinalIndex} is treated as the index of
 * the node among all the non-text nodes.
 *
 * @author Yauheni Shahun
 */
public class CDataImpl extends AbstractCharacterData implements CDATASection {

  private static final int DATA_REVERSE_OFFSET = 1;

  /**
   * Constructs a succinct CDATA node.
   *
   * @param dom the succinct DOM
   * @param index the index of the node in the balanced parentheses
   * @param ordinalIndex the index of the node among all the non-text nodes
   */
  public CDataImpl(SuccinctDom dom, int index, int ordinalIndex) {
    super(dom, index, ordinalIndex);
  }

  /*
   * Node API.
   */

  @Override
  public String getNodeName() {
    return "#cdata-section";
  }

  @Override
  public String getNodeValue() throws DOMException {
    return dom.getPseudoAttribute(ordinalIndex, DATA_REVERSE_OFFSET);
  }

  @Override
  public void setNodeValue(String nodeValue) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
        "setNodeValue");
  }

  @Override
  public short getNodeType() {
    return Node.CDATA_SECTION_NODE;
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
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
        "setTextContent");
  }

  /*
   * CharacterData API.
   */

  @Override
  public String getData() throws DOMException {
    return dom.getPseudoAttribute(ordinalIndex, DATA_REVERSE_OFFSET);
  }

  @Override
  public int getLength() {
    return dom.getPseudoAttributeLength(ordinalIndex, DATA_REVERSE_OFFSET);
  }

  @Override
  public String substringData(int offset, int count) throws DOMException {
    if (offset >= 0 && count >= 0) {
      String substr =
          dom.getPseudoAttributeSubstring(ordinalIndex, DATA_REVERSE_OFFSET, offset, count);
      // Null indicates that the offset exceeds the data length.
      if (substr != null) {
        return substr;
      }
    }
    throw new DOMException(DOMException.INDEX_SIZE_ERR, String.format(
        "Wrong parameters in substringData(%d,%d)", offset, count));
  }

  /*
   * Text API.
   */

  @Override
  public Text splitText(int offset) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
        "splitText");
  }

  @Override
  public boolean isElementContentWhitespace() {
    throw new UnsupportedOperationException("isElementContentWhitespace");
  }

  @Override
  public String getWholeText() {
    throw new UnsupportedOperationException("getWholeText");
  }

  @Override
  public Text replaceWholeText(String content) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
        "replaceWholeText");
  }
}
