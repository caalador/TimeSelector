package org.percepta.mgrankvi.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class TimeSelectorWidget extends Composite implements SelectionHandler {//} implements CircleSelectCallback {

    public static final String CLASS_NAME = "TimeSelector";

    private SelectionHandler selectionHandler;

    Label content;

    TimeSelectorPopupWidget selector;

    public TimeSelectorWidget() {

        content = new Label("12:00");
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
        baseContent.getElement().getStyle().setBackgroundColor("white");
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
        content.setText(formatter.format(hour) + ":" + formatter.format(minute));
        selector.setTimeSelection(hour, minute);
    }


}