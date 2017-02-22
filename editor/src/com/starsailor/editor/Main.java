package com.starsailor.editor;

import com.starsailor.editor.resources.ResourceLoader;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 */
public class Main extends Application {

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Starsailer Manager");
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setMaxWidth(primaryScreenBounds.getWidth());
    primaryStage.setMaxHeight(primaryScreenBounds.getHeight());
//    primaryStage.setFullScreen(true);
//    primaryStage.setHeight(700);
//    primaryStage.setWidth(1200);
    primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, new MainKeyEventFilter());

    primaryStage.setWidth(primaryScreenBounds.getWidth());
    primaryStage.setHeight(primaryScreenBounds.getHeight());
    Scene scene = new Scene(new BorderPane());
    scene.getStylesheets().add(ResourceLoader.getResource("theme.css"));
    primaryStage.setScene(scene);
    primaryStage.getIcons().add(new Image(ResourceLoader.getResource("favicon.png")));
    primaryStage.show();
  }
}
