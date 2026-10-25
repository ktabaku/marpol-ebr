package al.sybina.ebr.rcp.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.internal.events.EventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.fx.core.di.LocalInstance;
import org.osgi.service.event.Event;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class LogbookHome {

	@Inject
	private MWindow window;
	@Inject
	private EModelService modelService;
	@Inject
	private EHandlerService handlerService;
	@Inject
	private ECommandService commandService;
	@Inject
	private IEclipseContext ctx;
	
	public static String screenIdMain = "mainScreen";
    public static String screenFileMain = "LogbookHome.fxml";

    
    private ScreensController mainContainer;

	@PostConstruct
	public void createPartControl(BorderPane borderPane, @LocalInstance FXMLLoader loader, EventBroker eventBroker, EPartService partService, MPart thisPart) {
		
		
		mainContainer = new ScreensController();
	    mainContainer.loadScreen(loader, LogbookHome.screenIdMain, LogbookHome.screenFileMain);

	    
	    
	    mainContainer.setScreen(LogbookHomeView.screenIdMain);
	    borderPane.setCenter(mainContainer);
	    
	    

	}
	@Inject
	@Optional
	public void handleBringToTop(@UIEventTopic(UIEvents.UILifeCycle.BRINGTOTOP) Event event, MPart thisPart) {
		if (thisPart == event.getProperty(UIEvents.EventTags.ELEMENT)) {
			if(!mainContainer.isOnTop(LogbookHomeView.screenIdMain))
				mainContainer.setScreen(LogbookHomeView.screenIdMain);
		}
	}
}
