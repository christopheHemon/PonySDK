/*
 * Copyright (c) 2011 PonySDK
 *  Owners:
 *  Luciano Broussal  <luciano.broussal AT gmail.com>
 *	Mathieu Barbier   <mathieu.barbier AT gmail.com>
 *	Nicolas Ciaravola <nicolas.ciaravola.pro AT gmail.com>
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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.UIObject;
import com.ponysdk.ui.terminal.UIService;
import com.ponysdk.ui.terminal.instruction.PTInstruction;
import com.ponysdk.ui.terminal.model.Model;

public abstract class PTUIObject<T extends UIObject> extends AbstractPTObject {

    private static final String FONT_SIZE = "fontSize";

    protected T uiObject;

    private Object nativeObject;

    protected void init(final PTInstruction create, final UIService uiService, final T uiObject) {
        if (this.uiObject != null) { throw new IllegalStateException("init may only be called once."); }
        this.uiObject = uiObject;
        if (create != null) {
            this.objectID = create.getObjectID();
            uiService.registerUIObject(this.objectID, uiObject);
        }
    }

    public T cast() {
        return uiObject;
    }

    @Override
    public void update(final PTInstruction update, final UIService uiService) {
        if (update.containsKey(Model.WIDGET_WIDTH)) {
            uiObject.setWidth(update.getString(Model.WIDGET_WIDTH));
        } else if (update.containsKey(Model.WIDGET_HEIGHT)) {
            uiObject.setHeight(update.getString(Model.WIDGET_HEIGHT));
        } else if (update.containsKey(Model.WIDGET_FONT_SIZE)) {// TODO nciaravola WIDGET_FONT_SIZE not used
            uiObject.getElement().getStyle().setProperty(FONT_SIZE, update.getString(Model.WIDGET_FONT_SIZE));
        } else if (update.containsKey(Model.PUT_PROPERTY_KEY)) {
            uiObject.getElement().setPropertyString(update.getString(Model.PUT_PROPERTY_KEY), update.getString(Model.PROPERTY_VALUE));
        } else if (update.containsKey(Model.PUT_ATTRIBUTE_KEY)) {
            uiObject.getElement().setAttribute(update.getString(Model.PUT_ATTRIBUTE_KEY), update.getString(Model.ATTRIBUTE_VALUE));
        } else if (update.containsKey(Model.REMOVE_ATTRIBUTE_KEY)) {
            uiObject.getElement().removeAttribute(update.getString(Model.REMOVE_ATTRIBUTE_KEY));
        } else if (update.containsKey(Model.STYLE_NAME)) {
            uiObject.setStyleName(update.getString(Model.STYLE_NAME));
        } else if (update.containsKey(Model.STYLE_PRIMARY_NAME)) {
            uiObject.setStylePrimaryName(update.getString(Model.STYLE_PRIMARY_NAME));
        } else if (update.containsKey(Model.ADD_STYLE_NAME)) {
            uiObject.addStyleName(update.getString(Model.ADD_STYLE_NAME));
        } else if (update.containsKey(Model.REMOVE_STYLE_NAME)) {
            uiObject.removeStyleName(update.getString(Model.REMOVE_STYLE_NAME));
        } else if (update.containsKey(Model.WIDGET_VISIBLE)) {
            uiObject.setVisible(update.getBoolean(Model.WIDGET_VISIBLE));
        } else if (update.containsKey(Model.ENSURE_DEBUG_ID)) {
            uiObject.ensureDebugId(update.getString(Model.ENSURE_DEBUG_ID));
        } else if (update.containsKey(Model.WIDGET_TITLE)) {
            uiObject.setTitle(update.getString(Model.WIDGET_TITLE));
        } else if (update.containsKey(Model.PUT_STYLE_KEY)) {
            uiObject.getElement().getStyle().setProperty(update.getString(Model.PUT_STYLE_KEY), update.getString(Model.STYLE_VALUE));
        } else if (update.containsKey(Model.REMOVE_STYLE_KEY)) {
            uiObject.getElement().getStyle().clearProperty(update.getString(Model.REMOVE_STYLE_KEY));
        } else if (update.containsKey(Model.BIND)) {
            nativeObject = bind(update.getString(Model.BIND), String.valueOf(objectID), uiObject.getElement());
        } else if (update.containsKey(Model.NATIVE)) {
            final JSONObject object = update.getObject(Model.NATIVE);
            sendToNative(String.valueOf(objectID), nativeObject, object.getJavaScriptObject());
        }
    }

    public UIObject asWidget(final int objectID, final UIService uiService) {
        if (uiService.getPTObject(objectID) instanceof PTUIObject) { return ((PTUIObject<?>) uiService.getPTObject(objectID)).cast(); }
        throw new IllegalStateException("This object is not an UIObject");
    }

    private native Object bind(String functionName, String objectID, Element element) /*-{
                                                                                      var self = this;
                                                                                      var o = $wnd[functionName](objectID, element);
                                                                                      return o;
                                                                                      }-*/;

    private native void sendToNative(String objectID, Object nativeObject, JavaScriptObject data) /*-{
                                                                                                  nativeObject.update(data);
                                                                                                  }-*/;

}
