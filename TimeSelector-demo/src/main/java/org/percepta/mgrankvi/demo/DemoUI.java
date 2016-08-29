package org.percepta.mgrankvi.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
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
        content.addComponent(component);
        contentLayout.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
        setContent(contentLayout);

    }

}
