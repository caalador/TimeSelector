package org.percepta.mgrankvi.client.embedded;

import org.percepta.mgrankvi.client.Target;

public interface WidgetCallback {
    void timeSelected(int hour, int minute);
    void setTarget(Target target);
}
