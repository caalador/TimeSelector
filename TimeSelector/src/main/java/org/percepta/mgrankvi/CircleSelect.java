package org.percepta.mgrankvi;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.ConnectorEventListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import com.vaadin.util.ReflectTools;
import org.percepta.mgrankvi.client.circle.select.CircleSelectServerRpc;
import org.percepta.mgrankvi.client.circle.select.CircleSelectState;

import java.lang.reflect.Method;

/**
 * @author Mikael Grankvist - Vaadin Ltd
 */
public class CircleSelect extends CustomField<Integer> {

    CircleSelectServerRpc rpc = new CircleSelectServerRpc() {
        @Override
        public void setSelection(Integer newValue) {
            setValue(newValue);
        }

        @Override
        public void valueHover(Integer hoverValue) {
            fireHoverEvent(hoverValue);
        }
    };

    public CircleSelect() {
        registerRpc(rpc);
    }

    @Override
    protected Component initContent() {
        return new CssLayout();
    }

    @Override
    public Class<? extends Integer> getType() {
        return Integer.class;
    }

    @Override
    protected CircleSelectState getState() {
        return (CircleSelectState) super.getState();
    }

    /**
     * Set the visible values to be drawn into the circle
     *
     * @param values Values to draw into circle
     */
    public void setVisibleValues(Integer... values) {
        getState().values = values;
    }

    /**
     * Set amount of selectable sectors. If sectors == values.length selection will happen by values[sector]
     *
     * @param sectors Amount of sectors in circle
     */
    public void setSectors(int sectors) {
        getState().sectors = sectors;
    }

    /**
     * Set inner values to circle.
     * NOTE: Amount of values should correspond to sector amount!!
     *
     * @param values Inner values to draw to circle
     */
    public void setInnerValues(Integer... values) {
        getState().innerValues = values;
    }

    /**
     * Clear the inner values of the circle
     */
    public void clearInnerValues() {
        getState().innerValues = null;
    }

    /**
     * Sets the canvas size.
     */
    public void setSize(Integer size) {
        getState().size = size;
    }

    @Override
    public void setValue(Integer newFieldValue) throws ReadOnlyException, Converter.ConversionException {
        super.setValue(newFieldValue);
        getState().currentValue = newFieldValue;
    }

    public interface HoverEventListener extends ConnectorEventListener {

        Method ACTION_CLICK_METHOD = ReflectTools.findMethod(HoverEventListener.class, "actionClick",
                HoverEvent.class);

        void actionClick(HoverEvent event);
    }

    public static class HoverEvent extends Component.Event {

        private final Integer value;

        public HoverEvent(CircleSelect circleSelect, Integer value) {
            super(circleSelect);
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public void addHoverEventListener(HoverEventListener listener) {
        addListener(HoverEvent.class, listener, HoverEventListener.ACTION_CLICK_METHOD);
    }

    public void removeHoverEventListener(HoverEventListener listener) {
        removeListener(HoverEventListener.class, listener);
    }

    /**
     * Fires a event to all listeners without any event details.
     */
    public void fireHoverEvent(Integer value) {
        fireEvent(new HoverEvent(this, value));
    }
}
