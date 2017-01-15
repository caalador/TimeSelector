package org.percepta.mgrankvi.client.embedded;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.*;
import org.percepta.mgrankvi.client.Target;
import org.percepta.mgrankvi.client.circle.select.CircleSelect;
import org.percepta.mgrankvi.client.circle.select.CircleSelectCallback;

import java.util.Arrays;
import java.util.List;

public class TimeSelectorEmbeddedWidget extends SimpleLayoutPanel implements CircleSelectCallback {

    private static final String CLASS_NAME = "TimeSelector";
    private WidgetCallback callback;

    private boolean twentyFour = false;
    private int minuteSectors;
    private Integer[] visibleMinutes;
    private final Integer[] hourValues = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private final Integer[] twentyFourHourValues = new Integer[]{13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 00};
    private NumberFormat format = NumberFormat.getFormat("00");

    private Button amButton;
    private Button pmButton;
    private Label hourLabel;
    private Label minuteLabel;

    private HorizontalPanel topPanel;

    private Target selectionMode = Target.HOURS;
    private Target half = Target.AM;

    private int hourSelection = 12;
    private int minuteSelection = 0;

    private CircleSelect clock;
    private VerticalPanel amToPm;

    public TimeSelectorEmbeddedWidget(String fillColor, String selectorColor, int clockSize) {
        VerticalPanel content = new VerticalPanel();
        content.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        add(content);
        setStyleName("c-" + CLASS_NAME + "-embedded");

        topPanel = new HorizontalPanel();
        topPanel.setWidth("100%");
        topPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        topPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        topPanel.setStyleName("selector-embedded-top");

        createDigitalClockView();
        createAmPmSwitch();

        clock = new CircleSelect(fillColor, selectorColor, clockSize, this, hourValues);

        content.add(topPanel);
        content.add(clock);
    }

    private void createDigitalClockView() {
        hourLabel = new Label("12");
        hourLabel.setStyleName("selector-number");
        hourLabel.setHeight("100%");
        hourLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                setClockFaceToHours();
            }
        });
        minuteLabel = new Label("00");
        minuteLabel.setStyleName("selector-number");
        minuteLabel.setHeight("100%");
        minuteLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                setClockFaceToMinutes();
            }
        });

        Label divider = new Label(":");
        divider.setHeight("100%");
        divider.setStyleName("selector-number");

        hourLabel.addStyleName("selected");

        topPanel.add(hourLabel);
        topPanel.add(divider);
        topPanel.add(minuteLabel);
    }

    private void createAmPmSwitch() {
        amToPm = new VerticalPanel();
        amToPm.setWidth("55px");
        amToPm.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        amToPm.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        amButton = new Button("AM");
        amButton.setStyleName("day-time");
        amButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                half = Target.AM;
                amButton.addStyleName("selected");
                pmButton.removeStyleName("selected");
                callback.setTarget(half);
            }
        });
        pmButton = new Button("PM");
        pmButton.setStyleName("day-time");
        pmButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                half = Target.PM;
                pmButton.addStyleName("selected");
                amButton.removeStyleName("selected");
                callback.setTarget(half);
            }
        });

        amButton.addStyleName("selected");

        amToPm.add(amButton);
        amToPm.add(pmButton);
        topPanel.add(amToPm);
        topPanel.setCellWidth(amToPm, "55px");
    }

    public void setMinuteSectors(int minuteSectors) {
        this.minuteSectors = minuteSectors;
    }

    public void setVisibleMinutes(Integer[] visibleMinutes) {
        this.visibleMinutes = visibleMinutes;
    }

    private void setClockFaceToHours() {
        selectionMode = Target.HOURS;
        hourLabel.addStyleName("selected");
        minuteLabel.removeStyleName("selected");

        clock.setValues(hourValues);
        if (twentyFour) {
            clock.setInnerValues(twentyFourHourValues);
        }
        clock.setSelection(hourSelection);
        clock.setSectors(12);
    }

    private void setClockFaceToMinutes() {
        selectionMode = Target.MINUTES;
        minuteLabel.addStyleName("selected");
        hourLabel.removeStyleName("selected");

        List<Integer> visibleList = Arrays.asList(visibleMinutes);
        clock.setValues(visibleList);
        if (visibleList.contains(minuteSelection) && visibleList.size() == minuteSectors) {
            clock.setSelection(visibleList.indexOf(minuteSelection) + 1);
        } else {
            clock.setSelection(minuteSelection);
        }
        clock.setSectors(minuteSectors);
    }

    public void setClockMode(boolean twentyFour) {
        this.twentyFour = twentyFour;
        amToPm.setVisible(!this.twentyFour);
        if (this.twentyFour)
            topPanel.setCellWidth(amToPm, "0px");
        else
            topPanel.setCellWidth(amToPm, "55px");
        setClockFaceToHours();
    }

    public void setClockSize(int size) {
        clock.setSize(size);
        clock.refresh();
    }

    @Override
    public void valueSelection(Integer timeValue) {
        if (selectionMode.equals(Target.HOURS)) {
            hourSelection = timeValue;
            hourLabel.setText(format.format(timeValue));
            setClockFaceToMinutes();
        } else {
            minuteSelection = timeValue;
            minuteLabel.setText(format.format(timeValue % 60));
            setClockFaceToHours();
        }
        setTimeSelection(hourSelection, minuteSelection);
    }

    @Override
    public void valueHover(Integer timeValue) {
        if (selectionMode.equals(Target.HOURS)) {
            hourLabel.setText(format.format(timeValue));
        } else {
            minuteLabel.setText(format.format(timeValue % 60));
        }
    }

    @Override
    public void mouseOutEvent() {
        updateClockLabels();
        updateClockFace();
    }

    private void updateClockFace() {
        if (selectionMode.equals(Target.HOURS)) {
            setClockFaceToHours();
        } else {
            setClockFaceToMinutes();
        }
    }

    private void updateClockLabels() {
        hourLabel.setText(NumberFormat.getFormat("00").format(hourSelection));
        minuteLabel.setText(NumberFormat.getFormat("00").format(minuteSelection));
    }

    public void setTimeSelection(int hour, int minute) {
        hourSelection = hour;
        minuteSelection = minute;
        updateClockLabels();
        updateClockFace();
        callback.timeSelected(hour, minute);
    }

    public void addCallback(WidgetCallback callback) {
        this.callback = callback;
    }

    public void setHourSelection(int hour) {
        hourSelection = hour;
    }

    public void setMinuteSelection(int minutes) {
        minuteSelection = minutes;
    }

    public void setSelectorColor(String color) {
        clock.setSelectorColor(color);
    }

    public void setFillColor(String color) {
        clock.setFillColor(color);
    }
}
