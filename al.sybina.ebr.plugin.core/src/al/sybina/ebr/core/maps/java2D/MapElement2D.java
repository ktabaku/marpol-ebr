/**
 * project : capcode file name : MapElement2D.java created : 5 janv. 2006 by :
 * cyrille
 */
/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2009-11-02 00:10:37 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/maps/java2d/MapElement2D.java,v 1.17 2009-11-02 00:10:37 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.16  2009/03/04 10:15:55  crosay
 * Complete - taskdisplay the equipments connected to PC
 *
 * Complete - taskexternalise strings
 *
 * Incomplete - tasknew plugin: bearing-speed-and log
 *
 * Revision 1.15  2008/10/10 21:01:43  crosay
 * added alpha composite field
 *
 * Revision 1.14  2008/09/12 19:29:44  crosay
 * added BasicStroke to allow modification of the drawing of lines
 *
 * Revision 1.13  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.17 $ 
 * $State: Exp $ 
 */
package al.sybina.ebr.core.maps.java2D;





import al.sybina.ebr.core.data.Angle;
import al.sybina.ebr.core.geo.Position;
import al.sybina.ebr.core.geo.transform.CoordsTranslator;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;

/**
 * Class used by a CcmapViewer class to display Elements on a capcode map.<br>
 * 
 * @author cyrille
 */
public abstract class MapElement2D {
	/** horizontal layout of the map element */
	public enum E_HorizontalLayout {
		LEFT, CENTER, RIGHT;

		public static E_HorizontalLayout getLayout(String name) {
			try {
				return valueOf(name);
			} catch (Exception e) {
				return LEFT;
			}
		}
	}

	/** vertical layout of the map element */
	public enum E_VerticalLayout {
		TOP, CENTER, BOTTOM;
		public static E_VerticalLayout getLayout(String name) {
			try {
				return valueOf(name);
			} catch (Exception e) {
				return CENTER;
			}
		}
	}

	/** x position on the screen */
	protected int x = 0;
	/** y position on the screen */
	protected int y = 0;
	/** name of the element */
	protected String name = ""; //$NON-NLS-1$
	/** true if visible */
	protected boolean visible = true;

	/** Longitude and Latitude of the element */
	protected Position position;

	/** angle in degrees of rotation */
	protected Angle angle;
	/** width */
	protected int width;
	/** height */
	protected int height;
	/** layout position of the element (left, center, etc) */
	public Point2D startPos;
	/** position after the Transform */
	public Point2D drawnPos;

	protected CoordsTranslator coordsToWindow;

	/** color */
	protected Color color = Color.WHITE;

	/** the font for printing text */
	protected Font font = null;

	/** horizontal layout of the element */
	protected E_HorizontalLayout horizontalLayout = E_HorizontalLayout.CENTER;
	/** vertical layout of the element */
	protected E_VerticalLayout verticalLayout = E_VerticalLayout.CENTER;

	protected boolean drawName = true;
	protected boolean fill = false;
	protected Rectangle bounds;
	protected double stroke = 1.0d;
	protected Color backGroundColor = null;
	private float opacity = 1;
	protected Point2D center;

	/**
	 * create an empty MapElement2D.
	 */
	public MapElement2D() {
		super();
		angle = new Angle(0);
		drawnPos = new Point2D(0, 0);
		position = new Position("E0.0", "N0.0"); //$NON-NLS-1$ //$NON-NLS-2$
		bounds = new Rectangle(0, 0);
	}

	/**
	 * create a MapElement2D with a known CoordsTranslator. a CoordsTranslator
	 * is the element that convert a geographic position into x,y (pixels)
	 * 
	 * @see CoordsTranslator.
	 * @param ct
	 *            : CoordsTranslator.
	 */
	public MapElement2D(CoordsTranslator ct) {
		this();
		coordsToWindow = ct;
	}

	/**
	 * @return a point that will be use as a starting point for drawing element
	 *         depending on the layout.
	 */
	protected Point2D computeStartPos(int x, int y) {
		Point2D res = new Point2D(x, y);
		switch (horizontalLayout) {
		case RIGHT:
			res = res.subtract(width, 0);
			break;
		case CENTER:
			res = res.subtract(width / 2, 0);
			break;
		default:
		}
		switch (verticalLayout) {
		case BOTTOM:
			res = res.subtract(0, height);
			break;
		case CENTER:
			res = res.subtract(0, height / 2);
			break;
		default:
		}
		bounds.setX(res.getX());
		bounds.setY(res.getY());
		bounds.setWidth(this.width);
		bounds.setHeight(this.height);
		return res;
	}

	/**
	 * draw the element in a given graphic context and affine transform (they
	 * may be different)<br>
	 * you just have to call draw (Graphics2D) <br>
	 * 
	 * @param g
	 *            the graphic where the element shall be drawn
	 */

