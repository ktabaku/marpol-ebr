package al.sybina.ebr.rcp.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.modeling.EPartService;


public class SaveAllHandler {
	@Execute
	public void execute(EPartService service, IEventBroker broker) {
		service.saveAll(false);
		broker.post("IBR_SAVE_ALL", "saved");
	}

	@CanExecute
	boolean canExecute(@Optional EPartService partService) {
		if (partService != null) {
			return !partService.getDirtyParts().isEmpty();
		}
		return false;
	}

}
