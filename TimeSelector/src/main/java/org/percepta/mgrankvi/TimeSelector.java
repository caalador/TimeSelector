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

import java.time.LocalTime;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

/**
 * Create a TimeSelector element.
 * Value type is String, but the component works with LocalTime when using
 * it through the set/getTime api.
 */
@Tag("time-selector")
@JsModule("./TimeSelector.js")
public class TimeSelector
        extends AbstractSinglePropertyField<TimeSelector, String> {

    public TimeSelector() {
        super("value", LocalTime.now().toString(), false);
    }

    /**
     * Set the local time for the component.
     *
     * @param value
     *         time value as a LocalTime
     */
    public void setTime(LocalTime value) {
        setValue(value.toString());
    }

    public LocalTime getTime() {
        return LocalTime.parse(getValue());
    }

    public void setTwentyFour(boolean twentyFour) {
        getElement().setProperty("twentyFour", twentyFour);
    }
}
