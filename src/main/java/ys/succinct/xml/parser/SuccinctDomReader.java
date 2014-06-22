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

package ys.succinct.xml.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ys.succinct.util.IntBitSet;
import ys.succinct.xml.dom.DocumentContext;
import ys.succinct.xml.dom.ElementStore;
import ys.succinct.xml.dom.QNameStore;
import ys.succinct.xml.dom.SuccinctDom;
import ys.succinct.xml.dom.ValueStore;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * An object that builds the succinct {@link Document} using the underlying {@link XMLStreamReader}.
 *
 * @author Yauheni Shahun
 */
public class SuccinctDomReader {

  private final XMLStreamReader reader;
  private final String documentURI;
  private final boolean isNamespaceAware;

  private final IntBitSet parenthesisBits = new IntBitSet();
  private final IntBitSet nodeBits = new IntBitSet();
  private final IntBitSet attributeBits = new IntBitSet();
  private final ElementStore.Builder elementStoreBuilder = new ElementStore.Builder();
  private final ValueStore.Builder textStoreBuilder = new ValueStore.Builder();
  private final QNameStore.Builder attributeNameStoreBuilder = new QNameStore.Builder();
  private final ValueStore.Builder attributeValueStoreBuilder = new ValueStore.Builder();

  /**
   * Counter of the parentheses.
   */
  private int parenthesisCount;
  /**
   * Counter of the DOM nodes.
   */
  private int nodeCount;
  /**
   * Counter of bits in the attribute bit string.
   */
  private int attributeCount;

  private int documentIndex;
  private String inputEncoding;
  private String xmlEncoding;
  private String xmlVersion;
  private boolean isXmlStandalone;
  private String defaultNamespaceUri;

  /**
   * Constructs an XML reader that builds the succinct DOM.
   *
   * @param reader the underlying XML stream reader
   * @param URI the URI of the XML input
   * @param isNamespaceAware specifies whether the reader is aware of the namespaces
   */
  public SuccinctDomReader(XMLStreamReader reader, String URI, boolean isNamespaceAware) {
    this.reader = reader;
    this.documentURI = URI;
    this.isNamespaceAware = isNamespaceAware;
  }

  /**
   * Parses the encapsulated XML input to the succinct DOM.
   *
   * @return the succinct {@link Document} instance
   * @throws XMLStreamException if the underlying {@link XMLStreamReader} fails
   * @throws NodeLimitException if the number of nodes that the succinct DOM can handle is exceeded
   */
  public Document parse() throws XMLStreamException {
    handleEvent(reader.getEventType());
    while (reader.hasNext()) {
      handleEvent(reader.next());
    }

    DocumentContext documentContext = new DocumentContext(
        documentURI, inputEncoding, xmlEncoding, xmlVersion, isXmlStandalone);

    SuccinctDom dom = new SuccinctDom(
        parenthesisBits.toIntArray(parenthesisCount),
        parenthesisCount,
        nodeBits.toIntArray(nodeCount),
        nodeCount,
        attributeBits.toIntArray(attributeCount),
        attributeCount,
        elementStoreBuilder.build(),
        textStoreBuilder.build(),
        attributeNameStoreBuilder.build(),
        attributeValueStoreBuilder.build(),
        documentIndex,
        documentContext,
        defaultNamespaceUri,
        isNamespaceAware);

    return dom.getDocument();
  }

  /**
   * Handles the XML Stream event.
   */
  private void handleEvent(int event) {
    switch (event) {
      case XMLStreamConstants.START_ELEMENT:
        handleElementStart();
        break;
      case XMLStreamConstants.END_ELEMENT:
        handleElementEnd();
        break;
      case XMLStreamConstants.PROCESSING_INSTRUCTION:
        handleProcessingInstruction();
        break;
      case XMLStreamConstants.CHARACTERS:
        handleCharacters();
        break;
      case XMLStreamConstants.COMMENT:
        handleComment();
        break;
      case XMLStreamConstants.SPACE:
        // Skip ignorable whitespaces (the case happens with DTD).
        break;
      case XMLStreamConstants.START_DOCUMENT:
        handleDocumentStart();
        break;
      case XMLStreamConstants.END_DOCUMENT:
        handleDocumentEnd();
        break;
      case XMLStreamConstants.ENTITY_REFERENCE:
        // This case isn't expected as the entity references are resolved by default.
        break;
      case XMLStreamConstants.DTD:
        // TODO: Parse the content to the DocumentType node.
        break;
      case XMLStreamConstants.CDATA:
        handleCData();
        break;
      default:
        break;
    }
  }

  private void handleDocumentStart() {
    documentIndex = parenthesisCount;
    parenthesisBits.set(parenthesisCount); // Set the open parenthesis.
    countParenthesis();
    nodeBits.set(nodeCount++); // Set the non-text node.
    elementStoreBuilder.addNode(Node.DOCUMENT_NODE);
    attributeBits.set(attributeCount++); // No attributes.

    inputEncoding = reader.getEncoding();
    xmlEncoding = reader.getCharacterEncodingScheme();
    xmlVersion = reader.getVersion();
    isXmlStandalone = reader.isStandalone();
  }

  private void handleDocumentEnd() {
    countParenthesis(); // Count the closed parenthesis.
  }

