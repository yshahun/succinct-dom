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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;

/**
 * A factory of parsers that produce the succinct DOM from the XML documents.
 * <p>
 * XML parsing is based on the default StAX implementation, therefore some restrictions are applied:
 * <ul>
 * <li>Setting validation has no effect as the StAX parser isn't validating
 * <li>No JAXP attribute is supported
 * <li>No feature is supported.
 * </ul>
 *
 * @see javax.xml.stream
 *
 * @author Yauheni Shahun
 */
public class SuccinctDocumentBuilderFactory extends DocumentBuilderFactory {

  private final XMLInputFactory factory;

  public SuccinctDocumentBuilderFactory() {
    factory = XMLInputFactory.newFactory();
    // DOM API doesn't support XML namespaces by default. Disable the feature in the StAX API.
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
    factory.setProperty("http://java.sun.com/xml/stream/properties/report-cdata-event", true);
  }

  @Override
  public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
    return new SuccinctDocumentBuilder(factory);
  }

  @Override
  public void setAttribute(String name, Object value) throws IllegalArgumentException {
    throw new IllegalArgumentException("No attribute is supported.");
  }

  @Override
  public Object getAttribute(String name) throws IllegalArgumentException {
    throw new IllegalArgumentException("No attribute is supported.");
  }

  @Override
  public void setFeature(String name, boolean value) throws ParserConfigurationException {
    throw new ParserConfigurationException("No feature is supported.");
  }

  @Override
  public boolean getFeature(String name) throws ParserConfigurationException {
    throw new ParserConfigurationException("No feature is supported.");
  }

  @Override
  public void setNamespaceAware(boolean awareness) {
    super.setNamespaceAware(awareness);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, awareness);
  }
}
