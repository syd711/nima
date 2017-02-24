package com.starsailor.editor.ui;

import com.starsailor.data.GameData;
import com.starsailor.data.ShipData;
import com.starsailor.editor.UIController;
import com.starsailor.editor.resources.ResourceLoader;
import com.starsailor.editor.util.FormUtil;
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

  private final ShipDataTreePane treePane;
  private final ShipDataFormPane formPane;

  private Label statusMessage = new Label("");
  private Label infoMessage = new Label("");

  public MainPane() {
    VBox top = new VBox();
    setTop(top);
    SplitPane splitPane = new SplitPane();
    splitPane.setOrientation(Orientation.HORIZONTAL);
    treePane = new ShipDataTreePane(this);
    treePane.setMaxWidth(800);
    treePane.setMinWidth(400);
    formPane = new ShipDataFormPane(this);
    splitPane.getItems().addAll(treePane, formPane);
    splitPane.setDividerPositions(0, 0.1);
    setCenter(splitPane);

    addFooter();

    ToolBar toolbar = new ToolBar();
    Button saveButton = new Button("", ResourceLoader.getImageView("save.png"));
    saveButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        UIController.getInstance().save(treePane.getRoot());
      }
    });

    Button refreshButton = new Button("", ResourceLoader.getImageView("refresh.png"));
    refreshButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        treePane.refresh();
      }
    });
    refreshButton.setTooltip(new Tooltip("Daten neu laden"));
    Button newButton = new Button("", ResourceLoader.getImageView("new.png"));
    newButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        TreeItem selection = treePane.getSelection();
        if(selection != null) {
          ShipData newChild = UIController.getInstance().newChildFor((ShipData) selection.getValue());
          TreeItem newNode = new TreeItem<GameData>(newChild);
          newNode.setExpanded(true);
          selection.getChildren().add(newNode);
          treePane.refresh();
          treePane.select(newNode);
        }
      }
    });
    newButton.setTooltip(new Tooltip("Neuen Unterknoten erzeugen"));

    Button deleteButton = new Button("", ResourceLoader.getImageView("remove.png"));
    deleteButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        TreeItem selection = treePane.getSelection();
        if(selection != null) {
          boolean delete = FormUtil.showConfirmation("Delete Node", "Delete the selection?");
          if(delete) {
            TreeItem parent = selection.getParent();
            parent.getChildren().remove(selection);
            GameData value = (GameData) parent.getValue();
            value.getChildren().remove(selection.getValue());
            select(null);
          }
        }
      }
    });
    deleteButton.setTooltip(new Tooltip("Knoten löschen"));

    toolbar.getItems().addAll(saveButton, new Separator(), refreshButton, new Separator(), newButton, deleteButton);


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

  public void select(TreeItem selection) {
    try {
      if(selection != null) {
        formPane.setData((ShipData) selection.getValue());
      }
      else {
        formPane.setData(null);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void refreshTree() {
    treePane.refresh();
  }
}
