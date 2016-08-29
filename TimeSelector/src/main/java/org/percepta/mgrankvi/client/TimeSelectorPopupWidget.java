package org.percepta.mgrankvi.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.percepta.mgrankvi.client.circle.select.CircleSelect;
import org.percepta.mgrankvi.client.circle.select.CircleSelectCallback;

public class TimeSelectorPopupWidget extends DecoratedPopupPanel implements CircleSelectCallback {

    public static final String CLASS_NAME = "TimeSelector";

    private final SelectionHandler selectionHandler;
    private boolean twentyFour = false;

    protected enum Target {HOURS, MINUTES, AM, PM}

    private NumberFormat format = NumberFormat.getFormat("00");

    private static RegExp sizePattern = RegExp.compile("^(-?\\d+(\\.\\d+)?)(%|px|em|ex|in|cm|mm|pt|pc)?$");

    private Button am, pm, cancel, ok;
    private Label hour, minute;

    private int width, height;

    private final VerticalPanel content;
    private HorizontalPanel top;

    private Target select = Target.HOURS;
    private Target half = Target.AM;

    private int hourSelection = 12;
    private int minuteSelection = 0;

    CircleSelect clock;
    VerticalPanel amToPm;

    public TimeSelectorPopupWidget(final SelectionHandler selectionHandler) {
        this.selectionHandler = selectionHandler;
        content = new VerticalPanel();

        // CSS class-name should not be v- prefixed
        setStyleName("c-" + CLASS_NAME + "-popup");
        add(content);
        top = new HorizontalPanel();
        top.setWidth("100%");
        top.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        top.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        content.add(top);
        content.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        top.setStyleName("selector-popup-top");

        hour = new Label("12");
        hour.setStyleName("selector-number");
        hour.setHeight("100%");
        hour.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                select = Target.HOURS;
                setHourSelection();
            }
        });
        minute = new Label("00");
        minute.setStyleName("selector-number");
        minute.setHeight("100%");
        minute.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                setMinuteSelection();
            }
        });
        Label divider = new Label(":");
        divider.setHeight("100%");
        divider.setStyleName("selector-number");

        // Default selection colors.
        hour.addStyleName("selected");

        top.add(hour);
        top.add(divider);
        top.add(minute);

        amToPm = new VerticalPanel();
        amToPm.setWidth("55px");
        amToPm.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        amToPm.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        am = new Button("AM");
        am.setStyleName("day-time");
        am.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                half = Target.AM;
                am.addStyleName("selected");
                pm.removeStyleName("selected");
                if (select.equals(Target.HOURS))
                    setHourSelection();
            }
        });
        pm = new Button("PM");
        pm.setStyleName("day-time");
        pm.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                half = Target.PM;
                pm.addStyleName("selected");
                am.removeStyleName("selected");
                if (select.equals(Target.HOURS))
                    setHourSelection();
            }
        });

        am.addStyleName("selected");

        amToPm.add(am);
        amToPm.add(pm);

        top.add(amToPm);
        top.setCellWidth(amToPm, "55px");

        clock = new CircleSelect(this, 250, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        content.add(clock);

        HorizontalPanel control = new HorizontalPanel();
        control.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        cancel = new Button("Cancel");
        cancel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                setHourSelection();
                hide();
            }
        });
        ok = new Button("Ok");
        ok.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                selectionHandler.timeSelection(hourSelection, minuteSelection);
                setHourSelection();
                hide();
            }
        });

        control.add(cancel);
        control.add(ok);

        content.add(control);
        content.setCellHorizontalAlignment(control, HasHorizontalAlignment.ALIGN_RIGHT);
    }

    private void setHourSelection() {
        setHours();
        select = Target.HOURS;
    }

    private void setHours() {
        hour.addStyleName("selected");
        minute.removeStyleName("selected");

        clock.setValues(getHourValues());
        if (twentyFour)
            clock.setInnerValues(new Integer[]{13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 00});
        clock.setSelection(hourSelection);
        clock.setSectors(12);
    }

    private void setMinuteSelection() {
        select = Target.MINUTES;
        minute.addStyleName("selected");
        hour.removeStyleName("selected");

        clock.setValues(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 00);
        clock.setSelection(minuteSelection);
        clock.setSectors(60);
    }

    public void setClockMode(boolean twentyFour) {
        this.twentyFour = twentyFour;
        amToPm.setVisible(!this.twentyFour);
        if (this.twentyFour)
            top.setCellWidth(amToPm, "0px");
        else
            top.setCellWidth(amToPm, "55px");
        setHours();
    }

    private Integer[] getHourValues() {
        return new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    }

    // Set width for time selection component
    public void setWidths(String width) {
        content.setWidth(width);
        this.width = parseStringSize(width).size;
    }

    public void setHeights(String height) {
        content.setHeight(height);
        this.height = parseStringSize(height).size;
    }

    @Override
    public void valueSelection(Integer timeValue) {
        if (select.equals(Target.HOURS)) {
            hourSelection = timeValue;
            hour.setText(format.format(timeValue));
            setMinuteSelection();
        } else {
            minuteSelection = timeValue;
            minute.setText(format.format(timeValue % 60));
        }
    }

    @Override
    public void valueHover(Integer timeValue) {
        if (select.equals(Target.HOURS)) {
            hour.setText(format.format(timeValue));
        } else {
            minute.setText(format.format(timeValue % 60));
        }
    }

    @Override
    public void mouseOutEvent() {
        setTimeSelection();
        updateClock();
    }

    private void updateClock() {
        if (select.equals(Target.HOURS)) {
            setHourSelection();
        } else {
            setMinuteSelection();
        }
    }

    private void setTimeSelection() {
        hour.setText(NumberFormat.getFormat("00").format(hourSelection));
        minute.setText(NumberFormat.getFormat("00").format(minuteSelection));
    }

    public void setTimeSelection(int hour, int minute) {
        hourSelection = hour;
        minuteSelection = minute;
        setTimeSelection();
        updateClock();
    }

    public Target getHalf() {
        return half;
    }

    public void setHalf(Target half) {
        this.half = half;
        if (half.equals(Target.PM)) {
            pm.addStyleName("selected");
            am.removeStyleName("selected");
        } else {
            am.addStyleName("selected");
            pm.removeStyleName("selected");
        }
    }

    public int getHourSelection() {
        return hourSelection;
    }

    public int getMinuteSelection() {
        return minuteSelection;
    }

    /*
     * Returns array with size in index 0 unit in index 1. Null or empty string
     * will produce Size(-1, "px");
     */
    private static Size parseStringSize(String s) {
        Size size = new Size(-1, "px");
        if (s == null) {
            return size;
        }
        s = s.trim();
        if ("".equals(s)) {
            return size;
        }

        MatchResult matcher = sizePattern.exec(s);
        if (matcher != null) {
            size.size = Integer.parseInt(matcher.getGroup(1));
            if (size.size < 0) {
                size.size = -1;
            } else {
                String unit = matcher.getGroup(3);
                if (unit == null) {
                    size.unit = "px";
                } else if (unit.equals("px")) {
                    size.unit = "px";
                } else if (unit.equals("%")) {
                    size.unit = "%";
                } else if (unit.equals("em")) {
                    size.unit = "em";
                } else if (unit.equals("ex")) {
                    size.unit = "ex";
                } else if (unit.equals("in")) {
                    size.unit = "in";
                } else if (unit.equals("cm")) {
                    size.unit = "cm";
                } else if (unit.equals("mm")) {
                    size.unit = "mm";
                } else if (unit.equals("pt")) {
                    size.unit = "pt";
                } else if (unit.equals("pc")) {
                    size.unit = "pc";
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid size argument: \"" + s
                    + "\" (should match " + sizePattern.getSource() + ")");
        }
        return size;
    }


}