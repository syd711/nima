package com.starsailor.editor.ui;

import com.starsailor.data.GameData;
import com.starsailor.data.ShipData;
import com.starsailor.editor.UIController;
import com.starsailor.editor.resources.ResourceLoader;
import com.starsailor.editor.util.IdGenerator;
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
public class ShipDataTreePane extends BorderPane implements EventHandler<MouseEvent> {
  private final TreeItem<ShipData> treeRoot;
  private TreeView treeView;

  private ShipData root;

  private MainPane mainPane;
  public ShipDataTreePane(MainPane mainPane) {
    this.mainPane = mainPane;
    root = UIController.getInstance().getTreeModel();
    treeRoot = new TreeItem<ShipData>(root);
    treeRoot.setExpanded(true);
    buildTree(root.getChildren(), treeRoot);

    treeView = new TreeView<ShipData>();
    treeView.setOnMouseClicked(this);
    treeView.setShowRoot(true);
    treeView.setRoot(treeRoot);
    treeView.setCellFactory(new Callback<TreeView<GameData>, TreeCell<GameData>>() {
      @Override
      public TreeCell<GameData> call(TreeView<GameData> p) {
        return new GameDataTreeCell();
      }
    });

    setCenter(treeView);
  }

  public GameData getRoot() {
    return root;
  }

  public TreeItem getSelection() {
    return (TreeItem) treeView.getSelectionModel().getSelectedItem();
  }


  private void buildTree(List<ShipData> children, TreeItem<ShipData> parent) {
    for(ShipData item : children) {
      item.setParent(parent.getValue());
      IdGenerator.getInstance().update(item);

      TreeItem<ShipData> categoryTreeItem = new TreeItem<>(item, ResourceLoader.getImageView("item.png"));
      categoryTreeItem.valueProperty().bind(new SimpleObjectProperty<>(item));
      categoryTreeItem.setExpanded(true);
      parent.getChildren().add(categoryTreeItem);

      buildTree(item.getChildren(), categoryTreeItem);
    }
  }

  @Override
  public void handle(MouseEvent event) {
    TreeItem selection = getSelection();
    mainPane.select(selection);
  }

  public void refresh() {
    treeView.refresh();
  }

  public void select(TreeItem newNode) {
    treeView.getSelectionModel().select(newNode);
  }
}
