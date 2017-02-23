package com.starsailor.editor.ui;

import com.google.gson.annotations.Expose;
import com.starsailor.data.GameData;
import javafx.beans.property.SimpleStringProperty;
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

    GridPane categoryDetailsForm = WidgetFactory.createFormGrid();
    int index = 0;

    Field[] fields = gameData.getClass().getFields();
    for(Field field : fields) {
      Expose annotation = field.getAnnotation(Expose.class);
      if(annotation != null) {
        try {
          String value = (String) field.get(gameData);
          SimpleStringProperty property = new SimpleStringProperty(value);
          WidgetFactory.addBindingFormTextfield(categoryDetailsForm, field.getName(), property, index , true, this);
        } catch (Exception e) {
          e.printStackTrace();
        }
        index++;
      }
    }

//    WidgetFactory.addBindingFormCheckbox(categoryDetailsForm, "Kategorie aktiviert:", getModel().getStatus(), index++, true, this);
//    WidgetFactory.addBindingFormTextfield(categoryDetailsForm, "Name:", getModel().getTitle(), index++, true, this);
//    WidgetFactory.addBindingFormTextarea(categoryDetailsForm, "Titeltext:", getModel().getDetails(), 200, index++, true, this);
//    WidgetFactory.addBindingFormTextarea(categoryDetailsForm, "Kurzbeschreibung (Bildunterschrift):", getModel().getShortDescription(), index++, true, this);

    WidgetFactory.createSection(dynamicForm, categoryDetailsForm, gameData.toString(), false);
  }

  @Override
  public void changed(ObservableValue observable, Object oldValue, Object newValue) {

  }
}
