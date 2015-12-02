package org.percepta.mgrankvi.client;

import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.communication.ServerRpc;

// ServerRpc is used to pass events from client to server
public interface TimeSelectorServerRpc extends ServerRpc {

	// Example API: Widget click is clicked
	void valueSelection(int hours, int minutes);

}
