package com.starsailor.editor.ui;

import com.google.gson.annotations.Expose;
import com.starsailor.data.GameData;
import com.starsailor.editor.util.FormUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;

/**
 *
 */
public class FormPane extends VBox implements ChangeListener {

  private VBox dynamicForm;
  private MainPane mainPane;

  public FormPane(MainPane mainPane) {
    this.mainPane = mainPane;
    dynamicForm = new VBox();
    dynamicForm.setAlignment(Pos.TOP_CENTER);
    dynamicForm.setFillWidth(true);

    getChildren().add(dynamicForm);
  }

  public void setData(GameData gameData) {
    dynamicForm.getChildren().clear();

    GridPane categoryDetailsForm = com.starsailor.editor.util.WidgetFactory.createFormGrid();
    int index = 0;

    Field[] fields = gameData.getClass().getDeclaredFields();
    for(Field field : fields) {
      field.setAccessible(true);
      Expose annotation = field.getAnnotation(Expose.class);
      if(annotation != null) {
        FormUtil.addBindingFormTextfield(categoryDetailsForm, gameData, field, index, true, this);
        index++;
      }
    }

//    WidgetFactory.addBindingFormCheckbox(categoryDetailsForm, "Kategorie aktiviert:", getModel().getStatus(), index++, true, this);
//    WidgetFactory.addBindingFormTextfield(categoryDetailsForm, "Name:", getModel().getTitle(), index++, true, this);
//    WidgetFactory.addBindingFormTextarea(categoryDetailsForm, "Titeltext:", getModel().getDetails(), 200, index++, true, this);
//    WidgetFactory.addBindingFormTextarea(categoryDetailsForm, "Kurzbeschreibung (Bildunterschrift):", getModel().getShortDescription(), index++, true, this);

    com.starsailor.editor.util.WidgetFactory.createSection(dynamicForm, categoryDetailsForm, gameData.toString(), false);
  }

  @Override
  public void changed(ObservableValue observable, Object oldValue, Object newValue) {

  }
}
