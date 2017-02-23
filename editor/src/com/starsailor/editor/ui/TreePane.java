package com.starsailor.editor.ui;

import com.starsailor.data.GameData;
import com.starsailor.editor.UIController;
import com.starsailor.editor.resources.ResourceLoader;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.util.List;

/**
 *
 */
public class TreePane extends BorderPane implements EventHandler<MouseEvent> {
  private final TreeItem<GameData> treeRoot;
  private TreeView treeView;

  private GameData root;

  public TreePane() {
    root = UIController.getInstance().getTreeModel();
    treeRoot = new TreeItem<GameData>(root);
    buildTree(root.getChildren(), treeRoot);

    treeView = new TreeView<GameData>();
    treeView.setOnMouseClicked(this);
    treeView.setShowRoot(false);
    treeView.setRoot(treeRoot);
    treeView.setCellFactory(new Callback<TreeView<GameData>, TreeCell<GameData>>() {
      @Override
      public TreeCell<GameData> call(TreeView<GameData> p) {
        return new GameDataTreeCell();
      }
    });

    setCenter(treeView);
  }

  public TreeItem getSelection() {
    return (TreeItem) treeView.getSelectionModel().getSelectedItem();
  }


  private void buildTree(List<GameData> children, TreeItem<GameData> parent) {
    for(GameData item : children) {
      TreeItem<GameData> categoryTreeItem = new TreeItem<>(item, ResourceLoader.getImageView("item"));
//      categoryTreeItem.expandedProperty().addListener(expandListener);
      categoryTreeItem.valueProperty().bind(new SimpleObjectProperty<>(item));
      categoryTreeItem.setExpanded(true);
      parent.getChildren().add(categoryTreeItem);

      buildTree(item.getChildren(), categoryTreeItem);
    }
  }

  @Override
  public void handle(MouseEvent event) {

  }

  public void refresh() {
    treeView.refresh();
  }
}
