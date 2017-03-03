package com.starsailor.editor;

import com.starsailor.managers.GameDataManager;
import com.starsailor.editor.resources.ResourceLoader;
import com.starsailor.editor.ui.MainPane;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 */
public class Editor extends Application {
  public static Editor INSTANCE;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    INSTANCE = this;

    GameDataManager.ASSETS_FOLDER = "../../core/assets/data/";

    primaryStage.setTitle("Starsailer Manager");
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setMaxWidth(primaryScreenBounds.getWidth());
    primaryStage.setMaxHeight(primaryScreenBounds.getHeight());
    primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, new com.starsailor.editor.ui.MainKeyEventFilter());

    primaryStage.setWidth(primaryScreenBounds.getWidth());
    primaryStage.setHeight(primaryScreenBounds.getHeight());
    Scene scene = new Scene(new MainPane());
    scene.setFill(Color.OLDLACE);
    scene.getStylesheets().add(ResourceLoader.getResource("theme.css"));
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
