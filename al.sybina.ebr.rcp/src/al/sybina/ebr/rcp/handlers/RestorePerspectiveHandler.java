package al.sybina.ebr.rcp.handlers;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import al.sybina.ebr.rcp.addons.E4PerspectiveResetAddon.SnippetBasedPerspectiveRestoreService;

public class RestorePerspectiveHandler {

	@Execute
	public void execute(EModelService modelService, MWindow window,
			EPartService partService, IEclipseContext appContext) {
		MPerspective persp = modelService
				.getActivePerspective(window);
		modelService.resetPerspectiveModel(persp, window);
		
		/*
		 * Restore of the state is called only in the public method, because the private
		 * resetPerspectiveModel() method is also called during removePerspectiveModel() which will
		 * remove the Perspective anyhow and so a previous reset to the old state isn't necessary.
		 */
		
		SnippetBasedPerspectiveRestoreService restoreService = appContext
				.get(SnippetBasedPerspectiveRestoreService.class);
		if (restoreService == null)
			return;
		
		// restore the old state (will only work if an add-on added an EPerspectiveRestoreService
		// implementation)
		MPerspective state = restoreService.reloadPerspective(persp.getElementId(), window);
		if (state != null) {
			boolean wasPerspectiveActive = (persp.getParent().getSelectedElement() == persp);
			/*
			 * Un-render the perspective (destroy the parts) must be done before it is replaced,
			 * because otherwise the @PreDestroy methods on the parts are not invoked! (shared
			 * elements are excluded if they are already opened in other perspectives)
			 */
			persp.setToBeRendered(false);
		
			// replace the current perspective with the stored state
			EcoreUtil.replace((EObject) persp, (EObject) state);
		
			if (wasPerspectiveActive) { // switch to perspective only if it was active
				/*
				 * Activate the restored perspective This will re-create the parts based on all the
				 * model settings which exists before the perspective was changed. (shared elements
				 * are reused if they are already opened in other perspectives)
				 */
				IEclipseContext context = window.getContext();
				if (context == null) {
					/*
					 * sometimes the window doesn't have a context and to prevent a NPE just try our
					 * luck with the application context
					 */
					context = appContext;
				}
				EPartService ps = context.get(EPartService.class);
				ps.switchPerspective(state); // no null-check, because we want to fail early
			}
		}
	}
	
	
//	@CanExecute
//	public boolean canExecute(EModelService modelService, MWindow window) {
//		MPerspective activePerspective = modelService
//				.getActivePerspective(window);
//		return modelService.findSnippet(window,
//				activePerspective.getElementId()) != null;
//	}
		
}