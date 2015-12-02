package org.percepta.mgrankvi;

import org.percepta.mgrankvi.client.TimeSelectorClientRpc;
import org.percepta.mgrankvi.client.TimeSelectorServerRpc;
import org.percepta.mgrankvi.client.TimeSelectorState;

import com.vaadin.shared.MouseEventDetails;

// This is the server-side UI component that provides public API 
// for MyComponent
public class TimeSelector extends com.vaadin.ui.AbstractComponent {

	private int clickCount = 0;

	// To process events from the client, we implement ServerRpc
	private TimeSelectorServerRpc rpc = new TimeSelectorServerRpc() {

		// Event received from client - user clicked our widget
		public void clicked(MouseEventDetails mouseDetails) {
			
			// Send nag message every 5:th click with ClientRpc
			if (++clickCount % 5 == 0) {
				getRpcProxy(TimeSelectorClientRpc.class)
						.alert("Ok, that's enough!");
			}
			
			// Update shared state. This state update is automatically 
			// sent to the client. 
			getState().text = "You have clicked " + clickCount + " times";
		}
	};

	public TimeSelector() {

		// To receive events from the client, we register ServerRpc
		registerRpc(rpc);
	}

	// We must override getState() to cast the state to MyComponentState
	@Override
	public TimeSelectorState getState() {
		return (TimeSelectorState) super.getState();
	}
}
