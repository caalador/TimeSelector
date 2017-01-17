package org.percepta.mgrankvi;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import org.percepta.mgrankvi.client.TimeSelectorClientRpc;
import org.percepta.mgrankvi.client.TimeSelectorServerRpc;
import org.percepta.mgrankvi.client.TimeSelectorState;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.LocalTime;

public class TimeSelector extends CustomField<LocalTime> {

    LocalTime value;

    private TimeSelectorServerRpc rpc = new TimeSelectorServerRpc() {

        @Override
        public void valueSelection(int hours, int minutes) {
            setValue(LocalTime.of(hours, minutes));
        }
    };

    public TimeSelector() {
        registerRpc(rpc);
    }

    @Override
    protected Component initContent() {
        return new CssLayout();
    }

    // We must override getState() to cast the state to MyComponentState
    @Override
    public TimeSelectorState getState() {
        return (TimeSelectorState) super.getState();
    }

    /**
     * Set if the clock should be displayed in 24h format or AM/PM
     * Note! hours will still be returned as 0 to 23
     *
     * @param twentyFour true - 24h format
     */
    public void setTwentyFour(boolean twentyFour) {
        getState().twentyfour = twentyFour;
    }

    /**
     * Get hour selection.
     *
     * @return the selected hour (from 0 to 23)
     */
    public int getHours() {
        return getValue().getHour();
    }

    /**
     * Get minute selection
     *
     * @return the currently selected minute (from 0 to 59)
     */
    public int getMinutes() {
        return getValue().getMinute();
    }

    public void setMinuteCaptions(Integer... minuteLabels) {
        getState().visibleMinutes = minuteLabels;
    }

    public void setMinuteSectors(int sectors) {
        getState().minuteSectors = sectors;
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

    @Override
    public LocalTime getValue() {
        return value;
    }

    @Override
    protected void doSetValue(LocalTime newFieldValue) {
        value = newFieldValue;
        setTime(newFieldValue.getHour(), newFieldValue.getMinute());

        fireChangeEvent(newFieldValue);
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
    public void fireChangeEvent(LocalTime value) {
        fireEvent(new SelectionChangeEvent(this, value.getHour(), value.getMinute()));
    }
}
