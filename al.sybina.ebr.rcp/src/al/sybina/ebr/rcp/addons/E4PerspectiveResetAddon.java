package al.sybina.ebr.rcp.addons;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.UIEvents.ElementContainer;
import org.eclipse.e4.ui.workbench.UIEvents.EventTags;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.osgi.service.event.Event;


/**
 * An add-on which enables a perspective restore/reset in E4.
 * 
 * This add-on will store the perspective states in the application snippet container. It will also
 * register an {@link EPerspectiveRestoreService} implementation in the {@link IEclipseContext} of
 * the application which is able to restore them.
 */
@SuppressWarnings("restriction")
/* EventTopic and IEclipseContext which are needed. */
public class E4PerspectiveResetAddon {

	private static final Logger logger = LoggerCreator.createLogger(E4PerspectiveResetAddon.class);
	/**
	 * Initializes/Registers an {@link EPerspectiveRestoreService} which is able to restore
	 * {@link MPerspective} states which are stored by this add-on.
	 * 
	 * @param eclipseContext
	 *            the {@link IEclipseContext} of the application
	 */
	@PostConstruct
	public void init(IEclipseContext eclipseContext) {
		eclipseContext.set(SnippetBasedPerspectiveRestoreService.class,
				new SnippetBasedPerspectiveRestoreService(eclipseContext));
	}

	/**
	 * Event handling method which will store the state of a newly opened perspective in the
	 * application snippet container.
	 * 
	 * <p>
	 * This method will check if there is already a perspective snippet stored for the newly opened
	 * perspective. If this isn't the case it will create a new snippet of the perspective in the
	 * application snippet container, otherwise it will do nothing.
	 * </p>
	 * 
	 * @param event
	 *            the event send by the event broker
	 * @param application
	 *            the current application
	 * @param modelService
	 *            the model service of the application (used to find and create the snippets)
	 */
	@Inject
	public void keepState(
			@Optional @EventTopic(ElementContainer.TOPIC_SELECTEDELEMENT) Event event,
			MApplication application, EModelService modelService) {
		// shortcuts to leave the method a soon as possible
		if (event == null)
			return;

		if (!UIEvents.isSET(event))
			return;

		if (!(event.getProperty(EventTags.ELEMENT) instanceof MPerspectiveStack))
			return;

		Object perspectiveToDisp = event.getProperty(EventTags.NEW_VALUE);

		if (perspectiveToDisp instanceof MPerspective) {
			MPerspective perspective = (MPerspective) perspectiveToDisp;

			if (modelService.findSnippet(application, perspective.getElementId()) == null) {
				// no snippet exists so far, create a new one
				//modelService.cloneElement(perspective, application);
				clonePerspectiveWithWorkaround(modelService, perspective, application);
			}
		}
	}

	/**
	 * An {@link EPerspectiveRestoreService} implementation which will restore an MPerspective from
	 * the application snippet container.
	 */
	public static final class SnippetBasedPerspectiveRestoreService {

		/**
		 * The application IEclipseContext which will be used to find the snippet.
		 */
		private final IEclipseContext appContext;

		/**
		 * Sole Constructor.
		 * 
		 * @param appContext
		 *            the application {@link IEclipseContext}
		 * @throws IllegalArgumentException
		 *             if the given context is <code>null</code>
		 */
		public SnippetBasedPerspectiveRestoreService(IEclipseContext appContext) {
			if (appContext == null) {
				throw new IllegalArgumentException("No IEclipseContext given!"); //$NON-NLS-1$
			}

			this.appContext = appContext;
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * Uses the application snippet container to retrieve/reload a {@link MPerspective} state.
		 * </p>
		 */
		public MPerspective reloadPerspective(String perspectiveID, MWindow window) {
			if (window == null || perspectiveID == null)
				return null;


			EModelService modelService = appContext.get(EModelService.class);
			if (modelService == null)
				return null;

			MApplication application = appContext.get(MApplication.class);
			MUIElement storedPerspState = modelService.cloneSnippet(application, perspectiveID,
					window);
			if (storedPerspState instanceof MPerspective) {
				return (MPerspective) storedPerspState;
			}

			return null;
		}

	}
	
	/**
	 * Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=481996
	 * Set the references of the placeholder to the corresponding parts again in the copy.
	 */
	private static MPerspective clonePerspectiveWithWorkaround(EModelService modelService, MPerspective original, MApplication application) {
		MPerspective clone = (MPerspective) modelService.cloneElement(original, null);
		clone.setElementId(original.getElementId());

		List<MPlaceholder> placeholderClones = modelService.findElements(clone, null, MPlaceholder.class, null, EModelService.IN_ANY_PERSPECTIVE);
		// For each placeholder in the new perspective, set the reference value to the one from the old perspective
		for (MPlaceholder placeholderClone : placeholderClones) {
			// Search for the corresponding placeholder in the "old" perspective
			List<MPlaceholder> placeholderOriginal = modelService.findElements(original, placeholderClone.getElementId(), MPlaceholder.class, null,
					EModelService.IN_ANY_PERSPECTIVE);
			if (placeholderOriginal.size() == 1) {
				// We found only one corresponding placeholder element. Set reference of old element to the new element
				placeholderClone.setRef((placeholderOriginal.get(0).getRef()));
			} else if (placeholderOriginal.isEmpty()) {
				logger.error("Messages.PerspectiveController_err_no_placeholder");
			} else {
				logger.error("Messages.PerspectiveController_err_more_than_one_placeholder" + " " + placeholderOriginal.toString()); //$NON-NLS-1$
				placeholderClone.setRef((placeholderOriginal.get(0).getRef()));
			}
		}
		
		if (application != null) {
			MUIElement snippet = modelService.findSnippet(application, original.getElementId());
			if (snippet != null) {
				application.getSnippets().remove(snippet);
			}
			application.getSnippets().add(clone);
		}
		
		return clone;
	}
}
