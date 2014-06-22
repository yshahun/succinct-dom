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

/**
 * An object that holds the properties of the XML document like the XML declaration, URI, etc.
 *
 * @author Yauheni Shahun
 */
public class DocumentContext {

  private final String URI;
  private final String inputEncoding;
  private final String xmlEncoding;
  private final String xmlVersion;
  private final boolean isXmlStandalone;

  /**
   * Constructs a document context.
   *
   * @param inputEncoding the encoding of the XML input
   * @param xmlEncoding the XML encoding
   * @param xmlVersion the XML version
   * @param isXmlStandalone the flag that indicates if the XML depends on the external DTD
   */
  public DocumentContext(String URI, String inputEncoding, String xmlEncoding, String xmlVersion,
      boolean isXmlStandalone) {
    this.URI = URI;
    this.inputEncoding = inputEncoding;
    this.xmlEncoding = xmlEncoding;
    this.xmlVersion = xmlVersion;
    this.isXmlStandalone = isXmlStandalone;
  }

  /**
   * Returns the document URI or {@code null} if unknown.
   */
  public String getURI() {
    return URI;
  }

  /**
   * Returns the encoding of the XML input.
   */
  public String getInputEncoding() {
    return inputEncoding;
  }

  /**
   * Returns the encoding of the XML document.
   */
  public String getXmlEncoding() {
    return xmlEncoding;
  }

  /**
   * Returns the XML version string, e.g. "1.0".
   */
  public String getXmlVersion() {
    return xmlVersion;
  }

  /**
   * Indicates whether the XML document depends on the external subset of the DTD.
   *
   * @return {@code true} if the XML doesn't depend on the external entities, otherwise
   *         {@code false}
   */
  public boolean isXmlStandalone() {
    return isXmlStandalone;
  }
}
