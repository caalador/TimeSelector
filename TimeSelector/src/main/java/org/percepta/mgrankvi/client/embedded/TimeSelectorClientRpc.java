package org.percepta.mgrankvi.client.embedded;

import com.vaadin.shared.communication.ClientRpc;
import org.percepta.mgrankvi.client.Target;

public interface TimeSelectorClientRpc extends ClientRpc {
    void setHourAndMinute(int hours, int minutes);
    void setHalf(Target half);
}
