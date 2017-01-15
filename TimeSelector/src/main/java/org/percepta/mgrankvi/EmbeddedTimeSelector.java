package org.percepta.mgrankvi;

import com.vaadin.ui.AbstractComponent;
import org.percepta.mgrankvi.client.Target;
import org.percepta.mgrankvi.client.embedded.TimeSelectorClientRpc;
import org.percepta.mgrankvi.client.embedded.TimeSelectorServerRpc;
import org.percepta.mgrankvi.client.embedded.TimeSelectorState;

public class EmbeddedTimeSelector extends AbstractComponent {

    int hour, minute;
    Target half = Target.AM;

    private TimeSelectorServerRpc rpc = new TimeSelectorServerRpc() {
        @Override
        public void setHourAndMinute(int hours, int minutes) {
            hour = hours;
            minute = minutes;
            setTime(hours, minutes);
        }

        @Override
        public void setHalf(Target half) {
            EmbeddedTimeSelector.this.half = half;
            EmbeddedTimeSelector.this.setHalf(half);
        }
    };

    public EmbeddedTimeSelector() {
        registerRpc(rpc);
    }

    @Override
    public TimeSelectorState getState() {
        return (TimeSelectorState) super.getState();
    }

    public void setTime(int hour, int minute) {
        getRpcProxy(TimeSelectorClientRpc.class).setHourAndMinute(hour, minute);
    }

    public void setSelectorColor(String color) {
        getState().selectorColor = color;
    }

    public void setFillColor(String color) {
        getState().fillColor = color;
    }

    public void setHalf(Target target) {
        getRpcProxy(TimeSelectorClientRpc.class).setHalf(target);
    }

    public void setTimeFormat(boolean isTwentyFourFormat) {
        getState().twentyFourTimeFormat = isTwentyFourFormat;
    }

    public int getHours() {
        return hour;
    }

    public int getMinutes() {
        return minute;
    }

    public void setMinuteCaptions(Integer... minuteLabels) {
        getState().visibleMinutes = minuteLabels;
    }

    public void setMinuteSectors(int sectors) {
        getState().minuteSectors = sectors;
    }

    public void setClockSize(int size) {
        getState().clockSize = size;
    }

    public boolean isTwentyFourFormat() {
        return getState().twentyFourTimeFormat;
    }

    public Target getHalf() {
        return half;
    }
}
