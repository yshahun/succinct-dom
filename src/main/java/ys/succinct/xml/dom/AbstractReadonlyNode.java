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
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

/**
 * An abstract node that implements {@link Node} API that is common to the read-only nodes of the
 * succinct DOM. This implementation takes care of the mutating methods mostly that are simply not
 * supported.
 *
 * @author Yauheni Shahun
 */
public abstract class AbstractReadonlyNode implements Node {

  /*
   * DOM Level 1.
   */

  @Override
  public Node appendChild(Node newChild) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "appendChild");
  }

  @Override
  public Node insertBefore(Node newChild, Node refChild) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "insertBefore");
  }

  @Override
  public Node removeChild(Node oldChild) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "removeChild");
  }

  @Override
  public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "replaceChild");
  }

  @Override
  public Node cloneNode(boolean deep) {
    throw new UnsupportedOperationException("cloneNode");
  }

  @Override
  public void normalize() {
    throw new UnsupportedOperationException("normalize");
  }

  /*
   * DOM Level 2.
   */

  @Override
  public boolean isSupported(String feature, String version) {
    return false; // No supported features.
  }

  @Override
  public void setPrefix(String prefix) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setPrefix");
  }

  /*
   * DOM Level 3.
   */

  @Override
  public short compareDocumentPosition(Node other) throws DOMException {
    throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "compareDocumentPosition");
  }

  @Override
  public String getBaseURI() {
    return null; // Not supported.
  }

  @Override
  public Object getFeature(String feature, String version) {
    return null; // Not supported.
  }

  /**
   * Tests whether the base properties of this node and the given node are equal.
   * <p>
   * Subclasses may enhance the logic to meet the specification requirements.

   * @see Node#isEqualNode(Node)
   */
  @Override
  public boolean isEqualNode(Node other) {
    if (other == null) {
      return false;
    }
    if (getNodeType() != other.getNodeType()) {
      return false;
    }
    if (!isEqual(getNodeName(), other.getNodeName())) {
      return false;
    }
    if (!isEqual(getLocalName(), other.getLocalName())) {
      return false;
    }
    if (!isEqual(getNamespaceURI(), other.getNamespaceURI())) {
      return false;
    }
    if (!isEqual(getPrefix(), other.getPrefix())) {
      return false;
    }
    if (!isEqual(getNodeValue(), other.getNodeValue())) {
      return false;
    }

    NodeList thisChildren = getChildNodes();
    NodeList otherChildren = other.getChildNodes();

    if (thisChildren != null || otherChildren != null) {
      if (thisChildren == null || otherChildren == null) {
        return false;
      }
      int length = thisChildren.getLength();
      if (length != otherChildren.getLength()) {
        return false;
      }
      for (int i = 0; i < length; i++) {
        if (!thisChildren.item(i).isEqualNode(otherChildren.item(i))) {
          return false;
        }
      }
    }

    return isEqualAttributes(other);
  }

  /**
   * Tests whether the attributes of this node and the given node are equal.
   */
  private boolean isEqualAttributes(Node other) {
    NamedNodeMap thisAttributes = getAttributes();
    NamedNodeMap otherAttributes = other.getAttributes();

    if (thisAttributes == null & otherAttributes == null) {
      return true;
    }
    if (thisAttributes == null || otherAttributes == null) {
      return false;
    }
    int length = thisAttributes.getLength();
    if (length != otherAttributes.getLength()) {
      return false;
    }

    for (int i = 0; i < length; i++) {
      Node thisAttribute = thisAttributes.item(i);
      Node otherAttribute = otherAttributes.getNamedItem(thisAttribute.getNodeName());
      if (!thisAttribute.isEqualNode(otherAttribute)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean isSameNode(Node other) {
    return equals(other);
  }

  @Override
  public String lookupNamespaceURI(String prefix) {
    throw new UnsupportedOperationException("lookupNamespaceURI");
  }

  @Override
  public String lookupPrefix(String namespaceURI) {
    throw new UnsupportedOperationException("lookupPrefix");
  }

  @Override
  public Object getUserData(String key) {
    throw new UnsupportedOperationException("getUserData");
  }

  @Override
  public Object setUserData(String key, Object data, UserDataHandler handler) {
    throw new UnsupportedOperationException("setUserData");
  }

  private static boolean isEqual(Object a, Object b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return a.equals(b);
  }
}
