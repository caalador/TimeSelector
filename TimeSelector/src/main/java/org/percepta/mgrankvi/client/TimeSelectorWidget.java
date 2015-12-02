package org.percepta.mgrankvi.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vaadin.client.VConsole;
import org.percepta.mgrankvi.client.circle.select.CircleSelect;
import org.percepta.mgrankvi.client.circle.select.CircleSelectCallback;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TimeSelectorWidget extends Composite implements CircleSelectCallback, MouseMoveHandler, MouseOutHandler {

    public static final String CLASS_NAME = "TimeSelector";

    private enum Target {HOURS, MINUTES, AM, PM}

    private String TOP_BG = "mediumaquamarine";
    private String NUMBER_SELECTED = "white";
    private String NUMBER_INACTIVE = "lightseagreen";
    private String DAY_TIME_SELECTED = "lavender";
    private String DAY_TIME_INACTIVE = "darkseagreen";

    private NumberFormat format = NumberFormat.getFormat("00");

    private static RegExp sizePattern = RegExp.compile("^(-?\\d+(\\.\\d+)?)(%|px|em|ex|in|cm|mm|pt|pc)?$");
//    private final Canvas time;
    private Button am, pm, cancel, ok;
    private Label hour, minute;

    private int width, height;
    private int circleX, circleY, radian;

    private final VerticalPanel content;
    private HorizontalPanel top;

    private Integer selection = 12;

    private Target select = Target.HOURS;
    private Target half = Target.AM;

    private List<Number> numbers = new LinkedList<Number>();

    private int hourSelection = 12;
    private int minuteSelection = 0;

    CircleSelect clock;

    public TimeSelectorWidget() {

        content = new VerticalPanel();

        SimplePanel baseContent = new SimplePanel();
        baseContent.add(content);
        baseContent.getElement().getStyle().setBackgroundColor("white");
        initWidget(baseContent);
        // CSS class-name should not be v- prefixed
        baseContent.setStyleName("c-" + CLASS_NAME);

        top = new HorizontalPanel();
        top.setWidth("100%");
        top.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        content.add(top);
        content.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        top.getElement().getStyle().setBackgroundColor(TOP_BG);

        int fontSize = 40;
        hour = new Label("12");
        hour.setHeight("100%");
        hour.getElement().getStyle().setFontSize(fontSize, Style.Unit.PX);
        hour.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        hour.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                select = Target.HOURS;
                hour.getElement().getStyle().setColor(NUMBER_SELECTED);
                minute.getElement().getStyle().setColor(NUMBER_INACTIVE);
//                paint();
                setHourSelection();
//                clock.refresh();
            }
        });
        minute = new Label("00");
        minute.setHeight("100%");
        minute.getElement().getStyle().setFontSize(fontSize, Style.Unit.PX);
        minute.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        minute.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                select = Target.MINUTES;
                hour.getElement().getStyle().setColor(NUMBER_INACTIVE);
                minute.getElement().getStyle().setColor(NUMBER_SELECTED);
//                paint();
setMinuteSelection();
//                clock.refresh();
            }
        });
        Label divider = new Label(":");
        divider.setHeight("100%");
        divider.getElement().getStyle().setFontSize(fontSize, Style.Unit.PX);
        divider.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);

        // Default selection colors.
        hour.getElement().getStyle().setColor(NUMBER_SELECTED);
        minute.getElement().getStyle().setColor(NUMBER_INACTIVE);

        top.add(hour);
        top.add(divider);
        top.add(minute);

        VerticalPanel amToPm = new VerticalPanel();
        amToPm.setWidth("55px");
        amToPm.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        amToPm.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        am = new Button("AM");
        clearButtonStyle(am.getElement());
        am.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (half == Target.PM) {
                    hourSelection -= 12;
                    setTimeSelection();
                }
                half = Target.AM;
                am.getElement().getStyle().setColor(DAY_TIME_SELECTED);
                pm.getElement().getStyle().setColor(DAY_TIME_INACTIVE);
//                paint();
                setHourSelection();
            }
        });
        pm = new Button("PM");
        clearButtonStyle(pm.getElement());
        pm.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (half == Target.AM) {
                    hourSelection += 12;
                    setTimeSelection();
                }
                half = Target.PM;
                pm.getElement().getStyle().setColor(DAY_TIME_SELECTED);
                am.getElement().getStyle().setColor(DAY_TIME_INACTIVE);
//                paint();
                setHourSelection();
            }
        });

        am.getElement().getStyle().setColor(DAY_TIME_SELECTED);
        pm.getElement().getStyle().setColor(DAY_TIME_INACTIVE);

        amToPm.add(am);
        amToPm.add(pm);

        top.add(amToPm);
        top.setCellWidth(amToPm, "55px");

        clock = new CircleSelect(this, 250, 1,2,3,4,5,6,7,8,9,10,11,12);
        content.add(clock);

