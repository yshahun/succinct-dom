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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * A map-like collection of the attribute nodes of the element that are available lazily.
 *
 * @author Yauheni Shahun
 */
public class AttributeMapImpl implements NamedNodeMap {

  private final SuccinctDom dom;
  /**
   * The index of the first attribute of the element in the attribute bit string.
   */
  private final int firstAttributeIndex;
  /**
   * The number of attributes of the element.
   */
  private final int attributeCount;
  /**
   * The lazy collection of the attribute indexes mapped to the attribute names.
   */
  private Map<String, Integer> attributes;

  /**
   * Constructs an attribute map.
   *
   * @param dom the succinct DOM
   * @param firstAttributeIndex the index of the first attribute in the attribute bit string
   * @param attributeCount the number of attributes of the element
   */
  public AttributeMapImpl(SuccinctDom dom, int firstAttributeIndex, int attributeCount) {
    this.dom = dom;
    this.firstAttributeIndex = firstAttributeIndex;
    this.attributeCount = attributeCount;
  }

  @Override
  public Node getNamedItem(String name) {
    ensureAttributeMap();
    Integer attributeIndex = attributes.get(name);
    return (attributeIndex == null) ? null : new AttributeImpl(dom, attributeIndex);
  }

  @Override
  public Node setNamedItem(Node arg) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setNamedItem");
  }

  @Override
  public Node removeNamedItem(String name) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "removeNamedItem");
  }

  @Override
  public Node item(int index) {
    return new AttributeImpl(dom, firstAttributeIndex + index);
  }

  @Override
  public int getLength() {
    return attributeCount;
  }

  @Override
  public Node getNamedItemNS(String namespaceURI, String localName) throws DOMException {
    if (localName == null) {
      throw new IllegalArgumentException("localName is null.");
    }

    ensureAttributeMap();
    Integer attributeIndex = attributes.get(getNameKey(localName, namespaceURI));
    return (attributeIndex == null) ? null : new AttributeImpl(dom, attributeIndex);
  }

  @Override
  public Node setNamedItemNS(Node arg) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setNamedItemNS");
  }

  @Override
  public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "removeNamedItemNS");
  }

  /**
   * Ensures that the internal attribute map is built for the named access.
   */
  private void ensureAttributeMap() {
    if (attributes != null) {
      return;
    }

    attributes = new HashMap<>();
    int indexBoundary = firstAttributeIndex + attributeCount;
    for (int i = firstAttributeIndex; i < indexBoundary; i++) {
      attributes.put(dom.getAttributeQName(i), i); // Map to the qualified name.

      String namespaceURI = dom.getAttributeNamespaceURI(i);
      String localName = dom.getAttributeLocalName(i);
      if (namespaceURI != null && localName != null) {
        attributes.put(getNameKey(localName, namespaceURI), i); // Map to the namespace.
      }
    }
  }

  /**
   * Builds the key for the attribute that inludes its namespace.
   *
   * @param localName the local name of the attribute
   * @param namespaceURI the namespace URI of the attribute
   * @return the key value
   */
  private static String getNameKey(String localName, String namespaceURI) {
    return (namespaceURI == null) ? localName : namespaceURI + "-" + localName;
  }
}
