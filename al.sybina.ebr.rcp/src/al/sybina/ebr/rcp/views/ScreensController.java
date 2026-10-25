package al.sybina.ebr.rcp.views;
import java.util.HashMap;

import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 *
 */
public class ScreensController  extends StackPane {
	
	private static final Logger logger = LoggerCreator.createLogger(ScreensController.class);
    //Holds the screens to be displayed

    private HashMap<String, Node> screens = new HashMap<>();
    
    public ScreensController() {
        super();
    }

    //Add the screen to the collection
    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    //Returns the Node with the appropriate name
    public Node getScreen(String name) {
        return screens.get(name);
    }

    //Loads the fxml file, add the screen to the screens collection and
    //finally injects the screenPane to the controller.
    public boolean loadScreen(FXMLLoader loader, String name, String resource) {
        try {
        	FXMLLoader fXMLLoader = new FXMLLoader();
        	fXMLLoader.setClassLoader(loader.getClassLoader());
        	fXMLLoader.setControllerFactory(loader.getControllerFactory());
        	fXMLLoader.setLocation(getClass().getResource(resource));
            Parent loadScreen = (Parent) fXMLLoader.load();
            ControlledScreen myScreenControler = ((ControlledScreen) fXMLLoader.getController());
            myScreenControler.setScreenParent(this);
            addScreen(name, loadScreen);
            return true;
        } catch (Exception e) {
        	logger.error("Error loading screen! ", e);
            return false;
        }
    }

    //This method tries to displayed the screen with a predefined name.
    //First it makes sure the screen has been already loaded.  Then if there is more than
    //one screen the new screen is been added second, and then the current screen is removed.
    // If there isn't any screen being displayed, the new screen is just added to the root.
    public boolean setScreen(final String name) {       
        if (screens.get(name) != null) {   //screen loaded
            final DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty()) {    //if there is more than one screen
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(500), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                    	Node mainScreen = screens.get(LogbookHomeView.screenIdMain);
                    	getChildren().retainAll(mainScreen);
                    	if(screens.get(name)==mainScreen) {
                    		mainScreen.setOpacity(1.0);
                    		getChildren().remove(screens.get(name));
                    	}else {
                    		mainScreen.setOpacity(0.5);
                    	}
                        getChildren().add(screens.get(name));     //add the screen
                        Timeline fadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                new KeyFrame(new Duration(400), new KeyValue(opacity, 1.0)));
                        fadeIn.play();
                    }
                }, new KeyValue(opacity, 0.0)));
                fade.play();

            } else {
                setOpacity(0.0);
                getChildren().add(screens.get(name));       //no one else been displayed, then just show
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(1250), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            return true;
        } else {
            System.out.println("screen hasn't been loaded!!! \n");
            return false;
        }


        /*Node screenToRemove;
         if(screens.get(name) != null){   //screen loaded
         if(!getChildren().isEmpty()){    //if there is more than one screen
         getChildren().add(0, screens.get(name));     //add the screen
         screenToRemove = getChildren().get(1);
         getChildren().remove(1);                    //remove the displayed screen
         }else{
         getChildren().add(screens.get(name));       //no one else been displayed, then just show
         }
         return true;
         }else {
         System.out.println("screen hasn't been loaded!!! \n");
         return false;
         }*/
    }

    //This method will remove the screen with the given name from the collection of screens
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }
    
    public boolean isOnTop(String name) {
    	Node node = screens.get(name);
    	if(node!=null && node==getChildren().get(getChildren().size()-1)) {
    		return true;
    	}
    	return false;
    }
    public boolean contains(String name) {
    	Node node = screens.get(name);
    	if(node!=null) {
	    	for(Node n : getChildren()) {
	    		if(n==node) {
	    			return true;
	    		}
	    	}
    	}
    	return false;
    }
}