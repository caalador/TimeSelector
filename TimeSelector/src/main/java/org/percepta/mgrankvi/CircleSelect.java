/*
 * Copyright 2020 mgrankvi
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
package org.percepta.mgrankvi;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

import elemental.json.Json;
import elemental.json.JsonArray;

@Tag("circle-selector")
@JsModule("./CircleSelector.js")
public class CircleSelect
        extends AbstractSinglePropertyField<CircleSelect, Integer> {

    public CircleSelect() {
        super("actualSelection", 0, false);
    }

    public void setSectors(int i) {
        getElement().setProperty("sectors", Integer.toString(i));
        setNumSlices(i * 2);
    }

    public void setVisibleValues(int... values) {
        JsonArray array = Json.createArray();
        for (int i = 0; i < values.length; i++) {
            array.set(i, Integer.toString(values[i]));
        }
        getElement().setPropertyJson("values", array);
        getElement().setPropertyJson("numbers", Json.createArray());
    }

    public void setNumSlices(int slices) {
        getElement().setProperty("numSlices", Integer.toString(slices));
    }
}
