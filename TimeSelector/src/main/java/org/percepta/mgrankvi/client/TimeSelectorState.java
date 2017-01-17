package org.percepta.mgrankvi.client;

import com.vaadin.shared.ui.customfield.CustomFieldState;

public class TimeSelectorState extends CustomFieldState {

	public boolean twentyfour = true;

	public Integer[] visibleMinutes = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 0};
	public int minuteSectors = 60;
}