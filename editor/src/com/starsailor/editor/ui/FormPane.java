package com.starsailor.editor.ui;

import com.google.gson.annotations.Expose;
import com.starsailor.data.GameData;
import com.starsailor.editor.util.FormUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FormPane extends BorderPane implements ChangeListener {
  private VBox dynamicForm;
  private MainPane mainPane;
  private List<String> ignoredFields;

  public FormPane(MainPane mainPane, List<String> ignoredFields) {
    this.mainPane = mainPane;
    this.ignoredFields = new ArrayList<>(ignoredFields);
    this.ignoredFields.add("id");
    this.ignoredFields.add("name");

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
      createIdSection(gameData);
    }
  }

  private void createIdSection(GameData gameData) throws Exception {
    GridPane categoryDetailsForm = FormUtil.createFormGrid();
    int index = 0;

    Field idField = gameData.getClass().getDeclaredField("id");
    idField.setAccessible(true);
    FormUtil.addBindingFormField(categoryDetailsForm, gameData, idField, index, false);
    index++;

    Field nameField = gameData.getClass().getDeclaredField("name");
    nameField.setAccessible(true);
    TextField textField = (TextField) FormUtil.addBindingFormField(categoryDetailsForm, gameData, nameField, index, true);
    textField.textProperty().addListener(this);
    index++;

    TitledPane section = FormUtil.createSection(dynamicForm, categoryDetailsForm, "Model Info", false);
    section.setExpanded(true);
  }

  protected void createSection(GameData gameData, String title, boolean extendable, ChangeListener changeListener) throws Exception {
    if(title == null) {
      title = gameData.toString();
    }
    int index = 0;
    GridPane categoryDetailsForm = FormUtil.createFormGrid();

    if(extendable) {
      Field extendParentDataField = gameData.getClass().getSuperclass().getDeclaredField("extendParentData");
      extendParentDataField.setAccessible(true);
      CheckBox checkbox = (CheckBox)FormUtil.addBindingFormField(categoryDetailsForm, gameData, extendParentDataField, index, true);
      checkbox.selectedProperty().addListener(this);
      if(changeListener != null) {
        checkbox.selectedProperty().addListener(changeListener);
      }
      updateGrid(checkbox);
      index++;
    }


    Field[] fields = gameData.getClass().getDeclaredFields();
    for(Field field : fields) {
      field.setAccessible(true);
      Expose annotation = field.getAnnotation(Expose.class);
      if(annotation != null) {
        String name = field.getName();
        Object fieldValue = getGameDataValue(field, gameData);
        if(ignoredFields.contains(name)) {
          continue;
        }

        FormUtil.addBindingFormField(categoryDetailsForm, gameData, field, index, true);
        index++;
      }
    }
    TitledPane section = FormUtil.createSection(dynamicForm, categoryDetailsForm, title, false);
    section.setExpanded(!isExtending(gameData));
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
    if(observable instanceof BooleanProperty) {
      CheckBox checkBox = (CheckBox) ((BooleanProperty) observable).getBean();
      updateGrid(checkBox);
    }
    else if(observable instanceof StringProperty) {
      mainPane.refreshTree();
    }
  }

  protected void updateGrid(CheckBox checkBox) {
    boolean selection = checkBox.isSelected();
    if(selection) {
      FormUtil.setColorForTitledPane(checkBox, Color.LIGHTGOLDENRODYELLOW);
    }
    else {
      FormUtil.setColorForTitledPane(checkBox, Color.WHITE);
    }
  }
}
