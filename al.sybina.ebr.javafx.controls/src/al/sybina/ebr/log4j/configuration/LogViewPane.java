package al.sybina.ebr.log4j.configuration;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

import static al.sybina.ebr.log4j.configuration.LogView.MAX_LINES;
import static java.util.stream.Collectors.toList;

/**
 * A container of {@link LogView}s.
 */
public final class LogViewPane {

    private static final Logger log = LoggerFactory.getLogger( LogViewPane.class );

    private final SplitPane pane = new SplitPane();

    // a simple observable that changes state every time a change occurs in a pane divider
    // (changes in number of dividers as well as in their positions)
    private final ObjectProperty<Boolean> panesDividersObservable = new SimpleObjectProperty<>( false );

    
    public LogViewPane() {
        MenuItem copyMenuItem = new MenuItem( "Copy Selection" );
        copyMenuItem.setAccelerator( new KeyCodeCombination( KeyCode.C, KeyCombination.SHORTCUT_DOWN ) );
        copyMenuItem.setOnAction( event -> getFocusedView().ifPresent( wrapper ->
                wrapper.logView.getSelection().ifPresent( content -> {
                    Clipboard.getSystemClipboard().setContent( content );
                } ) ) );

        MenuItem pauseMenuItem = new MenuItem( "Pause/Resume file auto-refresh" );
        pauseMenuItem.setAccelerator( new KeyCodeCombination( KeyCode.P, KeyCombination.SHORTCUT_DOWN ) );
        pauseMenuItem.setOnAction( ( event ) ->
                getFocusedView().ifPresent( view -> view.header.togglePauseRefresh() ) );

        MenuItem goToDateMenuItem = new MenuItem( "To date-time" );
        goToDateMenuItem.setAccelerator( new KeyCodeCombination( KeyCode.G, KeyCombination.SHORTCUT_DOWN ) );
        goToDateMenuItem.setOnAction( event -> {
            Optional<LogViewWrapper> wrapper = getFocusedView();
            if ( wrapper.isPresent() ) {
                wrapper.get().toDateTime();
            } else {
                new GoToDateView( null,  null).show();
            }
        } );

        MenuItem toTopMenuItem = new MenuItem( "To top of file" );
        toTopMenuItem.setAccelerator( new KeyCodeCombination( KeyCode.T,
                KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN ) );
        toTopMenuItem.setOnAction( event -> getFocusedView().ifPresent( LogViewWrapper::toTop ) );

        MenuItem pageUpMenuItem = new MenuItem( "Page Up" );
        pageUpMenuItem.setAccelerator( new KeyCodeCombination( KeyCode.U, KeyCombination.SHORTCUT_DOWN ) );
        pageUpMenuItem.setOnAction( event -> getFocusedView().ifPresent( LogViewWrapper::pageUp ) );

        MenuItem pageDownMenuItem = new MenuItem( "Page Down" );
        pageDownMenuItem.setAccelerator( new KeyCodeCombination( KeyCode.D, KeyCombination.SHORTCUT_DOWN ) );
        pageDownMenuItem.setOnAction( event -> getFocusedView().ifPresent( LogViewWrapper::pageDown ) );

        MenuItem tailMenuItem = new MenuItem( "Tail file (on/off)" );
        tailMenuItem.setAccelerator( new KeyCodeCombination( KeyCode.T, KeyCombination.SHORTCUT_DOWN ) );
        tailMenuItem.setOnAction( event -> getFocusedView()
                .ifPresent( LogViewWrapper::switchTailFile ) );

        pane.setContextMenu( new ContextMenu(
                copyMenuItem,
                new SeparatorMenuItem(),
                toTopMenuItem, tailMenuItem, pageUpMenuItem, pageDownMenuItem, goToDateMenuItem,
                new SeparatorMenuItem(),
                pauseMenuItem) );

    }



