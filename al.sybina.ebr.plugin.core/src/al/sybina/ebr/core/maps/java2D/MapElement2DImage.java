/**
 * project : capcode.toolbox
 * package : al.sybina.ebr.maps.java2d
 * created by :cyrille on 22 janv. 2006
 */
/**
 * CVS markups
 * last update by : $Author: crosay $
 * $Date: 2009-12-01 22:27:33 $
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/maps/java2d/MapElement2DImage.java,v 1.20 2009-12-01 22:27:33 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.19  2009/11/02 00:10:37  crosay
 * Incomplete # add AIS capability
 * http://sourceforge.net/apps/trac/capcode/ticket/42
 * Complete # required by ticket 6: install a change waypoint event mechanism
 * http://sourceforge.net/apps/trac/capcode/ticket/37
 * Complete # when importing a polar, ask also to save it under capcode format.
 * http://sourceforge.net/apps/trac/capcode/ticket/40
 * Incomplete # wordwind plugin for capcode
 * http://sourceforge.net/apps/trac/capcode/ticket/6
 *
 * Revision 1.18  2009/09/03 21:49:20  crosay
 * Incomplete # change the logging of event
 * http://sourceforge.net/apps/trac/capcode/ticket/33
 *
 * Revision 1.17  2009/08/31 22:18:08  crosay
 * Incomplete # preparation for delivery to version 0.9.3
 * http://sourceforge.net/apps/trac/capcode/ticket/17
 *
 * Revision 1.16  2009/08/15 15:12:49  crosay
 * Complete # add reference points to maps
 * http://sourceforge.net/apps/trac/capcode/ticket/22
 *
 * Revision 1.15  2009/03/16 17:13:05  crosay
 * Complete - taskexternalise strings
 *
 * Revision 1.14  2009/02/22 20:24:48  crosay
 * Complete - tasktask  2595952 - FR 2537699 - waypoint files
 * https://sourceforge.net/tracker2/?func=detail&aid=2595952&group_id=100698&atid=1045270
 *
 * Revision 1.13  2009/02/20 22:41:42  crosay
 * Incomplete - tasktask  2595952 - FR 2537699 - waypoint files
 * https://sourceforge.net/tracker2/?func=detail&aid=2595952&group_id=100698&atid=1045270
 *
 * Revision 1.12  2009/01/24 20:52:36  crosay
 * Complete - taskRemoval of sqlite
 *
 * Complete - tasksuppression of debug display for preference saving
 *
 * Complete - tasktask 2531747 animation of the wearther
 * https://sourceforge.net/tracker2/?func=detail&aid=2024403&group_id=100698&atid=1045270
 *
 * Revision 1.11  2008/09/11 15:29:56  crosay
 * task 2042679 (BSB Maps format) continued
 * finalisation
 *
 * Revision 1.10  2008/07/12 19:45:10  crosay
 * added a method that read the picture file path
 *
 * Revision 1.9  2008/05/28 16:28:54  crosay
 * added CVS markups in source code
 *
 * $Revision: 1.20 $
 * $State: Exp $
 */
package al.sybina.ebr.core.maps.java2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * @author cyrille
 *
 */
public class MapElement2DImage extends MapElement2D {
	public Image image;
	private String imageFilePathName;
	private double scale = 1.0;
	private boolean isMaxSized = false;
	private Dimension2D size; 

	/**
	 * creates a new MapElement2D image with the following parameters:
	 *
	 * @param imageFilePathName :
	 *            location of the gif/jpg/png image to load
	 * @throws IOException
	 */
	public MapElement2DImage(String imageFileName) throws IOException {
		super();
		// load the image
		loadImage(imageFileName);
	}
	public MapElement2DImage(String imageFileName, Dimension2D dimension) throws IOException {
		super();
		setMaxSize(dimension);
		loadImage(imageFileName);
	}

	public MapElement2DImage(URL imageURL) throws IOException, URISyntaxException {
		super();
		loadImage(imageURL);
	}

	public MapElement2DImage(Image anImage) {
		super();
		image = anImage;
		if (image != null) {
			width = (int) (isMaxSized ? Math.min(size.getWidth(), image.getWidth()) : image.getWidth());
			height = (int) (isMaxSized ? Math.min(size.getHeight(), image.getHeight()) : image.getHeight());;
		}
	}

	public MapElement2DImage(InputStream inputStream) throws IOException {
		super();
		loadImage(inputStream);
	}


	private void loadImage(URL url) throws IOException, URISyntaxException {
		if (url != null) { 
			URL imageURL = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null).toURL();
			this.imageFilePathName = imageURL.toString();
			image = new Image(imageURL.toURI().toString());
			if (image != null) {
				width = (int) (isMaxSized ? Math.min(size.getWidth(), image.getWidth()) : image.getWidth());
				height = (int) (isMaxSized ? Math.min(size.getHeight(), image.getHeight()) : image.getHeight());;
			}
		}
	}

	/**
	 * load the image given by the file name. if no image is found, a default
	 * picture is displayed.
	 *
	 * @param imageFilePathName
	 * @throws IOException
	 */
	public void loadImage(String imageFileName) throws IOException {
		imageFilePathName = imageFileName;
		if (imageFilePathName != null){
			image = new Image(new File(imageFileName).toURI().toString());
			if (image != null) {
				width = (int) (isMaxSized ? Math.min(size.getWidth(), image.getWidth()) : image.getWidth());
				height = (int) (isMaxSized ? Math.min(size.getHeight(), image.getHeight()) : image.getHeight());;
			}			
		}
	}

	/**
	 * load the image given by the file. 
	 *
	 * @param imageFilePathName
	 * @throws IOException
	 */
	public void loadImage(InputStream inputStream) throws IOException {
		image = new Image(inputStream);
		if (image != null) {
			width = (int) (isMaxSized ? Math.min(size.getWidth(), image.getWidth()) : image.getWidth());
			height = (int) (isMaxSized ? Math.min(size.getHeight(), image.getHeight()) : image.getHeight());
		}
	}


	/* (non-Javadoc)
	 *
	 * @see al.sybina.ebr.maps.java2d.MapElement2D#paintElement(java.awt.Graphics2D)
	 */
	@Override
	public void paintElement(GraphicsContext g) {
		if (g!= null && image!=null){
			g.scale(scale, scale);
			g.drawImage(image, startPos.getX(), startPos.getY(), (double)width, (double)height);
		}
	}

	/**
	 * return the complete path to the picture file.
	 */
	public String getImageFilePathName() {
		return imageFilePathName;
	}

	public void setImageFilePathName(String imagePath) throws IOException {
		loadImage(imagePath);
	}

	/**
	 * @return the name of the image.
	 * This is just the last name in the pathname's name sequence.
	 */
	public String getImageFileName(){
		File f = new File(imageFilePathName);
		return f.getName();
	}

	public void setImageURL(URL imageFileName) throws IOException, URISyntaxException {
		loadImage(imageFileName);
	}

	public void setScale(double d) {
		this.scale = d;
	}

	public double getScale() {
		return scale;
	}

	public Image getImage() {
		return image;
	}
	
	public void setMaxSize(Dimension2D size){
		this.size = size;
		isMaxSized = size.getHeight() > 0 && size.getWidth() > 0;
	}

	public boolean isMaxSized() {
		return isMaxSized;
	}

}
