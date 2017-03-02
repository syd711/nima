package com.starsailor.editor.ui;

import com.starsailor.model.GameData;
import com.starsailor.model.GameDataWithId;
import com.starsailor.editor.resources.ResourceLoader;
import com.starsailor.editor.util.IdGenerator;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

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

        TreeItem<T> child = new TreeItem<>(item, ResourceLoader.getImageView("item.png"));
        child.valueProperty().bind(new SimpleObjectProperty<>(item));
        child.setExpanded(true);
        parent.getChildren().add(child);

        buildTree(item.getChildren(), child);
      }
    }
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
  }

  abstract protected T getRoot();
}
