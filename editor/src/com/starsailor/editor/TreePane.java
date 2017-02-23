package com.starsailor.editor;

import com.starsailor.data.GameData;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.util.Collections;
import java.util.List;

/**
 * Created by Matthias on 23.02.2017.
 */
public class TreePane extends BorderPane implements EventHandler<MouseEvent> {
  private final TreeItem<GameData> treeRoot;
  private TreeView treeView;

  private GameData root;

  public TreePane() {
//    root = new GameData();

    treeRoot = new TreeItem<GameData>(root);
    treeView = new TreeView<GameData>();
    treeView.setOnMouseClicked(this);
    treeView.setShowRoot(false);
    treeView.setRoot(treeRoot);
//    treeView.setCellFactory(new Callback<TreeView<TreeModel>, TreeCell<TreeModel>>() {
//      @Override
//      public TreeCell<TreeModel> call(TreeView<TreeModel> p) {
//        return null;//new CatalogTreeCell();
//      }
//    });

    List<GameData> items = getTopLevelModels();
    for(GameData topLevel : items) {
      root.getChildren().add(topLevel);
    }

    setCenter(treeView);
  }

  @Override
  public void handle(MouseEvent event) {

  }

  public List<GameData> getTopLevelModels() {
    return Collections.EMPTY_LIST;
  }
}