    public void showContextMenu() {
        Optional.ofNullable( pane.getContextMenu() )
                .ifPresent( menu -> menu.show( pane.getScene().getWindow() ) );
    }


    
    public DoubleProperty prefHeightProperty() {
        return pane.prefHeightProperty();
    }

    
    public Node getNode() {
        return pane;
    }


    
    public void add( LogView logView) {
        LogViewWrapper logViewWrapper = new LogViewWrapper( logView );

		pane.getItems().add(logViewWrapper);
    }


    
    private Optional<LogViewWrapper> getFocusedView() {
        Node node = pane.getScene().focusOwnerProperty().get();

        // go up in the hierarchy until we find a wrapper, or just hit null
        while ( node != null && !( node instanceof LogViewWrapper ) ) {
            node = node.getParent();
        }

        if ( node != null ) {
            return Optional.of( ( LogViewWrapper ) node );
        } else {
            return Optional.empty();
        }
    }


    
    public void focusOn( File file ) {
        for ( Node item : pane.getItems() ) {
            if ( item instanceof LogViewWrapper ) {
                if ( ( ( LogViewWrapper ) item ).logView.getFile().equals( file ) ) {
                    item.requestFocus();
                    break;
                }
            }
        }
    }




    
    public ScrollPane getScrollPaneFor( LogView view ) {
        for ( Node item : pane.getItems() ) {
            if ( item instanceof LogViewWrapper ) {
                LogViewWrapper wrapper = ( LogViewWrapper ) item;
                if ( wrapper.getLogView() == view ) {
                    return wrapper.getScrollPane();
                }
            }
        }

        return null;
    }

    private static class LogViewScrollPane extends ScrollPane {

        LogViewScrollPane( LogViewWrapper wrapper ) {
            super( wrapper.logView );
            setFocusTraversable( true );

            wrapper.header.tailFileProperty().addListener( observable -> {
                if ( wrapper.header.tailFileProperty().get() ) {
                    setVvalue( 1.0 );
                }
            } );

            setOnScroll( event -> {
                double deltaY = event.getDeltaY();
                if ( deltaY > 0.0 ) {
                    wrapper.stopTailingFile(); // stop tailing when user scrolls up
                } else if ( wrapper.isTailingFile() ) {
                    return; // no need to scroll down when tailing file
                }

                switch ( event.getTextDeltaYUnits() ) {
                    case NONE:
                        // no change
                        break;
                    case LINES:
                        deltaY *= 10.0;
                        break;
                    case PAGES:
                        deltaY *= 50.0;
                        break;
                }
                wrapper.logView.move( deltaY );
            } );
        }

    }

    static class LogViewWrapper extends VBox {

        private static final Logger log = LoggerFactory.getLogger( LogViewWrapper.class );

        private final LogView logView;
        private final LogViewHeader header;
        private final LogViewScrollPane scrollPane;

        public LogViewWrapper( LogView logView) {
            super( 2.0 );

            this.logView = logView;

            this.header = new LogViewHeader( logView, this::toDateTime );

            this.scrollPane = new LogViewScrollPane( this );

            getChildren().setAll( header, scrollPane );

            header.tailFileProperty().bindBidirectional( logView.tailingFileProperty() );
            logView.allowRefreshProperty().bind( header.pauseRefreshProperty().not() );

            logView.setOnFileExists( ( fileExists ) -> {
                if ( fileExists ) {
                    if ( !isShowingFileContents() ) {
                        log.debug( "Showing FileContents pane for file: {}", logView.getFile() );
                        Platform.runLater( () -> getChildren().setAll( header, scrollPane ) );
                    }
                } else {
                    if ( isShowingFileContents() ) {
                        log.debug( "Showing FileDoesNotExist pane for file: {}", logView.getFile() );
                        Platform.runLater( () -> getChildren().setAll( header, new FileDoesNotExistPane() ) );
                    }
                }
            } );

            logView.loadFileContents();
        }

        LogView getLogView() {
            return logView;
        }

        public LogViewScrollPane getScrollPane() {
            return scrollPane;
        }

        private boolean isShowingFileContents() {
            return getChildren().size() > 1 && getChildren().get( 1 ) instanceof LogViewScrollPane;
        }

        
        void toDateTime() {
            stopTailingFile();
            GoToDateView goToView = new GoToDateView( logView, this );
            goToView.show();
        }

        
        void toTop() {
            stopTailingFile();
            logView.toTop();
            scrollPane.setVvalue( 0.0 );
        }

        
        void pageUp() {
            stopTailingFile();
            logView.pageUp();
        }

        
        void pageDown() {
            if ( !isTailingFile() ) {
                logView.pageDown();
            }
        }

        
        private void startTailingFile() {
            if ( !isTailingFile() ) {
                log.debug( "Starting tailing file" );
                header.tailFileProperty().setValue( true );
            }
        }

        
        private void stopTailingFile() {
            if ( isTailingFile() ) {
                log.debug( "Stopping tailing file" );
                header.tailFileProperty().setValue( false );
            }
        }

        
        boolean isTailingFile() {
            return header.tailFileProperty().get();
        }

        
        void closeView() {
            try {
                logView.closeFileReader();
            } finally {
            	this.stop();;
            }
        }

        
        void stop() {
            // do not call onClose as this is not closing the view, just stopping the app
            logView.closeFileReader();
        }

        
        void switchTailFile() {
            if ( isTailingFile() ) {
                stopTailingFile();
            } else {
                startTailingFile();
            }
        }

        
        void scrollTo( int lineNumber ) {
            double vvalue = lineNumberToScrollVvalue( lineNumber );

            log.debug( "Setting scroll to {} for line number {}", vvalue, lineNumber );
            scrollPane.setVvalue( vvalue );
        }

    }

