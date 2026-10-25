package org.eclipse.e4.tools.services;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public interface IResourceProviderService {
	public Image getImage(String key);
	public Font getFont(String key);
	public Color getColor(String key);
}