//        time = Canvas.createIfSupported();
//        if (time != null) {
//            content.add(time);
//
//            setWidths("250px");
//            setHeights("350px");
//
//            time.getElement().getStyle().setProperty("outline", "none");
//            time.addClickHandler(new ClickHandler() {
//                @Override
//                public void onClick(ClickEvent clickEvent) {
//                    if (select == Target.HOURS) {
//                        hourSelection = selection;
//                        if(half == Target.PM) {
//                            hourSelection += 12;
//                        }
//
//                        select = Target.MINUTES;
//                        selection = minuteSelection;
//                        hour.getElement().getStyle().setColor(NUMBER_INACTIVE);
//                        minute.getElement().getStyle().setColor(NUMBER_SELECTED);
//                        paint();
//                    } else {
//                        minuteSelection = selection;
//                    }
//                }
//            });
//            paint();
//            time.addDomHandler(this, MouseMoveEvent.getType());
//            time.addDomHandler(this, MouseOutEvent.getType());
//        } else {
//            Label error = new Label("Component not supported");
//            content.add(error);
//            setWidths("250px");
//            setHeights("50px");
//        }


        HorizontalPanel control = new HorizontalPanel();
        control.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        cancel = new Button("Cancel");
        ok = new Button("Ok");

        control.add(cancel);
        control.add(ok);

        content.add(control);
        content.setCellHorizontalAlignment(control, HasHorizontalAlignment.ALIGN_RIGHT);
    }

    private void setHourSelection() {
        clock.setValues(getHourValues());
        clock.setSectors(12);
    }

    private void setMinuteSelection() {
        clock.setValues(5,10,15,20,25,30,35,40,45,50,55,60);
        clock.setSectors(60);
    }

    private Integer[] getHourValues() {
        if(half.equals(Target.PM)){
            return new Integer[] {13,14,15,16,17,18,19,20,21,22,23,24};
        }
        return  new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12};
    }

    private void clearButtonStyle(Element element) {
        element.getStyle().setProperty("background", "none");
        element.getStyle().setProperty("border", "none");
        element.getStyle().setProperty("outline", "none");
        element.getStyle().setFontWeight(Style.FontWeight.BOLD);
    }

//    private void paint() {
//        clearCanvas();
//
//        Context2d context = time.getContext2d();
//        context.setFillStyle("gray");
//
//        context.beginPath();
//        context.arc(circleX, circleY, 1, 0, 2 * Math.PI, false);
//        context.closePath();
//        context.stroke();
//        context.fill();
//
//        context.beginPath();
//        context.arc(circleX, circleY, radian, 0, 2 * Math.PI, false);
//        context.closePath();
//        context.stroke();
//
//
//        context.setFont("bold 15px Courier New");
//
//        TextMetrics textMetrics = context.measureText("00");
//        final int textWidth = (int) Math.ceil(textMetrics.getWidth());
//        int halfWidth = textWidth / 2;
//
//        // Init visible number positions
//        if (numbers.isEmpty()) {
//
//            for (int i = 1; i <= 12; i++) {
//                int degrees = (i * 30);
//                double rad = Math.toRadians(degrees) - (0.5 * Math.PI);
//
//                double x = circleX + (Math.cos(rad) * (radian - 15)) - halfWidth;
//                double y = circleY + (Math.sin(rad) * (radian - 15)) + (context.measureText("W").getWidth()/2);
//
//                numbers.add(new Number(i, x, y));
//            }
//        }
//
//        if (selection != null) {
//            context.setFillStyle("gainsboro");
//
////            if (select == Target.HOURS) {
////                for (Number number : numbers) {
////                    if (number.number == selection) {
////
////                        int degrees = (number.number * 30);
////                        double rad = Math.toRadians(degrees) - (0.5 * Math.PI);
////
////                        context.setStrokeStyle(TOP_BG);
////                        context.beginPath();
////                        context.moveTo(circleX, circleY);
////                        double x = circleX + (Math.cos(rad) * (radian - 15));
////                        double y = circleY + (Math.sin(rad) * (radian - 15));
////
////                        context.setStrokeStyle(TOP_BG);
////                        context.beginPath();
////                        context.moveTo(circleX, circleY);
////                        context.lineTo(x, y);
////                        context.closePath();
////                        context.stroke();
////
////                        context.beginPath();
////                        context.setFillStyle(TOP_BG);
////                        context.arc(x, y, Math.sqrt(textWidth * textWidth + 15 * 15) / 2, 0, 2 * Math.PI, false);
////                        context.closePath();
////                        context.fill();
////                        break;
////                    }
////                }
////            } else {
////                int degrees = (selection * 6);
//            int numSlices = select == Target.HOURS ? 24 : 120;
//            int degrees = (selection * (360/(numSlices/2)));
//                double rad = Math.toRadians(degrees) - (0.5 * Math.PI);
//
//                context.setStrokeStyle(TOP_BG);
//                context.beginPath();
//                context.moveTo(circleX, circleY);
//                double x = circleX + (Math.cos(rad) * (radian - 15));
//                double y = circleY + (Math.sin(rad) * (radian - 15));
//                context.lineTo(x, y);
//                context.closePath();
//                context.stroke();
//
//                context.beginPath();
//                context.setFillStyle(TOP_BG);
//                context.arc(x, y, Math.sqrt(textWidth * textWidth + 15 * 15) / 2, 0, 2 * Math.PI, false);
//                context.closePath();
//                context.fill();
//
//                context.beginPath();
//                context.setFillStyle("seagreen");
//                context.arc(x, y, 2, 0, 2 * Math.PI, false);
//                context.closePath();
//                context.fill();
//            }
////        }
//
//        context.setStrokeStyle("black");
//        context.setFillStyle("black");
//
//        for (Number number : numbers) {
//            context.fillText(format.format(getTimeValue(number.number)), number.x, number.y);
//        }
//
//    }

    public int getTimeValue(int value) {
        if (select == Target.HOURS) {
            if (half == Target.PM) {
                return value + 12;
            }
            return value;
        }
        return (value * 5) % 60;
    }