    static double lineNumberToScrollVvalue( double lineNumber ) {
        double frac = ( lineNumber / MAX_LINES );
        return BigDecimal.valueOf( frac )
                .setScale( 1, frac < 0.5 ? RoundingMode.DOWN : RoundingMode.UP )
                .doubleValue();
    }

    private static class LogViewHeader extends BorderPane {

        private final BooleanProperty tailFile;
        private final BooleanProperty pauseRefresh;

        LogViewHeader( LogView logView, Runnable goToDateTime ) {
            setMinWidth( 10.0 );

            File file = logView.getFile();

            HBox leftAlignedBox = new HBox( 2.0 );
            HBox rightAlignedBox = new HBox( 2.0 );

//            Button fileNameLabel = new Button();
//            fileNameLabel.setMinWidth( 5.0 );
//            fileNameLabel.setTooltip( new Tooltip( file.getAbsolutePath() ) );
//
//            Runnable updateFileLabel = () -> {
//                if ( file.exists() ) {
//                    final double fileLength = ( double ) file.length();
//                    final String fileSizeText;
//                    if ( fileLength < 10_000 ) {
//                        fileSizeText = String.format( "(%.0f bytes)", fileLength );
//                    } else {
//                        double fileSize = fileLength / 1_000_000.0;
//                        fileSizeText = String.format( "(%.2f MB)", fileSize );
//                    }
//
//                    Platform.runLater( () ->
//                            fileNameLabel.setText( file.getName() + " " + fileSizeText ) );
//                } else {
//                    Platform.runLater( () -> fileNameLabel.setText( file.getName() ) );
//                }
//            };

//            logView.onFileUpdate( updateFileLabel );
//            updateFileLabel.run();
//
//            leftAlignedBox.getChildren().add( fileNameLabel );
            Button goToDateButton = new Button("", new Glyph("FontAwesome", FontAwesome.Glyph.CLOCK_ALT));
            goToDateButton.setTooltip( new Tooltip( "Go to date-time" ) );
            goToDateButton.setOnAction( event -> goToDateTime.run() );

            ToggleButton tailFileButton = new ToggleButton("", new Glyph("FontAwesome", FontAwesome.Glyph.ARROW_DOWN) );
            tailFileButton.setTooltip( new Tooltip( "Tail file" ) );
            this.tailFile = tailFileButton.selectedProperty();
            this.tailFile.addListener( event -> {
                if ( tailFile.get() ) {
                    pauseRefreshProperty().set( false );
                }
            } );
            ToggleButton pauseRefreshButton = new ToggleButton("", new Glyph("FontAwesome", FontAwesome.Glyph.PAUSE) );
            pauseRefreshButton.setTooltip( new Tooltip( "Pause file auto-refresh.\n" +
                    "Disables moving up/down and all file reads." ) );
            this.pauseRefresh = pauseRefreshButton.selectedProperty();
            this.pauseRefresh.addListener( event -> {
                if ( pauseRefresh.get() ) {
                    tailFile.set( false );
                }
            } );

            leftAlignedBox.getChildren().addAll( goToDateButton, tailFileButton, pauseRefreshButton );

            setLeft( leftAlignedBox );
            setRight( rightAlignedBox );
        }

        BooleanProperty tailFileProperty() {
            return tailFile;
        }

        BooleanProperty pauseRefreshProperty() {
            return pauseRefresh;
        }

        
        void togglePauseRefresh() {
            pauseRefresh.set( !pauseRefresh.getValue() );
        }
    }

    private static class FileDoesNotExistPane extends StackPane {

        FileDoesNotExistPane() {
            setPrefSize( 800, 200 );
            setMinSize( 10.0, 10.0 );
            setFocusTraversable( true );

            Text text = new Text( "File does not exist" );
            text.getStyleClass().add( "large-background-text" );
            getChildren().add( text );

            addEventHandler( MouseEvent.MOUSE_CLICKED, event -> requestFocus() );

            focusedProperty().addListener( observable -> {
                if ( isFocused() ) {
                    getStyleClass().add( "selected-pane" );
                } else {
                    getStyleClass().remove( "selected-pane" );
                }
            } );
        }

    }

}
