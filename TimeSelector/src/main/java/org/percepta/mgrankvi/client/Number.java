package org.percepta.mgrankvi.client;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * @author Mikael Grankvist - Vaadin }>
 */
public class Number {

    public Integer number;
    public double x;
    public double y;

    public Number(Integer number, double x, double y) {
        this.number = number;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return NumberFormat.getFormat("00").format(number);
    }
}
