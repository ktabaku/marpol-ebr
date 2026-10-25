package al.sybina.ebr.core.events;

import java.util.EventObject;

public abstract class ChartEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	public ChartEvent(Object source) {
		super(source);
	}

}
