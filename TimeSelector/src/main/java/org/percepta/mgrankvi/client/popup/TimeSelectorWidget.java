package org.percepta.mgrankvi.client.popup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import org.percepta.mgrankvi.client.SelectionHandler;
import org.percepta.mgrankvi.client.Target;

public class TimeSelectorWidget extends Composite implements SelectionHandler {

    public static final String CLASS_NAME = "TimeSelector";

    private SelectionHandler selectionHandler;

    Label content;

    TimeSelectorPopupWidget selector;
    private boolean twentyFour = true;

    public TimeSelectorWidget() {
        content = new Label("12:00");
        content.setStyleName("time-label");
        content.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                selector.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                    public void setPosition(int offsetWidth, int offsetHeight) {
                        int left = (Window.getClientWidth() - offsetWidth) / 2;
                        int top = (Window.getClientHeight() - offsetHeight) / 2;
                        selector.setPopupPosition(left, top);
                    }
                });
            }
        });
        selector = new TimeSelectorPopupWidget(this);
        SimplePanel baseContent = new SimplePanel();
        baseContent.add(content);
        initWidget(baseContent);
        // CSS class-name should not be v- prefixed
        baseContent.setStyleName("c-" + CLASS_NAME);

    }

    public void addSelectionHandler(SelectionHandler selectionHandler) {
        this.selectionHandler = selectionHandler;
    }

    @Override
    public void timeSelection(int hour, int minute) {
        setTime(hour, minute);
        selectionHandler.timeSelection(hour, minute);
    }

    public void setTime(int hour, int minute) {
        NumberFormat formatter = NumberFormat.getFormat("00");
        content.setText(formatter.format(hour) + ":" + formatter.format(minute) + (twentyFour ? "" : " " + selector.getHalf()));
        selector.setTimeSelection(hour, minute);
    }

    /**
     * Set clock to 24h or 12h mode
     *
     * @param twentyFour true for 24h mode
     */
    public void setClockMode(boolean twentyFour) {
        this.twentyFour = twentyFour;
        selector.setClockMode(twentyFour);
        if (!twentyFour) {
            if (selector.getHourSelection() > 12) {
                setTime(selector.getHourSelection() - 12, selector.getMinuteSelection());
                selector.setHalf(Target.PM);
            } else {
                setTime(selector.getHourSelection(), selector.getMinuteSelection());
                selector.setHalf(Target.AM);
            }
        } else if (selector.getHalf().equals(Target.PM) && selector.getHourSelection() != 0) {
            setTime(selector.getHourSelection() + 12, selector.getMinuteSelection());
        } else {
            setTime(selector.getHourSelection(), selector.getMinuteSelection());
        }
    }

    public void setClockSize(int size) {
        selector.setClockSize(size);
    }

    public void setVisibleMinutes(Integer[] visibleMinutes) {
        selector.setVisibleMinutes(visibleMinutes);
    }

    public void setMinuteSectors(int minuteSectors) {
        selector.setMinuteSectors(minuteSectors);
    }
}