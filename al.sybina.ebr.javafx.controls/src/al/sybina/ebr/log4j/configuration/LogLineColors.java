package al.sybina.ebr.log4j.configuration;

import javafx.scene.paint.Paint;

/**
 * A tuple of 2 colors representing the colors for a log file line in the UI.
 */
public class LogLineColors {

    private final Paint background;
    private final Paint fill;

    public LogLineColors( Paint background, Paint fill ) {
        this.background = background;
        this.fill = fill;
    }

    public Paint getBackground() {
        return background;
    }

    public Paint getFill() {
        return fill;
    }
}
