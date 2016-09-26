package org.percepta.mgrankvi.client.circle.select;

import java.io.Serializable;

/**
 * @author Mikael Grankvist - Vaadin }>
 */
public interface CircleSelectCallback extends Serializable {

    void valueSelection(Integer timeValue);

    void valueHover(Integer timeValue);

    void mouseOutEvent();
}
