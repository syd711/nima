package com.starsailor.editor.util;

import com.starsailor.data.GameData;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 *
 */
public class FormUtil {
  public static TextField addBindingFormTextfield(GridPane grid, GameData data, Field field, int row, boolean editable, ChangeListener<String> listener) {
    TextField textBox = null;
    try {
      String value = (String) field.get(data);
      String label = splitCamelCase(StringUtils.capitalize(field.getName())) + ":";
      BeanPathAdapter<GameData> gameDataBeanPathAdapter = new BeanPathAdapter<>(data);

      Label condLabel = new Label(label);
      GridPane.setHalignment(condLabel, HPos.RIGHT);
      GridPane.setConstraints(condLabel, 0, row);
      textBox = new TextField(value);
      textBox.setEditable(editable);
      GridPane.setMargin(textBox, new Insets(5, 5, 5, 10));
      GridPane.setConstraints(textBox, 1, row);
      grid.getChildren().addAll(condLabel, textBox);

      gameDataBeanPathAdapter.bindBidirectional(field.getName(), textBox.textProperty());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return textBox;
  }

  static String splitCamelCase(String s) {
    return s.replaceAll(
        String.format("%s|%s|%s",
            "(?<=[A-Z])(?=[A-Z][a-z])",
            "(?<=[^A-Z])(?=[A-Z])",
            "(?<=[A-Za-z])(?=[^A-Za-z])"
        ),
        " "
    );
  }
}
