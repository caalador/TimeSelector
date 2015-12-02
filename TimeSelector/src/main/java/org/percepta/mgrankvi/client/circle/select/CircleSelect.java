package org.percepta.mgrankvi.client.circle.select;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.client.VConsole;
import org.percepta.mgrankvi.client.Number;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mikael Grankvist - Vaadin }>
 */
public class CircleSelect extends Composite implements MouseMoveHandler, MouseOutHandler {

    private String TOP_BG = "mediumaquamarine";

    private final Canvas time;

    private NumberFormat format = NumberFormat.getFormat("00");

    private int circleX, circleY, radian;

    private List<Number> numbers = new LinkedList<Number>();
    private List<Integer> values = new LinkedList<Integer>();

    private int size = 250;

    private Integer selection = 12;
    private int numSlices = 24;

    private final CircleSelectCallback circleSelectCallback;


    public CircleSelect(final CircleSelectCallback circleSelectCallback, int size, Integer... values) {
        this.circleSelectCallback = circleSelectCallback;
        this.values.addAll(Arrays.asList(values));

        SimplePanel baseContent = new SimplePanel();
        baseContent.getElement().getStyle().setBackgroundColor("white");

        time = Canvas.createIfSupported();
        if (time != null) {
            setSize(size);
            baseContent.add(time);

            time.getElement().getStyle().setProperty("outline", "none");
            time.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    circleSelectCallback.valueSelection(selection);
                }
            });
            paint();
            time.addDomHandler(this, MouseMoveEvent.getType());
            time.addDomHandler(this, MouseOutEvent.getType());
        }
        initWidget(baseContent);
    }

    /**
     * Refresh canvas
     */
    public void refresh() {
        paint();
    }

    /**
     * Set canvas size (rectangle)
     */
    public void setSize(int size) {
        this.size = size;

        time.setWidth(size + "px");
        time.setHeight(size + "px");
        clearCanvas();

        circleX = circleY = radian = size / 2;
        radian -= 5;

        numbers.clear();
    }

    /**
     * Set new major values to be painted in circle.
     * @param values Values to set in circle
     */
    public void setValues(Integer... values) {
        numbers.clear();
        this.values.clear();
        this.values.addAll(Arrays.asList(values));
        paint();
    }

    /**
     * set amount of slices to carve the circle into.
     *
     * @param slices
     */
    public void setSlices(int slices) {
        numSlices = slices;
        numbers.clear();
        paint();
    }

    public void setSectors(int sectors) {
        setSlices(sectors*2);
    }

    /**
     * Set the selected time to show as selected
     * @param selection Selected time wanted
     */
    public void setSelection(int selection) {
        this.selection = selection;
        paint();
    }

    private void paint() {
        clearCanvas();

        Context2d context = time.getContext2d();
        context.setFillStyle("gray");

        context.beginPath();
        context.arc(circleX, circleY, 1, 0, 2 * Math.PI, false);
        context.closePath();
        context.stroke();
        context.fill();

        context.beginPath();
        context.arc(circleX, circleY, radian, 0, 2 * Math.PI, false);
        context.closePath();
        context.stroke();


        context.setFont("bold 15px Courier New");

        TextMetrics textMetrics = context.measureText("00");
        final int textWidth = (int) Math.ceil(textMetrics.getWidth());
        int halfWidth = textWidth / 2;

        // Init visible number positions
        if (numbers.isEmpty()) {

            int degreesPerStep = 360 / values.size();
            for (int i = 1; i <= values.size(); i++) {
                int degrees = i * degreesPerStep;
                double rad = Math.toRadians(degrees) - (0.5 * Math.PI);

                double x = circleX + (Math.cos(rad) * (radian - 15)) - halfWidth;
                double y = circleY + (Math.sin(rad) * (radian - 15)) + (context.measureText("W").getWidth() / 2);

                numbers.add(new org.percepta.mgrankvi.client.Number(values.get(i-1), x, y));
            }
        }

        if (selection != null) {
            context.setFillStyle("gainsboro");

            int degrees = (selection * (360 / (numSlices / 2)));
            double rad = Math.toRadians(degrees) - (0.5 * Math.PI);

            context.setStrokeStyle(TOP_BG);
            context.beginPath();
            context.moveTo(circleX, circleY);
            double x = circleX + (Math.cos(rad) * (radian - 15));
            double y = circleY + (Math.sin(rad) * (radian - 15));
            context.lineTo(x, y);
            context.closePath();
            context.stroke();

            context.beginPath();
            context.setFillStyle(TOP_BG);
            context.arc(x, y, Math.sqrt(textWidth * textWidth + 15 * 15) / 2, 0, 2 * Math.PI, false);
            context.closePath();
            context.fill();

            context.beginPath();
            context.setFillStyle("seagreen");
            context.arc(x, y, 2, 0, 2 * Math.PI, false);
            context.closePath();
            context.fill();
        }

        context.setStrokeStyle("black");
        context.setFillStyle("black");

        for (Number number : numbers) {
            context.fillText(format.format(number.number), number.x, number.y);
        }

    }

    protected void clearCanvas() {
        time.setCoordinateSpaceWidth(size);
        time.setCoordinateSpaceHeight(size);
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        int relativeX = event.getRelativeX(time.getElement());
        int relativeY = event.getRelativeY(time.getElement());

        if (isInsideCircle(relativeX, relativeY)) {
            // + 0.5PI so we get 0 up top at 12 o'clock on the circle instead of 3 o'clock
            double theta = Math.atan2(relativeY - circleY, relativeX - circleX) + (Math.PI / 2);
            //theta is now in the range -Math.PI to Math.PI
            if (theta < 0)
                theta = 2 * Math.PI + theta;

            //Now theta is in the range [0, 2*pi]
            //Use this value to determine which slice of the circle the point resides in.
            //For example:
            int whichSlice = 0;
            double sliceSize = Math.PI * 2 / numSlices;
            double sliceStart;
            for (int i = 1; i <= numSlices; i++) {
                sliceStart = i * sliceSize;

                if (theta < sliceStart) {
                    whichSlice = i;
                    break;
                }
            }

            VConsole.log("in slice " + whichSlice + " - Theta: " + Math.toDegrees(theta));

            Integer number = (int) Math.ceil(whichSlice / 2);
            // Special case for 1-12 hour clock
            if (number == 0 && numSlices == 24) number = 12;
            VConsole.log("Minute value: " + Math.ceil(Math.toDegrees(theta) / 6) % 60);
            VConsole.log("Sector: " + format.format(number));
            selection = number;

            circleSelectCallback.valueHover(number);
        } else {
            circleSelectCallback.mouseOutEvent();
        }

        paint();
    }

    @Override
    public void onMouseOut(MouseOutEvent mouseOutEvent) {
        circleSelectCallback.mouseOutEvent();
        paint();
    }

    private boolean isInsideCircle(int pointX, int pointY) {
        return isInsideCircle(pointX, pointY, circleX, circleY, radian);
    }

    private boolean isInsideCircle(int pointX, int pointY, int centerX, int centerY, int radius) {
        int relX = pointX - centerX;
        int relY = pointY - centerY;
        return relX * relX + relY * relY <= radius * radius;
    }

}

// [360 deg/12 = 30 deg] for hours and [360 deg/60 = 6 deg] for minutes
// Rad = deg * PI / 180
// 12 "0"  - 1.5PI
// 01 "30" - 5PI/3
// 02 "60" - 330PI/180
// 03 "90" - 0
// 04 "120" - PI/6
// 05 "150" - PI/3
// 06 "180" - 0.5PI
// 07 "210" - PI/1.5
// 08 "240" - PI/1.2
// 09 "270" - PI
// 10 "300" - 210PI/180
// 11 "330" - 240PI/180