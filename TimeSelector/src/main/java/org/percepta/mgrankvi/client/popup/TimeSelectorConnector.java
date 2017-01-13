package org.percepta.mgrankvi.client.popup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import org.percepta.mgrankvi.TimeSelector;
import org.percepta.mgrankvi.client.*;

// Connector binds client-side widget class to server-side component class
// Connector lives in the client and the @Connect annotation specifies the
// corresponding server-side component
@Connect(TimeSelector.class)
public class TimeSelectorConnector extends AbstractComponentConnector {

    // ServerRpc is used to send events to server. Communication implementation
    // is automatically created here
    TimeSelectorServerRpc rpc = RpcProxy.create(TimeSelectorServerRpc.class, this);

    public TimeSelectorConnector() {

        // To receive RPC events from server, we register ClientRpc implementation
        registerRpc(TimeSelectorClientRpc.class, new TimeSelectorClientRpc() {
            @Override
            public void setTime(int hours, int minutes) {
                getWidget().setTime(hours, minutes);
            }
        });

        getWidget().addSelectionHandler(new SelectionHandler(){
            @Override
            public void timeSelection(int hour, int minute) {
                rpc.valueSelection(hour, minute);
            }
        });
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(TimeSelectorWidget.class);
    }

    @Override
    public TimeSelectorWidget getWidget() {
        return (TimeSelectorWidget) super.getWidget();
    }

    @Override
    public TimeSelectorState getState() {
        return (TimeSelectorState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
    }

    @OnStateChange("width")
    void setWidth() {
        getWidget().setWidth(getState().width);
    }

    @OnStateChange("height")
    void setHeight() {
        getWidget().setHeight(getState().height);
    }

    @OnStateChange("twentyfour")
    void clockMode() {
        getWidget().setClockMode(getState().twentyfour);
    }

    @OnStateChange("visibleMinutes")
    void setVisibleMinutes() {
        getWidget().setVisibleMinutes(getState().visibleMinutes);
    }

    @OnStateChange("minuteSectors")
    void setMinuteSectors() {
        getWidget().setMinuteSectors(getState().minuteSectors);
    }
}
