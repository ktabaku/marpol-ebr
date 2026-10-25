package al.sybina.ebr.core.events;

import al.sybina.ebr.core.geo.transform.CoordsTranslator;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public interface IChartPaintListener extends IChartEventListener{
	public void notify(ChartPaintEvent event);

	public void componentReady(Node comp);
	
	public boolean needsMouseEvents(Point2D mousePointer, CoordsTranslator ct, MouseEvent e);
}
