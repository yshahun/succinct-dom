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

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ys.succinct.util.BalancedParentheses;
import ys.succinct.util.BitVector;
import ys.succinct.util.IntBitSet;
import ys.succinct.util.RangeTreeParentheses;
import ys.succinct.util.SamplingBitVector;

/**
 * An object that contains all the succinct data structures to support DOM API. It's designed to
 * have the only instance of the succinct DOM per an XML document.
 *
 * @author Yauheni Shahun
 */
public class SuccinctDom {

  /**
   * The balanced parentheses representation (length 2N) of the DOM tree. It's used for navigation
   * within the DOM i.e. to resolve the parent/child/sibling relationships.
   */
  private final BalancedParentheses parentheses;
  /**
   * Rank/Select structure for the balanced parentheses representation of the DOM tree. It's used to
   * get the ordinal indexes of the nodes in the secondary structures.
   */
  private final BitVector parenthesisVector;
  /**
   * Rank/Select structure for the node bit string (length N) in which 0 bits correspond to the text
   * nodes and 1 bits correspond to the other nodes. It's used to get ordinal indexes of the nodes
   * in the appropriate stores.
   */
  private final BitVector nodeVector;
  /**
   * Rank/Select structure for the bit string that is built by concatenating the attribute
   * substrings. Each substring represents the attributes of the particular node. It's built as a
   * sequence of 0 bits (rank of 0s gives the ordinal index of the attribute) followed by 1 bit that
   * serves as a separator and represents the corresponding node (rank of 1s gives the ordinal index
   * of the node). A node without attributes is represented by the single 1 bit.
   */
  private final BitVector attributeVector;
  /**
   * An ordered store of information (type, name) about the non-text nodes (elements, etc.).
   */
  private final ElementStore elementStore;
  /**
   * An ordered store of the content of the text nodes.
   */
  private final OrderedStore textStore;
  /**
   * An ordered store of the attribute qualified names.
   */
  private final QNameStore attributeNameStore;
  /**
   * An ordered store of the attribute values.
   */
  private final OrderedStore attributeValueStore;
  /**
   * The document properties.
   */
  private final DocumentContext documentContext;
  /**
   * The default namespace URI.
   */
  private final String defaultNamespace;
  /**
   * Indicates whether the DOM is aware of the namespaces.
   */
  private final boolean isNamespaceAware;
  /**
   * The cached {@link Document} succinct node.
   */
  private final DocumentImpl document;
  /**
   * The index of the document (root) element in the balanced parentheses.
   */
  private final int documentElementIndex;

  /**
   * Constructs the succinct DOM.
   */
  public SuccinctDom(int[] parenthesisBits, int parenthesisCount, int[] nodeBits, int nodeCount,
      int[] attributeBits, int attributeCount, ElementStore elementStore, OrderedStore textStore,
      QNameStore attributeNameStore, OrderedStore attributeValueStore, int documentIndex,
      DocumentContext documentContext, String defaultNamespace, boolean isNamespaceAware) {
    this.parentheses = new RangeTreeParentheses(parenthesisBits, parenthesisCount);
    this.parenthesisVector = new SamplingBitVector(parenthesisBits, parenthesisCount);
    this.nodeVector = new SamplingBitVector(nodeBits, nodeCount);
    this.attributeVector = new SamplingBitVector(attributeBits, attributeCount);
    this.elementStore = elementStore;
    this.textStore = textStore;
    this.attributeNameStore = attributeNameStore;
    this.attributeValueStore = attributeValueStore;
    this.documentContext = documentContext;
    this.defaultNamespace = defaultNamespace;
    this.isNamespaceAware = isNamespaceAware;
    this.document = new DocumentImpl(this, documentIndex);
    this.documentElementIndex = findDocumentElementIndex();
  }

  /**
   * Finds the index of the first element (root) among the {@link Document}'s child nodes.
   */
  private int findDocumentElementIndex() {
    int i = getFirstChildIndex(document.index);
    while (i != -1) {
      int ordinalIndex = parenthesisVector.rank(i) - 1;
      if (nodeVector.get(ordinalIndex)) {
        int elementIndex = nodeVector.rank(ordinalIndex) - 1;
        if (elementStore.getType(elementIndex) == Node.ELEMENT_NODE) {
          return i;
        }
      }
      i = getNextSiblingIndex(i);
    }
    throw new IllegalStateException("Document element is not found.");
  }

