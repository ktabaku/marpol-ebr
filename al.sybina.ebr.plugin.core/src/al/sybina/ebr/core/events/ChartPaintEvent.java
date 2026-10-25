package al.sybina.ebr.core.events;


import al.sybina.ebr.core.geo.Sector;
import al.sybina.ebr.core.geo.transform.CoordsTranslator;
import javafx.scene.canvas.GraphicsContext;


public class ChartPaintEvent extends ChartEvent {
	private static final long serialVersionUID = 1L;
	private GraphicsContext g;
	private CoordsTranslator ct;
	private Sector visibleSector;
	private double scale;
	
	public ChartPaintEvent(Object source) {
		super(source);
	}
	public ChartPaintEvent(Object source, GraphicsContext g, CoordsTranslator ct, Sector visibleSector, double scale) {
		super(source);
		this.setGraphics(g);
		this.setTranslator(ct);
		this.setVisibleSector(visibleSector);
		this.setScale(scale);
	}
	public GraphicsContext getGraphics() {
		return g;
	}
	public void setGraphics(GraphicsContext g) {
		this.g = g;
	}
	public Sector getVisibleSector() {
		return visibleSector;
	}
	public void setVisibleSector(Sector visibleSector) {
		this.visibleSector = visibleSector;
	}
	public CoordsTranslator getTranslator() {
		return ct;
	}
	public void setTranslator(CoordsTranslator ct) {
		this.ct = ct;
	}
	public double getScale() {
		return scale;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}

}
