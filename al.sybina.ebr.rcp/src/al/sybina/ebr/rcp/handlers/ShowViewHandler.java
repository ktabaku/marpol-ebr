package al.sybina.ebr.rcp.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.fx.ui.workbench.fx.E4Application;

public class ShowViewHandler {
	@Execute
	public void execute(EPartService partService, EModelService modelService, MApplication application,
			@Named("org.eclipse.ui.views.showView.viewId") String viewId) {
		MPart part = (MPart) modelService.find(viewId, application);
		if(part != null) {
			part.getParent().setSelectedElement(part);
		} else {
			partService.showPart(viewId, PartState.ACTIVATE);
		}
	}
	
}
