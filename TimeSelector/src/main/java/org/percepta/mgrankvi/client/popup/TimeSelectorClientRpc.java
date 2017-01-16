package org.percepta.mgrankvi.client.popup;

import com.vaadin.shared.communication.ClientRpc;

public interface TimeSelectorClientRpc extends ClientRpc {
	void setTime(int hours, int minutes);
}