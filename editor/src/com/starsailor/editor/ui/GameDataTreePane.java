package com.starsailor.editor.ui;

import com.starsailor.editor.UIController;
import com.starsailor.editor.resources.ResourceLoader;
import com.starsailor.editor.util.IdGenerator;
import com.starsailor.model.GameData;
import com.starsailor.model.GameDataWithId;
import com.starsailor.model.items.MapItem;
import com.starsailor.model.items.ShipItem;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.io.File;
import java.util.List;

/**
 *
 */
abstract public class GameDataTreePane<T extends GameDataWithId> extends TitledPane implements EventHandler<MouseEvent> {
  private final TreeItem<T> treeRoot;
  private TreeView treeView;

  private T root;

  private MainPane mainPane;
  public GameDataTreePane(MainPane mainPane, String title) {
    setText(title);
    this.mainPane = mainPane;
    root = getRoot();
    treeRoot = new TreeItem<T>(root);
    treeRoot.setExpanded(true);
    buildTree(root.getChildren(), treeRoot);

    treeView = new TreeView<T>();
    treeView.setOnMouseClicked(this);
    treeView.setShowRoot(isRootVisible());
    treeView.setRoot(treeRoot);
    treeView.setCellFactory(new Callback<TreeView<GameData>, TreeCell<GameData>>() {
      @Override
      public TreeCell<GameData> call(TreeView<GameData> p) {
        return new GameDataTreeCell();
      }
    });

    setContent(treeView);
  }

  public TreeItem getTreeRoot() {
    return treeRoot;
  }

  protected boolean isRootVisible() {
    return true;
  }

  public TreeItem getSelection() {
    return (TreeItem) treeView.getSelectionModel().getSelectedItem();
  }


  private void buildTree(List<T> children, TreeItem<T> parent) {
    if(children != null) {
      for(T item : children) {
        item.setParent(parent.getValue());
        IdGenerator.getInstance().update(item);

        TreeItem<T> child = new TreeItem<>(item, iconFor(item));
        child.valueProperty().bind(new SimpleObjectProperty<>(item));
        child.setExpanded(true);
        parent.getChildren().add(child);

        buildTree(item.getChildren(), child);
      }
    }
  }

  public static ImageView iconFor(GameData gameData) {
    if(gameData instanceof ShipItem) {
      ShipItem shipItem = (ShipItem) gameData;
      if(shipItem.getDefaultSteering() == null) {
        return ResourceLoader.getImageView("green.png");
      }
    }

    if(gameData instanceof MapItem) {
      MapItem item = (MapItem) gameData;
      File tmxFileFor = UIController.getInstance().getTmxFileFor(item);
      if(tmxFileFor != null) {
        return ResourceLoader.getImageView("check-green.png");
      }
      return ResourceLoader.getImageView("green_hole.png");
    }
    return ResourceLoader.getImageView("item.png");
  }

  @Override
  public void handle(MouseEvent event) {
    TreeItem selection = getSelection();
    mainPane.select(this, selection);
  }

  public void refresh() {
    treeView.refresh();
  }

  public void select(TreeItem newNode) {
    treeView.getSelectionModel().select(newNode);
    mainPane.select(this, newNode);

  }

  abstract protected T getRoot();
}