//    private void clearCanvas() {
//        time.setCoordinateSpaceWidth(width);
//        time.setCoordinateSpaceHeight(width);
//    }


    // Set width for time selection component
    public void setWidths(String width) {
        content.setWidth(width);
        this.width = parseStringSize(width).size;
        numbers.clear();

//        if (time != null) {
//            time.setWidth(this.width + "px");
//            time.setHeight(this.width + "px");
//            clearCanvas();
//        }
        circleX = circleY = radian = this.width / 2;
        radian -= 5;
    }

    public void setHeights(String height) {
        content.setHeight(height);
        this.height = parseStringSize(height).size;
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

    @Override
    public void onMouseMove(MouseMoveEvent event) {
//        int relativeX = event.getRelativeX(time.getElement());
//        int relativeY = event.getRelativeY(time.getElement());
//
//        if (isInsideCircle(relativeX, relativeY)) {
//            // + 0.5PI so we get 0 up top at 12 o'clock on the circle instead of 3 o'clock
//            double theta = Math.atan2(relativeY - circleY, relativeX - circleX) + (Math.PI / 2);
//            //theta is now in the range -Math.PI to Math.PI
//            if (theta < 0)
//                theta = 2 * Math.PI + theta;
//
//            //Now theta is in the range [0, 2*pi]
//            //Use this value to determine which slice of the circle the point resides in.
//            //For example:
//            int numSlices = select == Target.HOURS ? 24 : 120;
//            int whichSlice = 0;
//            double sliceSize = Math.PI * 2 / numSlices;
//            double sliceStart;
//            for (int i = 1; i <= numSlices; i++) {
//                sliceStart = i * sliceSize;
//
//                if (theta < sliceStart) {
//                    whichSlice = i;
//                    break;
//                }
//            }
//
//            VConsole.log("in slice " + whichSlice + " - Theta: " + Math.toDegrees(theta));
//
//            Integer number = (int) Math.ceil(whichSlice / 2);//sectorMap.get(whichSlice);
//            if (number == 0 && select == Target.HOURS) number = 12;
//            VConsole.log("Minute value: " + Math.ceil(Math.toDegrees(theta) / 6) % 60);
//            VConsole.log("Sector: " + format.format(number));
//            selection = number;
//
//            valueSelection(number);
//        } else {
//            setTimeSelection();
//        }
//
//        paint();
    }

    @Override
    public void valueSelection(Integer timeValue) {
        selection = timeValue;

        if (select == Target.HOURS) {
            if (half == Target.PM) {
                timeValue += 12;
            }
            hour.setText(format.format(timeValue));
            setMinuteSelection();
        } else {
            minute.setText(format.format(timeValue% 60));
        }
    }

    @Override
    public void valueHover(Integer timeValue) {
        if (select == Target.HOURS) {
            if (half == Target.PM) {
                timeValue += 12;
            }
            hour.setText(format.format(timeValue));
        } else {
            minute.setText(format.format(timeValue % 60));
        }
    }

    @Override
    public void mouseOutEvent() {
        setTimeSelection();
    }

    @Override
    public void onMouseOut(MouseOutEvent mouseOutEvent) {
        setTimeSelection();
//        paint();
        clock.refresh();
    }

    public void setTimeSelection() {
        hour.setText(NumberFormat.getFormat("00").format(hourSelection));
        minute.setText(NumberFormat.getFormat("00").format(minuteSelection));
    }

//    private boolean isInsideCircle(int pointX, int pointY) {
//        return isInsideCircle(pointX, pointY, circleX, circleY, radian);
//    }
//
//    private boolean isInsideCircle(int pointX, int pointY, int centerX, int centerY, int radius) {
//        int relX = pointX - centerX;
//        int relY = pointY - centerY;
//        return relX * relX + relY * relY <= radius * radius;
//    }

}