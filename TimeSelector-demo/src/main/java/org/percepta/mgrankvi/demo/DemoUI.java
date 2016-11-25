package org.percepta.mgrankvi.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.percepta.mgrankvi.CircleSelect;
import org.percepta.mgrankvi.TimeSelector;

import javax.servlet.annotation.WebServlet;
import java.util.Date;

@Theme("demo")
@Title("MyComponent Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "org.percepta.mgrankvi.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
        @Override
        protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
            return super.createServletService(deploymentConfiguration);
        }
    }

    boolean twentyFour = true;

    @Override
    protected void init(VaadinRequest request) {

        // Initialize our new UI component
        final TimeSelector component = new TimeSelector();
        component.addSelectionChangeListener(new TimeSelector.SelectionChangeListener() {

            @Override
            public void selectionChanged(TimeSelector.SelectionChangeEvent event) {
                System.out.println(event.getHours() + ":" + event.getMinutes());
            }
        });

        component.setValue(new Date());

        // Show it in the middle of the screen
        final VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setStyleName("demoContentLayout");
        contentLayout.setSizeFull();

        VerticalLayout content = new VerticalLayout();
        content.setSizeUndefined();
        content.setSpacing(true);
        content.setCaption("TimeSelector");
        contentLayout.addComponent(content);
        content.addComponent(new Button("am/pm", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                twentyFour = !twentyFour;
                component.setTwentyFour(twentyFour);
                if (twentyFour) {
                    clickEvent.getButton().setCaption("Switch to am/pm");
                } else {
                    clickEvent.getButton().setCaption("Switch to twenty four");
                }
            }
        }));
        content.addComponent(new Button("Restrict minutes", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if(clickEvent.getButton().getCaption().equals("Restrict minutes")) {
                    component.setMinuteCaptions(15, 30, 45, 00);
                    component.setMinuteSectors(4);
                    clickEvent.getButton().setCaption("Unrestrict minutes");
                } else {
                    component.setMinuteCaptions(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 00);
                    component.setMinuteSectors(60);
                    clickEvent.getButton().setCaption("Restrict minutes");
                }
            }
        }));
        content.addComponent(component);

        HorizontalLayout circles = new HorizontalLayout();
        circles.setSpacing(true);
        circles.setCaption("CircleSelect as freestanding component");

        final CircleSelect defaultCircle = new CircleSelect();
        defaultCircle.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                Notification.show("Selected from default: " + defaultCircle.getValue());
            }
        });
        Label defaultLabel = new Label("Default settings");
        circles.addComponent(new VerticalLayout(defaultCircle,defaultLabel));

        final CircleSelect minutes = new CircleSelect();
        minutes.setSectors(60);
        minutes.setVisibleValues(5,10,15,20,25,30,35,40,45,50,55,0);
        minutes.setValue(17);
        minutes.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                Notification.show("Selected from minutes: " + minutes.getValue());
            }
        });
        Label minutesLabel = new Label("Sectors: 60<br/>Values: 5,10,15,20,25,30,35,40,45,50,55,0<br/>Note! 0 returns 60", ContentMode.HTML);
        circles.addComponent(new VerticalLayout(minutes,minutesLabel));

        final CircleSelect fullDay = new CircleSelect();
        fullDay.setSectors(12);
        fullDay.setVisibleValues(1,2,3,4,5,6,7,8,9,10,11,0);
        fullDay.setInnerValues(13,14,15,16,17,18,19,20,21,22,23,24);
        fullDay.setValue(18);
        fullDay.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                Notification.show("Selected from full day: " + fullDay.getValue());
            }
        });
        Label fullDayLabel = new Label("Sectors: 12<br/>Values: 1,2,3,4,5,6,7,8,9,10,11,0<br/>Inner values: 13,14,15,16,17,18,19,20,21,22,23,24" +
                "<br/>Note! values are returned the same as the labels.", ContentMode.HTML);
        circles.addComponent(new VerticalLayout(fullDay,fullDayLabel));

        final CircleSelect restricted = new CircleSelect();
        restricted.setVisibleValues(15,30,45,0);
        restricted.setSectors(4);
        restricted.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                Notification.show("Selected from restricted: " + restricted.getValue());
            }
        });
        Label restrictedLabel = new Label("Sectors: 4<br/>Values: 15,30,45,0", ContentMode.HTML);
        circles.addComponent(new VerticalLayout(restricted,restrictedLabel));

        contentLayout.addComponent(circles);
        contentLayout.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        contentLayout.setComponentAlignment(circles, Alignment.MIDDLE_CENTER);
        setContent(contentLayout);

    }

}
