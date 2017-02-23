package com.starsailor.editor;

import com.starsailor.editor.resources.ResourceLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 */
public class MainPane extends BorderPane {

  private Label statusMessage = new Label("");

  private Label infoMessage = new Label("");

  public MainPane() {

    VBox top = new VBox();
    setTop(top);
    SplitPane splitPane = new SplitPane();
    splitPane.setOrientation(Orientation.HORIZONTAL);
    splitPane.getItems().addAll(new TreePane(), new BorderPane());
    splitPane.setDividerPositions(0, 0.3);
    setCenter(splitPane);


    addFooter();

    ToolBar toolbar = new ToolBar();
    Button refreshButton = new Button("", ResourceLoader.getImageView("refresh.png"));
    refreshButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
      }
    });
    refreshButton.setTooltip(new Tooltip("Katalog neu laden"));
    toolbar.getItems().add(refreshButton);


    final MenuBar menuBar = new MenuBar();

    Menu menu = new Menu("Verwaltung");
    MenuItem menu1 = new MenuItem("bubu", ResourceLoader.getImageView("email.png"));
    menu.getItems().addAll(menu1);

    Menu help = new Menu("Hilfe");
    MenuItem info = new MenuItem("Über Starsailor");
    help.getItems().addAll(info);

    menuBar.getMenus().addAll(menu, help);
    top.getChildren().addAll(menuBar, toolbar);
  }

  private void addFooter() {
    BorderPane footer = new BorderPane();
    footer.setStyle("-fx-background-color:#DDD;");

    HBox statusBox = new HBox();
    statusBox.setAlignment(Pos.BASELINE_RIGHT);
    statusBox.getChildren().addAll(statusMessage);
    statusMessage.setPadding(new Insets(0, 5, 2, 0));

    HBox infoBox = new HBox();
    infoBox.setAlignment(Pos.BASELINE_LEFT);
    infoBox.getChildren().addAll(infoMessage);
    infoMessage.setStyle("-fx-font-weight: bold;");
    infoMessage.setPadding(new Insets(0, 0, 2, 5));

    footer.setLeft(infoBox);
    footer.setCenter(statusBox);
    setBottom(footer);
  }
}
