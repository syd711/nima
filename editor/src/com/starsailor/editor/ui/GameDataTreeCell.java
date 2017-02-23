package com.starsailor.editor.ui;

import com.starsailor.data.GameData;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 */
public class GameDataTreeCell extends TreeCell<GameData> {

  private TextField textField;

  public GameDataTreeCell() {
  }

  @Override
  public void startEdit() {
    super.startEdit();

    if (textField == null) {
      createTextField();
    }
    setText(null);
    setGraphic(textField);
    textField.selectAll();
  }

  @Override
  public void cancelEdit() {
    super.cancelEdit();
    setText(getItem().toString());
    setGraphic(getTreeItem().getGraphic());
  }

  @Override
  public void updateItem(GameData item, boolean empty) {
    super.updateItem(item, empty);

    if (empty) {
      setText(null);
      setGraphic(null);
    } else {
      if (isEditing()) {
        if (textField != null) {
          textField.setText(getString());
        }
        setText(null);
        setGraphic(textField);
      } else {
        setText(getString());
//        if(item != null && item.getStatusValue() == 1) {
//          setStyle("-fx-text-fill:#000;");
//          if(item.getType() == AbstractModel.TYPE_VARIANT || item.getType() == AbstractModel.TYPE_PRODUCT) {
//            Product product = (Product) item;
//            if(!product.isOnStock()) {
//              setStyle("-fx-text-fill:#cc4444;-fx-font-weight:bold;");
//            }
//            else if(product.isForFree() && (product.isVariant() || product.getVariants().isEmpty())) {
//              setStyle("-fx-text-fill:#cc4444;-fx-font-weight:bold;");
//            }
//            else {
//              setStyle("-fx-text-fill:#000;");
//            }
//
//            if(product.isOnStock() && (product.isVariant() || product.getVariants().isEmpty())) {
//              setText(getString() + " (" + product.getStock().get() + " auf Lager)");
//            }
//          }
//        }
//        else {
//          setStyle("-fx-text-fill:#aaa;-fx-font-weight:normal;");
//        }

        if(getTreeItem() != null) {
          setGraphic(getTreeItem().getGraphic());
        }
      }
    }
  }

  private void createTextField() {
    textField = new TextField(getString());
    textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

      @Override
      public void handle(KeyEvent t) {
        if(t.getCode() == KeyCode.ENTER) {
//          commitEdit(textField.getText());
        }
        else if(t.getCode() == KeyCode.ESCAPE) {
          cancelEdit();
        }
      }
    });
  }

  private String getString() {
    return getItem() == null ? "" : getItem().toString();
  }
}