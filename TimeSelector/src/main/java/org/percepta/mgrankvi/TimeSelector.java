package org.percepta.mgrankvi;

import com.vaadin.ui.Component;
import org.percepta.mgrankvi.client.TimeSelectorClientRpc;
import org.percepta.mgrankvi.client.TimeSelectorServerRpc;
import org.percepta.mgrankvi.client.TimeSelectorState;

import java.io.Serializable;
import java.lang.reflect.Method;

// This is the server-side UI component that provides public API 
// for MyComponent
public class TimeSelector extends com.vaadin.ui.AbstractComponent {

    int hour = 0;
    int minute = 0;

    // To process events from the client, we implement ServerRpc
    private TimeSelectorServerRpc rpc = new TimeSelectorServerRpc() {

        @Override
        public void valueSelection(int hours, int minutes) {
            hour = hours;
            minute = minutes;
            fireChangeEvent(hours, minutes);
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

    /**
     * Get hour selection.
     *
     * @return the selected hour (from 0 to 23)
     */
    public int getHours() {
        return hour;
    }

    /**
     * Get minute selection
     *
     * @return the currently selected minute (from 0 to 59)
     */
    public int getMinutes() {
        return minute;
    }

    /**
     * Set wanted time in hours and minutes
     *
     * @param hour   wanted hour
     * @param minute wanted minute
     */
    public void setTime(int hour, int minute) {
        getRpcProxy(TimeSelectorClientRpc.class).setTime(hour, minute);
    }

    private static final Method SELECTION_EVENT;

    static {
        try {
            SELECTION_EVENT = SelectionChangeListener.class.getDeclaredMethod("selectionChanged", new Class[]{SelectionChangeEvent.class});
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException("Internal error finding methods in TimeSelector");
        }
    }

    /**
     * Selection event. This event is thrown, when a selection is made.
     */
    public class SelectionChangeEvent extends Component.Event {
        private static final long serialVersionUID = 1890057101443553065L;

        private final int hours;
        private final int minutes;

        public SelectionChangeEvent(final Component source, int hours, int minutes) {
            super(source);
            this.hours = hours;
            this.minutes = minutes;
        }

        public int getHours() {
            return hours;
        }

        public int getMinutes() {
            return minutes;
        }
    }

    /**
     * Interface for listening for a change fired by a {@link Component}.
     */
    public interface SelectionChangeListener extends Serializable {
        public void selectionChanged(SelectionChangeEvent event);

    }

    /**
     * Adds the change listener.
     *
     * @param listener the Listener to be added.
     */
    public void addSelectionChangeListener(final SelectionChangeListener listener) {
        addListener(SelectionChangeEvent.class, listener, SELECTION_EVENT);
    }

    /**
     * Removes the selection listener.
     *
     * @param listener the Listener to be removed.
     */
    public void removeSelectionChangeListener(final SelectionChangeListener listener) {
        removeListener(SelectionChangeEvent.class, listener, SELECTION_EVENT);
    }

    /**
     * Fires a event to all listeners without any event details.
     */
    public void fireChangeEvent(int hours, int minutes) {
        fireEvent(new SelectionChangeEvent(this, hours, minutes));
    }
}
