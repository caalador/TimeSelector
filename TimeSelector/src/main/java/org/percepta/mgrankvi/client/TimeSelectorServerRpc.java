package org.percepta.mgrankvi.client;

import com.vaadin.shared.communication.ServerRpc;

// ServerRpc is used to pass events from client to server
public interface TimeSelectorServerRpc extends ServerRpc {

    /**
     * Value selection from client
     *
     * @param hours   Selected hours
     * @param minutes Selected minutes
     */
    void valueSelection(int hours, int minutes);

}
