package al.sybina.ebr.rcp.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

public class WelcomeHandler {
	
	@Execute
	public void printMessage() {
		System.out.println("WelcomeHandler printed....");
	}
}
