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

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.Notation;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

/**
 * A builder that produces the succinct {@link Document} from the XML input. The builder relies on
 * capabilities that the underlying StAX parser provides, therefore there are some restrictions.
 * <p>
 * The following {@link Node} types are not supported:
 * <ul>
 * <li>{@link DocumentType} (DTD isn't parsed)
 * <li>{@link Entity}
 * <li>{@link EntityReference} (entity references are resolved by default)
 * <li>{@link Notation} (DTD isn't parsed).
 * </ul>
 * <p>
 * The following handlers are not supported:
 * <ul>
 * <li>{@link EntityResolver}
 * <li>{@link ErrorHandler}.
 * </ul>
 * <p>
 * The succinct {@link Document} that the parser produces is read-only. This means that the mutation
 * and creation API isn't supported also.
 * <p>
 * The size of XML document that the builder can handle is restricted by the heap space and succinct
 * data structure limits. Practically, the number of nodes that the succinct DOM can support doesn't
 * exceed {@link Integer#MAX_VALUE} / 2.
 *
 * @author Yauheni Shahun
 */
public class SuccinctDocumentBuilder extends DocumentBuilder {

  private final XMLInputFactory factory;

  /**
   * Constructs a document builder.
   *
   * @param factory the underlying {@link XMLInputFactory}
   */
  protected SuccinctDocumentBuilder(XMLInputFactory factory) {
    this.factory = factory;
  }

  /**
   * @throws SAXException if the number of nodes that the succinct DOM can handle is exceeded
   */
  @Override
  public Document parse(InputSource is) throws SAXException, IOException {
    try {
      XMLStreamReader streamReader = factory.createXMLStreamReader(toSource(is));
      try {
        SuccinctDomReader domReader =
            new SuccinctDomReader(streamReader, is.getSystemId(), isNamespaceAware());
        return domReader.parse();
      } finally {
        streamReader.close();
      }
    } catch (NodeLimitException e) {
      throw new SAXException("Can't load the DOM: node limit is exceeded.");
    } catch (XMLStreamException e) {
      throw new SAXException(e.getMessage(), e);
    }
  }

  @Override
  public boolean isNamespaceAware() {
    return (boolean) factory.getProperty(XMLInputFactory.IS_NAMESPACE_AWARE);
  }

  @Override
  public boolean isValidating() {
    return (boolean) factory.getProperty(XMLInputFactory.IS_VALIDATING);
  }

  /**
   * Not supported.
   *
   * @throws UnsupportedOperationException
   */
  @Override
  public void setEntityResolver(EntityResolver er) {
    throw new UnsupportedOperationException("setEntityResolver");
  }

  /**
   * Not supported.
   *
   * @throws UnsupportedOperationException
   */
  @Override
  public void setErrorHandler(ErrorHandler eh) {
    throw new UnsupportedOperationException("setErrorHandler");
  }

  /**
   * Not supported.
   *
   * @throws UnsupportedOperationException
   */
  @Override
  public Document newDocument() {
    throw new UnsupportedOperationException("newDocument");
  }

  /**
   * Not supported.
   *
   * @throws UnsupportedOperationException
   */
  @Override
  public DOMImplementation getDOMImplementation() {
    throw new UnsupportedOperationException("getDOMImplementation");
  }

  /**
   * Transforms the given {@link InputSource} to the {@link Source}.
   */
  private static Source toSource(InputSource inputSource) {
    if (inputSource.getCharacterStream() != null) {
      return new StreamSource(inputSource.getCharacterStream(), inputSource.getSystemId());
    }
    if (inputSource.getByteStream() != null) {
      return new StreamSource(inputSource.getByteStream(), inputSource.getSystemId());
    }
    if (inputSource.getSystemId() != null) {
      return new StreamSource(inputSource.getSystemId());
    }
    throw new IllegalStateException("InputSource is empty.");
  }
}
