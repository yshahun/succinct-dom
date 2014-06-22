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
 * A succinct {@link Attr} node. Internal {@code index} is treated as the index of the attribute in
 * the attribute bit string. {@code ordinalIndex} is not used.
 *
 * @author Yauheni Shahun
 */
public class AttributeImpl extends AbstractSuccinctNode implements Attr {

  /**
   * Constructs a succinct attribute node.
   *
   * @param dom the succinct DOM
   * @param index the index of the attribute in the attribute bit string
   */
  public AttributeImpl(SuccinctDom dom, int index) {
    super(dom, index);
  }

  /*
   * Node API.
   */

  @Override
  public String getNodeName() {
    return dom.getAttributeQName(index);
  }

  @Override
  public String getNodeValue() throws DOMException {
    return dom.getAttributeValue(index);
  }

  @Override
  public void setNodeValue(String nodeValue) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setNodeValue");
  }

  @Override
  public short getNodeType() {
    return Node.ATTRIBUTE_NODE;
  }

  @Override
  public Node getParentNode() {
    return null;
  }

  @Override
  public NodeList getChildNodes() {
    return new FixedNodeList(new AttributeTextImpl(dom, index, dom.getAttributeValue(index)));
  }

  @Override
  public Node getFirstChild() {
    return new AttributeTextImpl(dom, index, dom.getAttributeValue(index));
  }

  @Override
  public Node getLastChild() {
    return new AttributeTextImpl(dom, index, dom.getAttributeValue(index));
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
    // Presume that the attribute node has the value and so the child text node.
    return true;
  }

  @Override
  public String getNamespaceURI() {
    return dom.getAttributeNamespaceURI(index);
  }

  @Override
  public String getPrefix() {
    return dom.getAttributePrefix(index);
  }

  @Override
  public String getLocalName() {
    return dom.getAttributeLocalName(index);
  }

  @Override
  public boolean hasAttributes() {
    return false;
  }

  @Override
  public String getTextContent() throws DOMException {
    return dom.getAttributeValue(index);
  }

  @Override
  public void setTextContent(String textContent) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setTextContent");
  }

  /*
   * Attr API.
   */

  @Override
  public String getName() {
    return dom.getAttributeQName(index);
  }

  @Override
  public boolean getSpecified() {
    // Presume that the attribute node always has the value.
    return true;
  }

  @Override
  public String getValue() {
    return dom.getAttributeValue(index);
  }

  @Override
  public void setValue(String value) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setValue");
  }

  @Override
  public Element getOwnerElement() {
    return (Element) dom.getAttributeElement(index);
  }

  @Override
  public TypeInfo getSchemaTypeInfo() {
    throw new UnsupportedOperationException("getSchemaTypeInfo");
  }

  @Override
  public boolean isId() {
    // Can't detect proper attribute type using XML Stream API.
    return false;
  }
}