  private void handleElementStart() {
    parenthesisBits.set(parenthesisCount); // Set the open parenthesis.
    countParenthesis();
    nodeBits.set(nodeCount++); // Set the non-text node.
    elementStoreBuilder.addNode(reader.getNamespaceURI(), nullOrNotEmpty(reader.getPrefix()),
        getLocalName(reader.getLocalName(), reader.getName()), Node.ELEMENT_NODE);

    attributeCount += reader.getAttributeCount();
    for (int i = 0; i < reader.getAttributeCount(); i++) {
      attributeNameStoreBuilder.addName(
          reader.getAttributeNamespace(i), nullOrNotEmpty(reader.getAttributePrefix(i)),
          getLocalName(reader.getAttributeLocalName(i), reader.getAttributeName(i)));
      attributeValueStoreBuilder.addValue(reader.getAttributeValue(i));
    }

    // Process the namespaces as regular attributes.
    attributeCount += reader.getNamespaceCount();
    for (int i = 0; i < reader.getNamespaceCount(); i++) {
      String namespaceUri = reader.getNamespaceURI(i);
      String prefix = reader.getNamespacePrefix(i);

      attributeNameStoreBuilder.addName(
          XMLConstants.XMLNS_ATTRIBUTE_NS_URI, getXmlnsPrefix(prefix), getXmlnsName(prefix));
      attributeValueStoreBuilder.addValue(namespaceUri);

      if (defaultNamespaceUri == null && (prefix == null || prefix.isEmpty())) {
        defaultNamespaceUri = namespaceUri;
      }
    }

    attributeBits.set(attributeCount++); // Set attributes separator.
  }

  private void handleElementEnd() {
    countParenthesis(); // Count the closed parenthesis.
  }

  private void handleCharacters() {
    parenthesisBits.set(parenthesisCount); // Set the open parenthesis.
    countParentheses(); // Count the open and closed parentheses.
    nodeCount++; // Count the text node.
    textStoreBuilder.addValue(
        reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
  }

  private void handleComment() {
    parenthesisBits.set(parenthesisCount); // Set the open parenthesis.
    countParentheses(); // Count the open and closed parentheses.
    nodeBits.set(nodeCount++); // Set the non-text node.
    elementStoreBuilder.addNode(Node.COMMENT_NODE);

    // Add a pseudo-attribute for the comment's text content.
    attributeCount++;
    attributeBits.set(attributeCount++);
    attributeNameStoreBuilder.addEmptyName();
    attributeValueStoreBuilder.addValue(
        reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
  }

  private void handleCData() {
    parenthesisBits.set(parenthesisCount); // Set the open parenthesis.
    countParentheses(); // Count the open and closed parentheses.
    nodeBits.set(nodeCount++); // Set the non-text node.
    elementStoreBuilder.addNode(Node.CDATA_SECTION_NODE);

    // Add a pseudo-attribute for the CDATA's text content.
    attributeCount++;
    attributeBits.set(attributeCount++);
    attributeNameStoreBuilder.addEmptyName();
    attributeValueStoreBuilder.addValue(
        reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
  }

  private void handleProcessingInstruction() {
    parenthesisBits.set(parenthesisCount); // Set the open parenthesis.
    countParentheses(); // Count the open and closed parentheses.
    nodeBits.set(nodeCount++); // Set the non-text node.
    elementStoreBuilder.addNode(Node.PROCESSING_INSTRUCTION_NODE);

    attributeCount += 2;
    attributeBits.set(attributeCount++);
    // Add the pseudo-attribute for the PI's target.
    attributeNameStoreBuilder.addEmptyName();
    attributeValueStoreBuilder.addValue(reader.getPITarget());
    // Add the pseudo-attribute for the PI's data.
    attributeNameStoreBuilder.addEmptyName();
    attributeValueStoreBuilder.addValue(reader.getPIData());
  }

  /**
   * Counts a parenthesis (open).
   *
   * @throws NodeLimitException if the number of parentheses that can be handled is exceeded
   */
  private void countParenthesis() {
    parenthesisCount++;
    if (parenthesisCount < 0) {
      throw new NodeLimitException();
    }
  }

  /**
   * Counts a pair of parentheses (open/closed).
   *
   * @throws NodeLimitException if the number of parentheses that can be handled is exceeded
   */
  private void countParentheses() {
    parenthesisCount += 2;
    if (parenthesisCount < 0) {
      throw new NodeLimitException();
    }
  }

  /**
   * Returns either the given string if it has non-zero length or {@code null} otherwise.
   */
  private static String nullOrNotEmpty(String s) {
    return s.isEmpty() ? null : s;
  }

  /**
   * Resolves the local name of the XML element. Returns {@code localName} itself if it's defined
   * explicitly (not null). Otherwise, returns the local part of {@code qName}.
   */
  private static String getLocalName(String localName, QName qName) {
    return (localName == null) ? qName.getLocalPart() : localName;
  }

  /**
   * Resolves the prefix for the namespace as an attribute. For example, it's {@code xmlns} in
   * {@code xmlns:p="http://example.com/price"}.
   */
  private static String getXmlnsPrefix(String prefix) {
    return (prefix != null) ? XMLConstants.XMLNS_ATTRIBUTE : null;
  }

  /**
   * Resolves the name for the namespace as an attribute. For example, it's {@code p} in
   * {@code xmlns:p="http://example.com/price"}, and {@code xmlns} by default.
   */
  private static String getXmlnsName(String prefix) {
    return (prefix == null) ? XMLConstants.XMLNS_ATTRIBUTE : prefix;
  }
}
