package al.sybina.ebr.core.events;

import java.util.EventObject;

import al.sybina.ebr.core.warnings.CoreWarningMessage;

public class WarningEvent extends EventObject {

	public CoreWarningMessage message;
	public WarningEvent(Object source, CoreWarningMessage msg) {
		super(source);
		message = msg;
	}

}
