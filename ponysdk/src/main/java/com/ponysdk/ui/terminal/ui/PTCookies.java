/*
 * Copyright (c) 2011 PonySDK
 *  Owners:
 *  Luciano Broussal  <luciano.broussal AT gmail.com>
 *  Mathieu Barbier   <mathieu.barbier AT gmail.com>
 *  Nicolas Ciaravola <nicolas.ciaravola.pro AT gmail.com>
 *
 *  WebSite:
 *  http://code.google.com/p/pony-sdk/
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ponysdk.ui.terminal.ui;

import java.util.Date;

import com.google.gwt.user.client.Cookies;
import com.ponysdk.ui.model.ServerToClientModel;
import com.ponysdk.ui.terminal.model.BinaryModel;
import com.ponysdk.ui.terminal.model.ReaderBuffer;

public class PTCookies extends AbstractPTObject {

    @Override
    public boolean update(final ReaderBuffer buffer, final BinaryModel binaryModel) {
        if (ServerToClientModel.ADD_COOKIE.equals(binaryModel.getModel())) {
            final String name = binaryModel.getStringValue();
            // ServerToClientModel.VALUE
            final String value = buffer.getBinaryModel().getStringValue();

            final BinaryModel expire = buffer.getBinaryModel();
            if (ServerToClientModel.COOKIE_EXPIRE.equals(expire.getModel())) {
                final Date date = new Date(expire.getLongValue());
                Cookies.setCookie(name, value, date);
            } else {
                buffer.rewind(expire);
                Cookies.setCookie(name, value);
            }
            return true;
        }
        if (ServerToClientModel.REMOVE_COOKIE.equals(binaryModel.getModel())) {
            Cookies.removeCookie(binaryModel.getStringValue());
            return true;
        }
        return super.update(buffer, binaryModel);
    }

}