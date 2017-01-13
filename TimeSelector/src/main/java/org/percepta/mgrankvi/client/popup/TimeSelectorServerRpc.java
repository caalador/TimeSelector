package org.percepta.mgrankvi.client.popup;

import com.vaadin.shared.communication.ServerRpc;

public interface TimeSelectorServerRpc extends ServerRpc {
	void valueSelection(int hours, int minutes);
}
