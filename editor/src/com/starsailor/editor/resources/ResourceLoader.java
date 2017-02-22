package com.starsailor.editor.resources;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

/**
 * Used for load resources and stuff.
 */
public class ResourceLoader {
  
  public static String getResource(String s) {
    try {
      return new File("resources/" + s).toURI().toString();
    } catch (Exception e) {
      //TODO Logger.getLogger(ResourceLoader.class.getName()).error("Resource not found: " + s + ": " + e.getMessage());
    }
    return null;
  }
  
  public static ImageView getImageView(String s) {
    return new ImageView(new Image(ResourceLoader.getResource(s)));
  }

  public static ImageView getWebImageView(String s) {

    Image image = new Image(s);
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(100);
    imageView.setFitHeight(80);
    return imageView;
  }

  public static String getTemplate(String s) {
    return new File("mail-templates/", s).toURI().toString();
  }
}
