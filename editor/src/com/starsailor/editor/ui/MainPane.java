package com.starsailor.editor.ui;

import com.starsailor.editor.UIController;
import com.starsailor.editor.resources.ResourceLoader;
import com.starsailor.editor.util.FormUtil;
import com.starsailor.model.*;
import com.starsailor.model.items.ShipItem;
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

  private FormPane formPane;

  private GameDataTreePane activeTreePane;
  private final BorderPane formPaneHolder;

  private final ShipItemTreePane shipItemsTreePane;
  private final WeaponDataTreePane weaponTreePane;
  private final ShipDataTreePane shipTreePane;
  private final ShieldDataTreePane shieldTreePane;

  private Label statusMessage = new Label("");
  private Label infoMessage = new Label("");
  private final Button newButton;
  private final Button deleteButton;
  private final Button cloneButton;

  public MainPane() {
    VBox top = new VBox();
    setTop(top);
    SplitPane splitPane = new SplitPane();
    splitPane.setOrientation(Orientation.HORIZONTAL);

    Accordion treesPane = new Accordion();
    treesPane.setMaxWidth(800);
    treesPane.setMinWidth(400);

    shipItemsTreePane = new ShipItemTreePane(this);
    shipTreePane = new ShipDataTreePane(this);
    shieldTreePane = new ShieldDataTreePane(this);
    weaponTreePane = new WeaponDataTreePane(this);
    shieldTreePane.setExpanded(true);

    treesPane.getPanes().addAll(shipItemsTreePane, shipTreePane, shieldTreePane, weaponTreePane);

    formPaneHolder = new BorderPane();
    splitPane.getItems().addAll(treesPane, formPaneHolder);
    splitPane.setDividerPositions(0, 0.1);
    setCenter(splitPane);

    addFooter();

    ToolBar toolbar = new ToolBar();
    Button saveButton = new Button("", ResourceLoader.getImageView("save.png"));
    saveButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        UIController.getInstance().save();
      }
    });

    Button refreshButton = new Button("", ResourceLoader.getImageView("refresh.png"));
    refreshButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        shipTreePane.refresh();
      }
    });
    refreshButton.setTooltip(new Tooltip("Daten neu laden"));
    newButton = new Button("", ResourceLoader.getImageView("new.png"));
    newButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        if(activeTreePane != null) {
          TreeItem selection = activeTreePane.getSelection();
          if(selection != null) {
            GameData newChild = UIController.getInstance().newChildFor((GameData) selection.getValue());
            TreeItem newNode = new TreeItem<GameData>(newChild, GameDataTreePane.iconFor(newChild));
            newNode.setExpanded(true);
            selection.getChildren().add(newNode);
            activeTreePane.select(newNode);
          }
        }
      }
    });
    newButton.setTooltip(new Tooltip("Neuen Unterknoten erzeugen"));
    newButton.setDisable(true);

    cloneButton = new Button("", ResourceLoader.getImageView("plus.png"));
    cloneButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        if(activeTreePane != null) {
          TreeItem selection = activeTreePane.getSelection();
          if(selection != null && selection.getParent() != null) {
            GameData newChild = UIController.getInstance().newChildFor((GameData) selection.getParent().getValue());
            TreeItem newNode = new TreeItem<GameData>(newChild, GameDataTreePane.iconFor(newChild));
            newNode.setExpanded(true);
            selection.getParent().getChildren().add(newNode);
          }
        }
      }
    });
    cloneButton.setTooltip(new Tooltip("Create Clone"));
    cloneButton.setDisable(true);

    deleteButton = new Button("", ResourceLoader.getImageView("remove.png"));
    deleteButton.setDisable(true);
    deleteButton.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent event) {
        TreeItem selection = activeTreePane.getSelection();
        if(selection != null) {
          boolean delete = FormUtil.showConfirmation("Delete Node", "Delete the selection?");
          if(delete) {
            TreeItem parent = selection.getParent();
            parent.getChildren().remove(selection);
            GameData value = (GameData) parent.getValue();
            value.getChildren().remove(selection.getValue());
            select(activeTreePane,null);
          }
        }
      }
    });
    deleteButton.setTooltip(new Tooltip("Knoten löschen"));

    toolbar.getItems().addAll(saveButton, new Separator(), refreshButton, new Separator(), newButton, cloneButton, deleteButton);


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

  public void select(GameDataTreePane treePane, TreeItem selection) {
    this.activeTreePane = treePane;
    this.deleteButton.setDisable(selection == null || selection.getParent() == null);
    this.newButton.setDisable(selection == null);
    this.cloneButton.setDisable(selection == null || selection.getParent() == null);

    try {
      if(selection != null) {
        GameData gameData = (GameData) selection.getValue();
        if(gameData instanceof ShipData) {
          formPane = new ShipDataFormPane(this);
        }
        else if(gameData instanceof ShieldData) {
          formPane = new ShieldDataFormPane(this);
        }
        else if(gameData instanceof WeaponData) {
          formPane = new WeaponDataFormPane(this);
        }
        else if(gameData instanceof ShipItem) {
          formPane = new ShipItemFormPane(this);
        }

        formPaneHolder.setCenter(formPane);
        formPane.setData(gameData);
      }
      else if(formPane != null){
        formPane.setData(null);
      }

    } catch (Exception e) {
      e.printStackTrace();
      FormUtil.showError("Error during selection: " + e.getMessage(), e);
    }
  }

  public void refreshTree() {
    activeTreePane.refresh();
  }
}
