package al.sybina.ebr.commons.utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;



@Creatable
@Singleton
public class ViewPerspectiveHelper {
	
	@Inject
	private EHandlerService handlerService;
	@Inject
	private ECommandService commandService;
	@Inject
	private EPartService partService;
	@Inject
	private IEclipseContext context;
	
	@Inject
	private EModelService modelService;
	@Inject
	private MApplication application;
	
	private static final Logger logger = LoggerCreator.createLogger(ViewPerspectiveHelper.class);
	
	@SuppressWarnings("restriction")
	public void showView(String viewId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("org.eclipse.ui.views.showView.viewId", viewId);
		ParameterizedCommand cmd = commandService.createCommand("al.sybina.ebr.rcp.command.showView", parameters);
		try {
			handlerService.executeHandler(cmd, context);
		} catch (Exception e1) {
			logger.error("cannot open view " + viewId, e1);
		}
	}
	
	@SuppressWarnings("restriction")
	public void goToPerspective(String perspectiveID) {
		java.util.Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("org.eclipse.ui.perspectives.showPerspective.perspectiveId", perspectiveID);
		ParameterizedCommand cmd = commandService.createCommand("al.sybina.ebr.rcp.command.switchperspective",
				parameters);
		try {
			handlerService.executeHandler(cmd, context);
		} catch (Exception e1) {
			logger.error("cannot open perspective " + perspectiveID, e1);
		}
	}

	@SuppressWarnings("restriction")
	public boolean exit() {
		ParameterizedCommand cmd = commandService.createCommand("org.eclipse.ui.file.exit", null);
		try {
			Object result = handlerService.executeHandler(cmd, context);
			return result instanceof Boolean ? (Boolean)result : null;
		} catch (Exception ex) {
			logger.error("cannot exit ibr ", ex);
			return false;
		}
	}
	
	@SuppressWarnings("restriction")
	public boolean restart() {
		ParameterizedCommand cmd = commandService.createCommand("org.eclipse.ui.file.restartWorkbench", null);
		try {
			Object result = handlerService.executeHandler(cmd, context);
			return result instanceof Boolean ? (Boolean)result : null;
		} catch (Exception ex) {
			logger.error("cannot exit ibr ", ex);
			return false;
		}
	}
	@SuppressWarnings("restriction")
	public boolean restart(String message) {
		java.util.Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("al.sybina.ebr.rcp.commandparameter.restartmessage", message);
		ParameterizedCommand cmd = commandService.createCommand("org.eclipse.ui.file.restartWorkbench", parameters);
		try {
			Object result = handlerService.executeHandler(cmd, context);
			return result instanceof Boolean ? (Boolean)result : null;
		} catch (Exception ex) {
			logger.error("cannot exit ibr ", ex);
			return false;
		}
	}
	
	public void setViewToBeRenderedAndVisible(String viewId, boolean visible) {
		MPart part = (MPart) modelService.find(viewId, application);
		if(part != null) {
			part.setVisible(visible);
//			part.setToBeRendered(visible);
//			part.getParent().getChildren().remove(part);
		}
	}

	public void setPlaceholderVisible(String placehoderId, boolean visible) {
		MPlaceholder mPlaceholder = (MPlaceholder) modelService.find(placehoderId, application);
		if(mPlaceholder != null) {
			mPlaceholder.setVisible(visible);
		}
	}

	public String getActivePerspectiveId() {
		MWindow mwindow = (MWindow)modelService.find("IDEWindow", application);
		MPerspective persp = modelService.getActivePerspective(mwindow);
		return persp.getElementId();
	}

}
