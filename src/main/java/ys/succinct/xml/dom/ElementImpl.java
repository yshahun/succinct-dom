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
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

/**
 * A succinct {@link Element} node. Internal {@code ordinalIndex} is treated as the index of the
 * node in the non-text node store.
 *
 * @author Yauheni Shahun
 */
public class ElementImpl extends AbstractSuccinctNode implements Element {

  /**
   * Cached collection of the attributes of the element.
   */
  private NamedNodeMap attributes;

  /**
   * Constructs a succinct element node.
   *
   * @param dom the succinct DOM
   * @param index the index of the node in the balanced parentheses
   * @param ordinalIndex the index of the node in the non-text node store
   */
  public ElementImpl(SuccinctDom dom, int index, int ordinalIndex) {
    super(dom, index, ordinalIndex);
  }

  /*
   * Node API.
   */

  @Override
  public String getNodeName() {
    return dom.getQName(ordinalIndex);
  }

  @Override
  public String getNodeValue() throws DOMException {
    return null;
  }

  @Override
  public void setNodeValue(String nodeValue) throws DOMException {
    // No op.
  }

  @Override
  public short getNodeType() {
    return ELEMENT_NODE;
  }

  @Override
  public Node getParentNode() {
    return dom.getParentNode(index);
  }

  @Override
  public Node getFirstChild() {
    return dom.getFirstChild(index);
  }

  @Override
  public Node getLastChild() {
    return dom.getLastChild(index);
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
  public boolean hasChildNodes() {
    return dom.hasChildNodes(index);
  }

  @Override
  public NodeList getChildNodes() {
    return dom.getChildNodes(index);
  }

  @Override
  public Document getOwnerDocument() {
    return dom.getDocument();
  }

  @Override
  public String getNamespaceURI() {
    return dom.getNamespaceURI(ordinalIndex);
  }

  @Override
  public String getPrefix() {
    return dom.getPrefix(ordinalIndex);
  }

  @Override
  public String getLocalName() {
    return dom.getLocalName(ordinalIndex);
  }

  @Override
  public NamedNodeMap getAttributes() {
    if (attributes == null) {
      attributes = dom.getAttributes(ordinalIndex);
    }
    return attributes;
  }

  @Override
  public boolean hasAttributes() {
    return dom.hasAttributes(ordinalIndex);
  }

  @Override
  public String getTextContent() throws DOMException {
    return dom.getTextContent(index);
  }

  @Override
  public void setTextContent(String textContent) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setTextContent");
  }

  /*
   * Element API.
   */

  @Override
  public String getTagName() {
    return dom.getQName(ordinalIndex);
  }

  @Override
  public String getAttribute(String name) {
    String value = null;
    Node attribute = getAttributes().getNamedItem(name);
    if (attribute != null) {
      value = attribute.getNodeValue();
    }
    return (value == null) ? "" : value;
  }

  @Override
  public void setAttribute(String name, String value) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setAttribute");
  }

  @Override
  public void removeAttribute(String name) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "removeAttribute");
  }

  @Override
  public Attr getAttributeNode(String name) {
    return (Attr) getAttributes().getNamedItem(name);
  }

  @Override
  public Attr setAttributeNode(Attr newAttr) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setAttributeNode");
  }

  @Override
  public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "removeAttributeNode");
  }

  @Override
  public NodeList getElementsByTagName(String name) {
    // TODO(yshahun): Auto-generated method stub
    return null;
  }

  @Override
  public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
    String value = null;
    Node attribute = getAttributes().getNamedItemNS(namespaceURI, localName);
    if (attribute != null) {
      value = attribute.getNodeValue();
    }
    return (value == null) ? "" : value;
  }

  @Override
  public void setAttributeNS(String namespaceURI, String qualifiedName, String value)
      throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setAttributeNS");
  }

  @Override
  public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "removeAttributeNS");
  }

  @Override
  public Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
    return (Attr) getAttributes().getNamedItemNS(namespaceURI, localName);
  }

  @Override
  public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setAttributeNodeNS");
  }

  @Override
  public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
      throws DOMException {
    // TODO(yshahun): Auto-generated method stub
    return null;
  }

  @Override
  public boolean hasAttribute(String name) {
    return getAttributes().getNamedItem(name) != null;
  }

  @Override
  public boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {
    return getAttributes().getNamedItemNS(namespaceURI, localName) != null;
  }

  @Override
  public TypeInfo getSchemaTypeInfo() {
    throw new UnsupportedOperationException("getSchemaTypeInfo");
  }

  @Override
  public void setIdAttribute(String name, boolean isId) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setIdAttribute");
  }

  @Override
  public void setIdAttributeNS(String namespaceURI, String localName, boolean isId)
      throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setIdAttributeNS");
  }

  @Override
  public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setIdAttributeNode");
  }
}
