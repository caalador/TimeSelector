package org.percepta.mgrankvi.client.circle.select;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author Mikael Grankvist - Vaadin Ltd
 */
@Connect(org.percepta.mgrankvi.CircleSelect.class)
public class CircleSelectConnector extends AbstractComponentConnector {

    CircleSelectServerRpc rpc = RpcProxy.create(CircleSelectServerRpc.class, this);

    CircleSelectCallback callback = new CircleSelectCallback() {
        @Override
        public void valueSelection(Integer timeValue) {
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
                    if (getState().values[i] == getState().currentValue) {
                        getWidget().setSelection((i + 1) % getState().sectors);
                        found = true;
                        break;
                    }
                }
                // Selector wasn't found so it probably is on the inner line.
                // Inner circle is matched by actual value.
                if (!found) {
                    getWidget().setSelection(getState().currentValue);
                }
            } else {
                getWidget().setSelection(getState().currentValue);
            }
        }
    };

    @Override
    protected Widget createWidget() {
        return new CircleSelect(getState().size, callback, getState().values);
    }

    @Override
    public CircleSelect getWidget() {
        return (CircleSelect) super.getWidget();
    }

    @Override
    public CircleSelectState getState() {
        return (CircleSelectState) super.getState();
    }

    @OnStateChange("size")
    void sizeChanged() {
        getWidget().setSize(getState().size);
        getWidget().refresh();
    }

    @OnStateChange("selectorColor")
    void selectorColorChanged() {
        getWidget().setSelectorColor(getState().selectorColor);
        getWidget().refresh();
    }

    @OnStateChange("fillColor")
    void fillColorChanged() {
        getWidget().setFillColor(getState().fillColor);
        getWidget().refresh();
    }

    @OnStateChange("innerValues")
    void innerValuesChanged() {
        getWidget().setInnerValues(getState().innerValues);
    }

    @OnStateChange("currentValue")
    void currentValueChanged() {
        getWidget().setSelection(getState().currentValue);
    }
}
