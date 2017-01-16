package org.percepta.mgrankvi;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import org.percepta.mgrankvi.client.popup.TimeSelectorClientRpc;
import org.percepta.mgrankvi.client.popup.TimeSelectorServerRpc;
import org.percepta.mgrankvi.client.popup.TimeSelectorState;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeSelector extends CustomField<Date> {

    int hour = 0;
    int minute = 0;

    // To process events from the client, we implement ServerRpc
    private TimeSelectorServerRpc rpc = new TimeSelectorServerRpc() {

        @Override
        public void valueSelection(int hours, int minutes) {
            hour = hours;
            minute = minutes;
            Calendar cal = GregorianCalendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hours);
            cal.set(Calendar.MINUTE, minutes);
            setValue(cal.getTime());
            fireChangeEvent(hours, minutes);
        }
    };

    public TimeSelector() {
        registerRpc(rpc);
    }

    @Override
    public Class<? extends Date> getType() {
        return Date.class;
    }

    @Override
    protected Component initContent() {
        return new CssLayout();
    }

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
        return getValue().getHours();
    }

    /**
     * Get minute selection
     *
     * @return the currently selected minute (from 0 to 59)
     */
    public int getMinutes() {
        return getValue().getMinutes();
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
    public void setValue(Date newFieldValue) throws ReadOnlyException, Converter.ConversionException {
        super.setValue(newFieldValue);
        setTime(newFieldValue.getHours(), newFieldValue.getMinutes());
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
        void selectionChanged(SelectionChangeEvent event);
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
