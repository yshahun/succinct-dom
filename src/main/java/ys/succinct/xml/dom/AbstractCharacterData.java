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

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;

/**
 * A succinct node that implements the common {@link CharacterData} API.
 *
 * @author Yauheni Shahun
 */
public abstract class AbstractCharacterData extends AbstractSuccinctNode implements CharacterData {

  public AbstractCharacterData(SuccinctDom dom, int index) {
    super(dom, index);
  }

  public AbstractCharacterData(SuccinctDom dom, int index, int ordinalIndex) {
    super(dom, index, ordinalIndex);
  }

  @Override
  public void setData(String data) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "setData");
  }

  @Override
  public void appendData(String arg) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "appendData");
  }

  @Override
  public void insertData(int offset, String arg) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "insertData");
  }

  @Override
  public void deleteData(int offset, int count) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "deleteData");
  }

  @Override
  public void replaceData(int offset, int count, String arg) throws DOMException {
    throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "replaceData");
  }
}
