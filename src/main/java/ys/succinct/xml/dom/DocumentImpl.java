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
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * A succinct {@link Document} node.
 *
 * @author Yauheni Shahun
 */
public class DocumentImpl extends AbstractSuccinctNode implements Document {

  /**
   * Constructs a document node.
   *
   * @param dom the succinct DOM
   * @param index the index of the node in the balanced parentheses
   */
  public DocumentImpl(SuccinctDom dom, int index) {
    super(dom, index);
  }

  /*
   * Node API.
   */

  @Override
  public String getNodeName() {
    return "#document";
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
    return DOCUMENT_NODE;
  }

  @Override
  public Node getParentNode() {
    return null;
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
  public Node getNextSibling() {
    return null;
  }

  @Override
  public Node getPreviousSibling() {
    return null;
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
  public NamedNodeMap getAttributes() {
    return null;
  }

  @Override
  public Document getOwnerDocument() {
    return null;
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
    return null;
  }

  @Override
  public void setTextContent(String textContent) throws DOMException {
    // No op.
  }

  /*
   * Document API.
   */

  @Override
  public DocumentType getDoctype() {
    return null; // Not supported.
  }

  @Override
  public DOMImplementation getImplementation() {
    return null; // Not supported.
  }

  @Override
  public Element getDocumentElement() {
    return (Element) dom.getNode(dom.getDocumentElementIndex());
  }

  @Override
  public Element createElement(String tagName) throws DOMException {
    throw new UnsupportedOperationException("createElement");
  }

  @Override
  public DocumentFragment createDocumentFragment() {
    throw new UnsupportedOperationException("createDocumentFragment");
  }

  @Override
  public Text createTextNode(String data) {
    throw new UnsupportedOperationException("createTextNode");
  }

  @Override
  public Comment createComment(String data) {
    throw new UnsupportedOperationException("createComment");
  }

  @Override
  public CDATASection createCDATASection(String data) throws DOMException {
    throw new UnsupportedOperationException("createCDATASection");
  }

  @Override
  public ProcessingInstruction createProcessingInstruction(String target, String data)
      throws DOMException {
    throw new UnsupportedOperationException("createProcessingInstruction");
  }

  @Override
  public Attr createAttribute(String name) throws DOMException {
    throw new UnsupportedOperationException("createAttribute");
  }

  @Override
  public EntityReference createEntityReference(String name) throws DOMException {
    throw new UnsupportedOperationException("createEntityReference");
  }

  @Override
  public NodeList getElementsByTagName(String tagname) {
    // TODO(yshahun): Auto-generated method stub
    return null;
  }

  @Override
  public Node importNode(Node importedNode, boolean deep) throws DOMException {
    throw new UnsupportedOperationException("importNode");
  }

  @Override
  public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
    throw new UnsupportedOperationException("createElementNS");
  }

  @Override
  public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
    throw new UnsupportedOperationException("createAttributeNS");
  }

  @Override
  public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
    // TODO(yshahun): Auto-generated method stub
    return null;
  }

  @Override
  public Element getElementById(String elementId) {
    // TODO(yshahun): XML Stream API doesn't resolve the schema types properly.
    return null;
  }

  @Override
  public String getInputEncoding() {
    return dom.getDocumentContext().getInputEncoding();
  }

  @Override
  public String getXmlEncoding() {
    return dom.getDocumentContext().getXmlEncoding();
  }

  @Override
  public boolean getXmlStandalone() {
    return dom.getDocumentContext().isXmlStandalone();
  }

  @Override
  public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
    throw new UnsupportedOperationException("setXmlStandalone");
  }

  @Override
  public String getXmlVersion() {
    return dom.getDocumentContext().getXmlVersion();
  }

  @Override
  public void setXmlVersion(String xmlVersion) throws DOMException {
    throw new UnsupportedOperationException("setXmlVersion");
  }

  @Override
  public boolean getStrictErrorChecking() {
    return true;
  }

  @Override
  public void setStrictErrorChecking(boolean strictErrorChecking) {
    throw new UnsupportedOperationException("setStrictErrorChecking");
  }

  @Override
  public String getDocumentURI() {
    return dom.getDocumentContext().getURI();
  }

  @Override
  public void setDocumentURI(String documentURI) {
    throw new UnsupportedOperationException("setDocumentURI");
  }

  @Override
  public Node adoptNode(Node source) throws DOMException {
    throw new UnsupportedOperationException("adoptNode");
  }

  @Override
  public DOMConfiguration getDomConfig() {
    throw new UnsupportedOperationException("getDomConfig");
  }

  @Override
  public void normalizeDocument() {
    throw new UnsupportedOperationException("normalizeDocument");
  }

  @Override
  public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
    throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "renameNode");
  }
}
