package org.percepta.mgrankvi.client.embedded;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.HasComponentsConnector;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import org.percepta.mgrankvi.EmbeddedTimeSelector;
import org.percepta.mgrankvi.client.Target;

import java.util.List;

@Connect(EmbeddedTimeSelector.class)
public class EmbeddedTimeSelectorConnector extends AbstractComponentConnector implements HasComponentsConnector, WidgetCallback {

    TimeSelectorServerRpc rpc = RpcProxy.create(TimeSelectorServerRpc.class, this);

    public EmbeddedTimeSelectorConnector() {
        super();
        registerRpc(TimeSelectorClientRpc.class, new TimeSelectorClientRpc() {
            @Override
            public void setHourAndMinute(int hours, int minutes) {
                getState().selectedHour = hours;
                getState().selectedMinute = minutes;
            }

            @Override
            public void setHalf(Target half) {
                getState().half = half;
            }
        });
        getWidget().addCallback(this);
    }

    @Override
    public void timeSelected(int hour, int minute) {
        EmbeddedTimeSelectorConnector.this.rpc.setHourAndMinute(hour, minute);
    }

    @Override
    public void setTarget(Target target) {
        EmbeddedTimeSelectorConnector.this.rpc.setHalf(target);
    }

    @Override
    protected Widget createWidget() {
        return new TimeSelectorEmbeddedWidget(getState().fillColor, getState().selectorColor, getState().clockSize);
    }

    @Override
    public TimeSelectorEmbeddedWidget getWidget() {
        return (TimeSelectorEmbeddedWidget) super.getWidget();
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

    @OnStateChange("twentyFourTimeFormat")
    void clockMode() {
        getWidget().setClockMode(getState().twentyFourTimeFormat);
    }

    @OnStateChange("visibleMinutes")
    void setVisibleMinutes() {
        getWidget().setVisibleMinutes(getState().visibleMinutes);
    }

    @OnStateChange("minuteSectors")
    void setMinuteSectors() {
        getWidget().setMinuteSectors(getState().minuteSectors);
    }

    @OnStateChange("clockSize")
    void setClockSize() {
        getWidget().setClockSize(getState().clockSize);
    }

    @OnStateChange("selectedHour")
    void setHours() {
        getWidget().setHourSelection(getState().selectedHour);
    }

    @OnStateChange("selectedMinute")
    void setMinutes() {
        getWidget().setMinuteSelection(getState().selectedMinute);
    }

    @OnStateChange("selectorColor")
    void setSelectorColor() {
        getWidget().setSelectorColor(getState().selectorColor);
    }

    @OnStateChange("fillColor")
    void setFillColor() {
        getWidget().setFillColor(getState().fillColor);
    }

    @Override
    public void updateCaption(ComponentConnector componentConnector) {}

    @Override
    public List<ComponentConnector> getChildComponents() {
        return null;
    }

    @Override
    public void setChildComponents(List<ComponentConnector> list) {}

    @Override
    public HandlerRegistration addConnectorHierarchyChangeHandler(ConnectorHierarchyChangeEvent.ConnectorHierarchyChangeHandler connectorHierarchyChangeHandler) {
        return null;
    }
}
