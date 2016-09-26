package org.percepta.mgrankvi.client.circle.select;

import com.vaadin.shared.communication.ServerRpc;

/**
 * @author Mikael Grankvist - Vaadin Ltd
 */
public interface CircleSelectServerRpc extends ServerRpc {

    void setSelection(Integer newValue);

    void valueHover(Integer hoverValue);
}
