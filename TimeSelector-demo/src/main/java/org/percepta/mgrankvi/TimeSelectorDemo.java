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

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

@Route("")
public class TimeSelectorDemo extends VerticalLayout {

    public TimeSelectorDemo() {
        TimeSelector timeSelector = new TimeSelector();
        timeSelector.setTime(LocalTime.of(10, 25));

        timeSelector.addValueChangeListener(
                event -> System.out.println(event.getValue()));

        final CircleSelect circle = new CircleSelect();
        circle.setSectors(60);
        circle.setVisibleValues(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 0);
        circle.setValue(17);

        Span circle_text = new Span(
                "CircleSelect selection: " + circle.getValue());
        circle.addValueChangeListener(event -> circle_text.setText(
                "CircleSelect selection: " + circle.getValue()));

        add(timeSelector, new HorizontalLayout(circle, circle_text));

        CircleSelect theme = new CircleSelect();
        theme.setSectors(24);
        theme.setVisibleValues(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24);
        final Style style = theme.getElement().getStyle();
        style.set("--circle-inside", "GhostWhite");
        style.set("--circle-selector", "Indigo");
        style.set("--circle-selector-dot", "RebeccaPurple");
        style.set("--circle-text", "SpringGreen");

        add(theme);
    }
}
