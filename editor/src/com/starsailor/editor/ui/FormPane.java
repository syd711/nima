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
import java.util.ArrayList;
import java.util.List;

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

    List<GameData> objects = createSection(gameData);
    for(GameData object : objects) {
      createSection(object);
    }
  }

  private List<GameData> createSection(GameData gameData) {
    List<GameData> objectFields = new ArrayList<>();
    GridPane categoryDetailsForm = com.starsailor.editor.util.WidgetFactory.createFormGrid();
    int index = 0;

    Field[] fields = gameData.getClass().getDeclaredFields();
    for(Field field : fields) {
      field.setAccessible(true);
      Expose annotation = field.getAnnotation(Expose.class);
      if(annotation != null) {
        Object fieldValue = getFieldValue(field, gameData);
        if(fieldValue instanceof GameData) {
          objectFields.add((GameData) fieldValue);
          continue;
        }

        FormUtil.addBindingFormTextfield(categoryDetailsForm, gameData, field, index, true, this);
        index++;
      }
    }
    com.starsailor.editor.util.WidgetFactory.createSection(dynamicForm, categoryDetailsForm, gameData.toString(), false);
    return objectFields;
  }

  private Object getFieldValue(Field field, GameData gameData) {
    try {
      Object value = field.get(gameData);
      if(value instanceof GameData) {
        return value;
      }
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void changed(ObservableValue observable, Object oldValue, Object newValue) {

  }
}
