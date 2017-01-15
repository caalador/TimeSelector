package org.percepta.mgrankvi.client.circle.select;

import com.vaadin.shared.AbstractFieldState;

/**
 * @author Mikael Grankvist - Vaadin Ltd
 */
public class CircleSelectState extends AbstractFieldState {

    public Integer[] values = {1,2,3,4,5,6,7,8,9,10,11,12};
    public Integer[] innerValues = null;
    public int sectors = 12;
    public Integer currentValue = null;
    public Integer size = 250;
    public String selectorColor = "mediumaquamarine";
    public String fillColor = "seagreen";
}