  /**
   * Returns the cached succinct {@link Document} instance.
   */
  public Document getDocument() {
    return document;
  }

  /**
   * Returns the index of the document's element (root).
   */
  public int getDocumentElementIndex() {
    return documentElementIndex;
  }

  /**
   * Returns the document properties.
   */
  public DocumentContext getDocumentContext() {
    return documentContext;
  }

  /**
   * Gets an instance of the node.
   *
   * @param parenthesisIndex the index of the node in the balanced parentheses
   * @return the succinct subclass of the {@link Node}
   */
  public Node getNode(int parenthesisIndex) {
    int ordinalIndex = parenthesisVector.rank(parenthesisIndex) - 1;
    if (nodeVector.get(ordinalIndex)) { // it's non-text element
      int elementIndex = nodeVector.rank(ordinalIndex) - 1;
      switch (elementStore.getType(elementIndex)) {
        case Node.ELEMENT_NODE:
          return new ElementImpl(this, parenthesisIndex, elementIndex);
        case Node.PROCESSING_INSTRUCTION_NODE:
          return new ProcessingInstructionImpl(this, parenthesisIndex, elementIndex);
        case Node.CDATA_SECTION_NODE:
          return new CDataImpl(this, parenthesisIndex, elementIndex);
        case Node.COMMENT_NODE:
          return new CommentImpl(this, parenthesisIndex, elementIndex);
        case Node.DOCUMENT_NODE:
          return document;
        default:
          throw new IllegalStateException("Unsupported node type.");
      }
    } else {
      return new TextImpl(this, parenthesisIndex, nodeVector.rank0(ordinalIndex) - 1);
    }
  }

  /**
   * Gets the parent of the given node.
   *
   * @param parenthesisIndex the index of the node in the balanced parentheses
   * @return the parent {@link Node} or {@code null} if there is no one
   */
  public Node getParentNode(int parenthesisIndex) {
    int parentIndex = parentheses.enclose(parenthesisIndex);
    return (parentIndex != -1) ? getNode(parentIndex) : null;
  }

  /**
   * Gets the first child of the given node.
   *
   * @param parenthesisIndex the index of the node in the balanced parentheses
   * @return the child {@link Node} or {@code null} if there is no one
   */
  public Node getFirstChild(int parenthesisIndex) {
    int nextIndex = parenthesisIndex + 1;
    if (parenthesisVector.get(nextIndex)) {
      return getNode(nextIndex);
    } else {
      return null;
    }
  }

  /**
   * Gets the index of the first child of the given node.
   *
   * @param parenthesisIndex the index of the node in the balanced parentheses
   * @return the index of the child node or {@code -1} if there is no one
   */
  private int getFirstChildIndex(int parenthesisIndex) {
    int nextIndex = parenthesisIndex + 1;
    if (parenthesisVector.get(nextIndex)) {
      return nextIndex;
    } else {
      return -1;
    }
  }

  /**
   * Gets the last child of the given node.
   *
   * @param parenthesisIndex the index of the node in the balanced parentheses
   * @return the child {@link Node} or {@code null} if there is no one
   */
  public Node getLastChild(int parenthesisIndex) {
    if (parenthesisVector.get(parenthesisIndex + 1)) { // if there are children
      int closeIndex = parentheses.findClose(parenthesisIndex);
      int lastChildIndex = parentheses.findOpen(closeIndex - 1);
      return getNode(lastChildIndex);
    } else {
      return null;
    }
  }

  /**
   * Gets the next sibling of the given node.
   *
   * @param parenthesisIndex the index of the node in the balanced parentheses
   * @return the next sibling {@link Node} or {@code null} if there is no one
   */
  public Node getNextSibling(int parenthesisIndex) {
    int nextIndex = parentheses.findClose(parenthesisIndex) + 1;
    // No range check is required as long as there is the document node.
    if (parenthesisVector.get(nextIndex)) {
      return getNode(nextIndex);
    } else {
      return null;
    }
  }

