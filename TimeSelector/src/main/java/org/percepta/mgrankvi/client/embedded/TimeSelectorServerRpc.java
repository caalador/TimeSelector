package org.percepta.mgrankvi.client.embedded;

import com.vaadin.shared.communication.ServerRpc;
import org.percepta.mgrankvi.client.Target;

public interface TimeSelectorServerRpc extends ServerRpc {
    void setHourAndMinute(int hours, int minutes);
    void setHalf(Target half);
}
