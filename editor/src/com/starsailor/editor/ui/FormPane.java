package com.starsailor.editor.ui;

import com.google.gson.annotations.Expose;
import com.starsailor.data.GameData;
import com.starsailor.editor.util.FormUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FormPane extends BorderPane implements ChangeListener {

  private VBox dynamicForm;
  private MainPane mainPane;

  public FormPane(MainPane mainPane) {
    this.mainPane = mainPane;
    dynamicForm = new VBox();
    dynamicForm.setAlignment(Pos.TOP_CENTER);
    dynamicForm.setFillWidth(true);

    ScrollPane centerScroller = new ScrollPane();
    centerScroller.setFitToWidth(true);
    centerScroller.setContent(dynamicForm);

    setCenter(centerScroller);
  }

  public void setData(GameData gameData) throws Exception {
    dynamicForm.getChildren().clear();

    if(gameData != null) {
      List<GameData> objects = createSection(gameData);

      for(GameData object : objects) {
        createSection(object);
      }
    }
  }

  private List<GameData> createSection(GameData gameData) throws Exception {
    List<GameData> objectFields = new ArrayList<>();
    GridPane categoryDetailsForm = FormUtil.createFormGrid();
    int index = 0;


    Field extendParentDataField = gameData.getClass().getSuperclass().getDeclaredField("extendParentData");
    extendParentDataField.setAccessible(true);
    FormUtil.addBindingFormTextfield(categoryDetailsForm, gameData, extendParentDataField, index, true, this);
    index++;

    Field[] fields = gameData.getClass().getDeclaredFields();
    for(Field field : fields) {
      field.setAccessible(true);
      Expose annotation = field.getAnnotation(Expose.class);
      if(annotation != null) {
        Object fieldValue = getGameDataValue(field, gameData);
        if(fieldValue instanceof GameData) {
          objectFields.add((GameData) fieldValue);
          continue;
        }

        FormUtil.addBindingFormTextfield(categoryDetailsForm, gameData, field, index, true, this);
        index++;
      }
    }
    TitledPane section = FormUtil.createSection(dynamicForm, categoryDetailsForm, gameData.toString(), false);
    section.setExpanded(!isExtending(gameData));
    return objectFields;
  }

  private Object getGameDataValue(Field field, GameData gameData) {
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

  private Boolean isExtending(GameData gameData) {
    try {
      Field extendParentDataField = gameData.getClass().getSuperclass().getDeclaredField("extendParentData");
      extendParentDataField.setAccessible(true);
      return (Boolean) extendParentDataField.get(gameData);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void changed(ObservableValue observable, Object oldValue, Object newValue) {

  }
}