  /**
   * Gets the index of the next sibling of the given node.
   *
   * @param parenthesisIndex the index of the node in the balanced parentheses
   * @return the index of the next sibling or {@code -1} if there is no one
   */
  private int getNextSiblingIndex(int parenthesisIndex) {
    int nextIndex = parentheses.findClose(parenthesisIndex) + 1;
    // No range check is required as long as there is the document node.
    if (parenthesisVector.get(nextIndex)) {
      return nextIndex;
    } else {
      return -1;
    }
  }

  /**
   * Gets the previous sibling of the given node.
   *
   * @param parenthesisIndex the index of the node in the balanced parentheses
   * @return the previous sibling {@link Node} or {@code null} if there is no one
   */
  public Node getPreviousSibling(int parenthesisIndex) {
    int previousIndex = parenthesisIndex - 1;
    if (!parenthesisVector.get(previousIndex)) {
      previousIndex = parentheses.findOpen(previousIndex);
      return getNode(previousIndex);
    } else {
      return null;
    }
  }

  /**
   * Checks whether the given node has the children.
   *
   * @param parenthesisIndex the index of the node in the balanced parentheses
   * @return {@code true} if the node has the children, otherwise {@code false}
   */
  public boolean hasChildNodes(int parenthesisIndex) {
    return parenthesisVector.get(parenthesisIndex + 1);
  }

  /**
   * Gets the ordered collection of the children of the given node. In case the node has no children
   * the empty collection is returned.
   *
   * @param parenthesisIndex the index of the node in the balanced parentheses
   * @return the {@link NodeList} instance
   */
  public NodeList getChildNodes(int parenthesisIndex) {
    IntBitSet bits = new IntBitSet();

    int count = 0;
    int i = getFirstChildIndex(parenthesisIndex);
    while (i != -1) {
      bits.setInt(count++, i);
      i = getNextSiblingIndex(i);
    }

    return new NodeListImpl(this, bits.toIntArray(count * 32));
  }

  /**
   * Gets the local name of the node (element).
   *
   * @param ordinalIndex the index of the node (element) in the non-text node store
   * @return the local name if the DOM is aware of the namespaces, otherwise {@code null}
   */
  public String getLocalName(int ordinalIndex) {
    return isNamespaceAware ? elementStore.getName(ordinalIndex) : null;
  }

  /**
   * Gets the qualified name of the node (element).
   *
   * @param ordinalIndex the index of the node (element) in the non-text node store
   * @return the qualified name if the DOM is aware of the namespaces, otherwise the local name
   */
  public String getQName(int ordinalIndex) {
    return elementStore.getQName(ordinalIndex);
  }

  /**
   * Gets the namespace URI of the node (element).
   *
   * @param ordinalIndex the index of the node (element) in the non-text node store
   * @return the namespace URI if the DOM is aware of the namespaces, otherwise {@code null}
   */
  public String getNamespaceURI(int ordinalIndex) {
    return isNamespaceAware ? elementStore.getNamespaceUri(ordinalIndex) : null;
  }

  /**
   * Gets the namespace prefix of the node (element).
   *
   * @param ordinalIndex the index of the node (element) in the non-text node store
   * @return the namespace prefix if the DOM is aware of the namespaces, otherwise {@code null}
   */
  public String getPrefix(int ordinalIndex) {
    // If the DOM isn't aware of the namespaces, the prefixes are null in the store.
    return elementStore.getPrefix(ordinalIndex);
  }

  /**
   * Checks whether the given namespace is default.
   *
   * @param namespaceURI the namespace URI
   * @return {@code true} if the namespace URI is equal to the default URI, otherwise {@code false}
   */
  public boolean isDefaultNamespace(String namespaceURI) {
    if (namespaceURI == null || namespaceURI.isEmpty()) {
      return false;
    }
    return namespaceURI.equals(defaultNamespace);
  }

  /**
   * Gets the character content of the text node.
   *
   * @param ordinalIndex the index of the node among all the text nodes
   * @return the string value
   */
  public String getText(int ordinalIndex) {
    return textStore.getString(ordinalIndex);
  }

  /**
   * Gets the length of the character content of the text node.
   *
   * @param ordinalIndex the index of the node among all the text nodes
   * @return the length of the text
   */
  public int getTextLength(int ordinalIndex) {
    return textStore.getLength(ordinalIndex);
  }

