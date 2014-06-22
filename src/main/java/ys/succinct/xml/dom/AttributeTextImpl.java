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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * A {@link Text} node that belongs to the attribute and represents its character value. This node
 * is virtual. It's not represented in the succinct DOM's structure and created on demand.
 *
 * @see Attr#getFirstChild()
 *
 * @author Yauheni Shahun
 */
public class AttributeTextImpl extends AbstractReadonlyNode implements Text {

  private final SuccinctDom dom;
  private final int attributeIndex;
  private final String text;

  /**
   * Constructs the attribute's text node.
   *
   * @param dom the succinct DOM
   * @param attributeIndex the index of the attribute in the attribute bit string
   * @param text the value of the attribute
   */
  public AttributeTextImpl(SuccinctDom dom, int attributeIndex, String text) {
    this.dom = dom;
    this.attributeIndex = attributeIndex;
    this.text = text;
  }

  /*
   * Node API.
   */

  @Override
  public String getNodeName() {
    return "#text";
  }

  @Override
  public String getNodeValue() throws DOMException {
    return text;
  }

  @Override
  public void setNodeValue(String nodeValue) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setNodeValue");
  }

  @Override
  public short getNodeType() {
    return TEXT_NODE;
  }

  @Override
  public Node getParentNode() {
    return new AttributeImpl(dom, attributeIndex);
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
    return null;
  }

  @Override
  public Node getNextSibling() {
    return null;
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
    return text;
  }

  @Override
  public void setTextContent(String textContent) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setTextContent");
  }

  @Override
  public boolean isDefaultNamespace(String namespaceURI) {
    return dom.isDefaultNamespace(namespaceURI);
  }

  /*
   * CharacterData API.
   */

  @Override
  public String getData() throws DOMException {
    return text;
  }

  @Override
  public void setData(String data) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setData");
  }

  @Override
  public int getLength() {
    return text.length();
  }

  @Override
  public String substringData(int offset, int count) throws DOMException {
    if (offset >= 0 && offset < text.length() && count >= 0) {
      return text.substring(offset, Math.min(text.length(), offset + count));
    }
    throw new DOMException(DOMException.INDEX_SIZE_ERR,
        String.format("Wrong parameters in substringData(%d,%d)", offset, count));
  }

  @Override
  public void appendData(String arg) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "appendData");
  }

  @Override
  public void insertData(int offset, String arg) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "insertData");
  }

  @Override
  public void deleteData(int offset, int count) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "deleteData");
  }

  @Override
  public void replaceData(int offset, int count, String arg) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "replaceData");
  }

  /*
   * Text API.
   */

  @Override
  public Text splitText(int offset) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "splitText");
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
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "replaceWholeText");
  }

  /*
   * Object API.
   */

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + attributeIndex;
    result = prime * result + ((dom == null) ? 0 : dom.hashCode());
    // The text is the secondary field, therefore ignore it in the hash code.
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
    AttributeTextImpl other = (AttributeTextImpl) obj;
    // The text is the secondary field, therefore ignore it in the comparison.
    return dom == other.dom && attributeIndex == other.attributeIndex;
  }
}
