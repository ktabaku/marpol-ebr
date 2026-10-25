package org.eclipse.e4.tools.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.BundleContext;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public abstract class BasicResourceProvider implements IResourceProviderService {
	protected Map<String, String> properties;
	protected BundleContext context;
	
	public void activate(BundleContext context, Map<String, String> properties) {
		this.properties = properties;
		this.context = context;
	}
	
	public Image getImage(String key) {
		URL url = FileLocator.find(context.getBundle(),new Path(properties.get(key)),null);
		
		if( url != null ) {
			InputStream stream = null;
			try {
				stream = url.openStream();
				return new Image(stream);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if( stream != null ) {
					try {
						stream.close();
					} catch (IOException e) {
					}
				}
			}
		}
		
		// TODO Auto-generated method stub
		return null;
	}
	
	public Font getFont(String key) {
		return null;
	}

	public Color getColor(String key) {
		String color = properties.get(key);
		if (color.startsWith("rgb")) {
			String rgb = color.substring(color.indexOf('(')+1, color.indexOf(')'));
			String[] cols = rgb.split(",");
			int r = Integer.parseInt(cols[0].trim());
			int g = Integer.parseInt(cols[1].trim());
			int b = Integer.parseInt(cols[2].trim());
			return Color.rgb(r,g,b);
		}
		return null;
	}
}