  /**
   * Gets the substring of the character content of the text node.
   *
   * @param ordinalIndex the index of the node among all the text nodes
   * @param offset the position within the content that the substring starts from
   * @param count the length of the substring
   * @return the substring value, or {@code null} if the offset is out of bound
   */
  public String getTextSubstring(int ordinalIndex, int offset, int count) {
    return textStore.getSubstring(ordinalIndex, offset, count);
  }

  /**
   * Gets the pseudo-attribute value of the non-text node (i.e. the CDATA, Comment, Processing
   * Instruction).
   *
   * @param ordinalIndex the index of the node among all the non-text nodes
   * @param reverseOffset the offset in the reverse direction in the attribute bit string that
   *        identifies the attribute
   * @return the string value
   */
  public String getPseudoAttribute(int ordinalIndex, int reverseOffset) {
    int attributeIndex = attributeVector.select(ordinalIndex);
    int attributeOrdinalIndex = attributeVector.rank0(attributeIndex - reverseOffset) - 1;
    return attributeValueStore.getString(attributeOrdinalIndex);
  }

  /**
   * Gets the length of the pseudo-attribute value of the non-text node.
   *
   * @param ordinalIndex the index of the node among all the non-text nodes
   * @param reverseOffset the offset in the reverse direction in the attribute bit string that
   *        identifies the attribute
   * @return the length of the attribute value
   */
  public int getPseudoAttributeLength(int ordinalIndex, int reverseOffset) {
    int attributeIndex = attributeVector.select(ordinalIndex);
    int attributeOrdinalIndex = attributeVector.rank0(attributeIndex - reverseOffset) - 1;
    return attributeValueStore.getLength(attributeOrdinalIndex);
  }

  /**
   * Gets the substring of the pseudo-attribute value of the non-text node.
   *
   * @param ordinalIndex the index of the node among all the non-text nodes
   * @param reverseOffset the offset in the reverse direction in the attribute bit string that
   *        identifies the attribute
   * @param offset the position within the content that the substring starts from
   * @param count the length of the substring
   * @return the substring value, or {@code null} if the offset is out of bound
   */
  public String getPseudoAttributeSubstring(
      int ordinalIndex, int reverseOffset, int offset, int count) {
    int attributeIndex = attributeVector.select(ordinalIndex);
    int attributeOrdinalIndex = attributeVector.rank0(attributeIndex - reverseOffset) - 1;
    return attributeValueStore.getSubstring(attributeOrdinalIndex, offset, count);
  }

  /**
   * Gets the text content of the given node and its descendants.
   *
   * @param nodeIndex the index of the node in the balanced parentheses
   * @return the string value
   *
   * @see Node#getTextContent()
   */
  public String getTextContent(int nodeIndex) {
    StringBuilder builder = new StringBuilder();
    buildTextContent(nodeIndex, builder);
    return builder.toString();
  }

  /**
   * Builds the text content of the node and its descendants recursively.
   *
   * @param nodeIndex the index of the node in the balanced parentheses
   * @param builder the object that accumulates the character content from the nodes
   */
  private void buildTextContent(int nodeIndex, StringBuilder builder) {
    int nodeOrdinalIndex = parenthesisVector.rank(nodeIndex) - 1;
    if (nodeVector.get(nodeOrdinalIndex)) { // non-text node
      int elementIndex = nodeVector.rank(nodeOrdinalIndex) - 1;
      int nodeType = elementStore.getType(elementIndex);
      switch (nodeType) {
        case Node.ELEMENT_NODE:
          int childIndex = getFirstChildIndex(nodeIndex);
          while (childIndex != -1) {
            buildTextContent(childIndex, builder);
            childIndex = getNextSiblingIndex(childIndex);
          }
          break;
        case Node.CDATA_SECTION_NODE:
          int attributeIndex = attributeVector.select(elementIndex);
          int attributeOrdinalIndex = attributeVector.rank0(attributeIndex) - 1;
          builder.append(attributeValueStore.getString(attributeOrdinalIndex));
          break;
        case Node.COMMENT_NODE:
        case Node.PROCESSING_INSTRUCTION_NODE:
          // Comments and Processing Instructions are ignored.
          break;
        default:
          throw new IllegalStateException("Unsupported node type: " + nodeType);
      }
    } else { // text node
      int textIndex = nodeVector.rank0(nodeOrdinalIndex) - 1;
      builder.append(textStore.getString(textIndex));
    }
  }

