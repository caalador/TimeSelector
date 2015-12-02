package org.percepta.mgrankvi.client.circle.select;

/**
 * @author Mikael Grankvist - Vaadin }>
 */
public interface CircleSelectCallback {

    void valueSelection(Integer timeValue);

    void valueHover(Integer timeValue);

    void mouseOutEvent();
}
