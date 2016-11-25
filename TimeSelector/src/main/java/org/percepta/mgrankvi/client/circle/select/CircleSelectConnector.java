package org.percepta.mgrankvi.client.circle.select;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author Mikael Grankvist - Vaadin Ltd
 */
@Connect(org.percepta.mgrankvi.CircleSelect.class)
public class CircleSelectConnector extends AbstractComponentConnector {

    CircleSelectServerRpc rpc = RpcProxy.create(CircleSelectServerRpc.class, this);

    int value = 0;

    CircleSelectCallback callback = new CircleSelectCallback() {
        @Override
        public void valueSelection(Integer timeValue) {
            value = timeValue;
            rpc.setSelection(timeValue);
        }

        @Override
        public void valueHover(Integer timeValue) {
            rpc.valueHover(timeValue);
        }

        @Override
        public void mouseOutEvent() {
            if (getState().values.length == getState().sectors) {
                boolean found = false;
                for (int i = 0; i < getState().sectors; i++) {
                    if (getState().values[i] == value) {
                        getWidget().setSelection((i + 1) % getState().sectors);
                        found = true;
                        break;
                    }
                }
                // Selector wasn't found so it probably is on the inner line.
                // Inner circle is matched by actual value.
                if (!found) {
                    getWidget().setSelection(value);
                }
            } else {
                getWidget().setSelection(value);
            }
        }
    };

    @Override
    protected Widget createWidget() {
        return new CircleSelect(callback, 250, getState().values);
    }


    @Override
    public CircleSelect getWidget() {
        return (CircleSelect) super.getWidget();
    }

    @Override
    public CircleSelectState getState() {
        return (CircleSelectState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        getWidget().setSectors(getState().sectors);

        getWidget().setValues(getState().values);
        if (getState().innerValues != null) {
            getWidget().setInnerValues(getState().innerValues);
        }
        if(getState().currentValue != null) {
            getWidget().setSelection(getState().currentValue);
        }
    }
}