  /**
   * Checks whether the given element has the attributes.
   *
   * @param ordinalIndex the index of the node among the non-text nodes
   * @return {@code true} if the element has the attributes, otherwise {@code false}
   */
  public boolean hasAttributes(int ordinalIndex) {
    int position = attributeVector.select(ordinalIndex);
    return (position == 0) ? false : !attributeVector.get(position - 1);
  }

  /**
   * Gets the collection of the attribute nodes of the element. In case the element has no
   * attributes the empty collection is returned.
   *
   * @param ordinalIndex the index of the node among the non-text nodes
   * @return the {@link NamedNodeMap} instance
   */
  public NamedNodeMap getAttributes(int ordinalIndex) {
    int position = attributeVector.select(ordinalIndex);
    int firstAttributeIndex = 0;
    if (ordinalIndex > 0) {
      firstAttributeIndex = attributeVector.select(ordinalIndex - 1) + 1;
    }
    return new AttributeMapImpl(this, firstAttributeIndex, position - firstAttributeIndex);
  }

  /*
   * Attribute methods.
   */

  /**
   * Gets the local name of the attribute.
   *
   * @param attributeIndex the index of the attribute in the attribute bit string
   * @return the local name if the DOM is aware of the namespaces, otherwise {@code null}
   */
  public String getAttributeLocalName(int attributeIndex) {
    if (isNamespaceAware) {
      int ordinalIndex = attributeVector.rank0(attributeIndex) - 1;
      return attributeNameStore.getName(ordinalIndex);
    } else {
      return null;
    }
  }

  /**
   * Gets the qualified name of the attribute.
   *
   * @param attributeIndex the index of the attribute in the attribute bit string
   * @return the qualified name if the DOM is aware of the namespaces, otherwise the local name
   */
  public String getAttributeQName(int attributeIndex) {
    int ordinalIndex = attributeVector.rank0(attributeIndex) - 1;
    return attributeNameStore.getQName(ordinalIndex);
  }

  /**
   * Gets the namespace URI of the attribute.
   *
   * @param attributeIndex the index of the attribute in the attribute bit string
   * @return the namespace URI if the DOM is aware of the namespaces, otherwise {@code null}
   */
  public String getAttributeNamespaceURI(int attributeIndex) {
    if (isNamespaceAware) {
      int ordinalIndex = attributeVector.rank0(attributeIndex) - 1;
      return attributeNameStore.getNamespaceUri(ordinalIndex);
    } else {
      return null;
    }
  }

  /**
   * Gets the namespace prefix of the attribute.
   *
   * @param attributeIndex the index of the attribute in the attribute bit string
   * @return the namespace prefix if the DOM is aware of the namespaces, otherwise {@code null}
   */
  public String getAttributePrefix(int attributeIndex) {
    if (isNamespaceAware) {
      int ordinalIndex = attributeVector.rank0(attributeIndex) - 1;
      return attributeNameStore.getPrefix(ordinalIndex);
    } else {
      return null;
    }
  }

  /**
   * Gets the value of the attribute.
   *
   * @param attributeIndex the index of the attribute in the attribute bit string
   * @return the string value
   */
  public String getAttributeValue(int attributeIndex) {
    int ordinalIndex = attributeVector.rank0(attributeIndex) - 1;
    return attributeValueStore.getString(ordinalIndex);
  }

  /**
   * Gets the element that owns the attribute.
   *
   * @param attributeIndex the index of the attribute in the attribute bit string
   * @return the element {@link Node}
   */
  public Node getAttributeElement(int attributeIndex) {
    int elementIndex = attributeVector.rank(attributeIndex);
    int nodeOrdinalIndex = nodeVector.select(elementIndex);
    int nodeIndex = parenthesisVector.select(nodeOrdinalIndex);
    return new ElementImpl(this, nodeIndex, elementIndex);
  }
}
