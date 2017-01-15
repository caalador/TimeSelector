package org.percepta.mgrankvi.client.embedded;

import org.percepta.mgrankvi.client.Target;

public class TimeSelectorState extends com.vaadin.shared.AbstractFieldState {

    public boolean twentyFourTimeFormat = true;
    public Integer[] visibleMinutes = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 0};
    public int minuteSectors = 60;
    public int clockSize = 250;
    public int selectedHour = 12;
    public int selectedMinute = 0;
    public Target half = Target.AM;
    public String fillColor = "seagreen";
    public String selectorColor = "mediumaquamarine";
}