	public void draw(GraphicsContext g) {
		if (visible && g != null) {
			// compute x and y from the longitude/latitude based on the
			// transform
			// coordToWindow
			g.save();
			setLocationToMap(coordsToWindow);
			int x = getX();
			int y = getY();
			center = new Point2D(x - width / 2, y - height / 2);
			startPos = computeStartPos(x, y);
			g.setFill(color);
			g.setStroke(color);
			g.setLineWidth(stroke);
			if (drawName) {
				drawName(g);
			}
			Affine oldAt = g.getTransform();
			g.setGlobalAlpha(opacity);
			if (angle != null) {
				g.rotate(angle.toRadian());//, x, y);
			}
			paintElement(g);
			//g.setComposite(c);
			g.getTransform().deltaTransform(startPos);
			g.setTransform(oldAt);
			g.restore();
		}
	}

	public void drawName(GraphicsContext g) {
		g.setFont(font);
		float width = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().computeStringWidth("x", g.getFont());
		float height = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().getFontMetrics(g.getFont()).getLineHeight();
		int stringLength = (int) (width * getName().length());
		if (backGroundColor != null) {
			g.setFill(backGroundColor);
			int h = (int) height;
			g.fillRoundRect(x - stringLength / 2, center.getY() - h,
					(int) stringLength, h, 5, 5);
			g.setFill(color);
		}
		g.fillText(getName(), x - stringLength / 2, center.getY());
	}

	/**
	 * abstract paint element, called by paint and must be overriden by
	 * descendants
	 * 
	 * @param g
	 */
	protected abstract void paintElement(GraphicsContext g);

	/**
	 * 
	 * @param ct
	 *            CoordTranslator transforms the position (longitute, latitude)
	 *            to x,y
	 */
	public void setLocationToMap(CoordsTranslator ct) {
		if (ct != null) {
			Point2D p = ct.getLocalPointFromPosition(position);
			setLocation(p);
		}
	}

	/**
	 * the the location (x,y) of the element. x, y are in pixel.
	 * 
	 * @param p
	 *            : point (x,y) of the new location.
	 */
	private void setLocation(Point2D p) {
		x = (int) p.getX();
		y = (int) p.getY();
	}

	/**
	 * @param pos
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * @return Returns the angle.
	 */
	public Angle getAngle() {
		return angle;
	}

	/**
	 * @param angle
	 *            The angle to set.
	 */
	public void setAngle(Angle angle) {
		this.angle = angle;
	}

	/**
	 * @return Returns the coordsToWindow.
	 */
	public CoordsTranslator getCoordsToWindow() {
		return coordsToWindow;
	}

	/**
	 * set the CoordsTranslator of the element. this is used to associate a
	 * point on the map to the geographic position and vice-versa.
	 * 
	 * @param coordsToWindow
	 *            The coordsToWindow to set.
	 */
	public void setCoordsToWindow(CoordsTranslator coordsToWindow) {
		this.coordsToWindow = coordsToWindow;
	}

	/**
	 * @return Returns the color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return Returns the drawName. (true is name shall be displayed)
	 */
	public boolean isDrawName() {
		return drawName;
	}

	/**
	 * @param drawName
	 *            true if name is drawn.
	 */
	public void setDrawName(boolean drawName) {
		this.drawName = drawName;
	}

	/**
	 * 
	 * @return true is the element is a link to another map
	 */
	public boolean isLink() {
		return false;
	}

	/**
	 * display the element as name + position
	 */
	@Override
	public String toString() {
		return (getName() + "," + position); //$NON-NLS-1$
	}

	/**
	 * @return the pos
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the horizontalLayout
	 */
	public E_HorizontalLayout getHorizontalLayout() {
		return horizontalLayout;
	}

	/**
	 * @param horizontalLayout
	 *            the horizontalLayout to set
	 */
	public void setHorizontalLayout(E_HorizontalLayout horizontalLayout) {
		this.horizontalLayout = horizontalLayout;
	}

	/**
	 * @return the verticalLayout
	 */
	public E_VerticalLayout getVerticalLayout() {
		return verticalLayout;
	}

	/**
	 * @param verticalLayout
	 *            the verticalLayout to set
	 */
	public void setVerticalLayout(E_VerticalLayout verticalLayout) {
		this.verticalLayout = verticalLayout;
	}

	/**
	 * @return the fill
	 */
	public boolean isFill() {
		return fill;
	}

	/**
	 * @param fill
	 *            the fill to set
	 */
	public void setFill(boolean fill) {
		this.fill = fill;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * get the Rectangle containing the element.
	 * 
	 * @return a Rectangle
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	public void setStroke(double s) {
		stroke = s;
	}

	public void setBackGroundColor(Color c) {
		backGroundColor = c;
	}

	/**
	 * @return the opacity
	 */
	public float getOpacity() {
		return this.opacity;
	}

	/**
	 * @param opacity
	 *            the opacity to set
	 */
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	/**
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * @param font
	 *            the font to set
	 */
	public void setFont(Font font) {
		this.font = font;
	}
}
