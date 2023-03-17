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

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

@Route("")
public class TimeSelectorDemo extends VerticalLayout {

    boolean twentyFour = true;

    public TimeSelectorDemo() {
        TimeSelector timeSelector = new TimeSelector();
        timeSelector.setTime(LocalTime.of(10, 25));

        timeSelector.addValueChangeListener(
                event -> System.out.println(event.getValue()));

        Button amPm = new Button("am/pm", clickEvent -> {
            twentyFour = !twentyFour;
            timeSelector.setTwentyFour(twentyFour);
            if (twentyFour) {
                clickEvent.getSource().setText("Switch to am/pm");
            } else {
                clickEvent.getSource().setText("Switch to twenty four");
            }
        });

        final CircleSelect circle = new CircleSelect();
        circle.setSectors(60);
        circle.setVisibleValues(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 0);
        circle.setValue(17);

        Span circle_text = new Span(
                "CircleSelect selection: " + circle.getValue());
        circle.addValueChangeListener(event -> circle_text.setText(
                "CircleSelect selection: " + circle.getValue()));

        CircleSelect theme = new CircleSelect();
        theme.setSectors(24);
        theme.setVisibleValues(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24);
        final Style style = theme.getElement().getStyle();
        style.set("--circle-inside", "GhostWhite");
        style.set("--circle-selector", "Indigo");
        style.set("--circle-selector-dot", "RebeccaPurple");
        style.set("--circle-text", "SpringGreen");

        add(new HorizontalLayout(circle, circle_text, theme));

        // Show it in the middle of the screen
        final VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.addClassName("demoContentLayout");
        contentLayout.setSizeFull();

        VerticalLayout content = new VerticalLayout();
        content.setSizeUndefined();
        content.setSpacing(true);
        contentLayout.add(content);
        content.add(amPm, timeSelector);

        HorizontalLayout circles = new HorizontalLayout();
        circles.setWidthFull();
        circles.setSpacing(true);
        circles.setId("circles-layout");
        Label l = new Label("CircleSelect as freestanding component");
        l.setFor(circles);

        final CircleSelect defaultCircle = new CircleSelect();
        defaultCircle.addValueChangeListener(e ->
                Notification.show("Selected from default: " + defaultCircle.getValue())
        );
        Label defaultLabel = new Label("Default settings");
        circles.add(new VerticalLayout(defaultCircle, defaultLabel));

        final CircleSelect minutes = new CircleSelect();
        minutes.setSectors(60);
        minutes.setVisibleValues(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 0);
        minutes.setValue(17);
        minutes.addValueChangeListener(e ->
                Notification.show("Selected from minutes: " + minutes.getValue())
        );
        Span minutesLabel = new Span("Sectors: 60\nValues: 5,10,15,20,25,30,35,40,45,50,55,0\nNote! 0 returns 60");
        circles.add(new VerticalLayout(minutes, minutesLabel));

        final CircleSelect fullDay = new CircleSelect();
        fullDay.setSectors(12);
        fullDay.setVisibleValues(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        fullDay.setInnerValues(13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24);
        fullDay.setValue(18);
        fullDay.addValueChangeListener(e -> Notification.show("Selected from full day: " + fullDay.getValue()));
        Span fullDayLabel = new Span("Sectors: 12\nValues: 1,2,3,4,5,6,7,8,9,10,11,0\nInner values: 13,14,15,16,17,18,19,20,21,22,23,24" +
                "\nNote! values are returned the same as the labels.");
        circles.add(new VerticalLayout(fullDay, fullDayLabel));

        final CircleSelect restricted = new CircleSelect();
        restricted.setVisibleValues(15, 30, 45, 0);
        restricted.setSectors(4);
        restricted.addValueChangeListener(e -> Notification.show("Selected from restricted: " + restricted.getValue()));

        Span restrictedLabel = new Span("Sectors: 4\nValues: 15,30,45,0");
        circles.add(new VerticalLayout(restricted, restrictedLabel));

        contentLayout.add(circles);
        contentLayout.setAlignItems( Alignment.CENTER);
        add(contentLayout);
    }
}
