package com.starsailor.editor;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 */
public class MainKeyEventFilter implements javafx.event.EventHandler<KeyEvent> {


  public void handle(KeyEvent event) {
    if(event.getCode() == KeyCode.W && event.isControlDown()) {

    }
  }
}
