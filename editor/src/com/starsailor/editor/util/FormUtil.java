package com.starsailor.editor.util;

import com.starsailor.data.GameData;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

/**
 *
 */
public class FormUtil {
  public static boolean showConfirmation(String header, String message) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("");
    alert.setHeaderText(header);
    alert.setContentText(new String((message).getBytes(), Charset.forName("utf-8")));
    alert.showAndWait();
    ButtonType result = alert.getResult();
    return result.getButtonData().isDefaultButton();
  }

  public static GridPane createFormGrid() {
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    ColumnConstraints colInfo2 = new ColumnConstraints();
    colInfo2.setPercentWidth(20);
    ColumnConstraints colInfo3 = new ColumnConstraints();
    colInfo3.setPercentWidth(80);
    grid.getColumnConstraints().add(colInfo2); //25 percent
    grid.getColumnConstraints().add(colInfo3); //50 percent
    return grid;
  }


  public static TitledPane createSection(Pane root, Node child, String title, boolean collapsed) {
    TitledPane group = new TitledPane(title, child);
    group.setPadding(new Insets(10, 10, 5, 10));
    root.getChildren().add(group);
    group.setExpanded(!collapsed);
    return group;
  }

  public static TextField addBindingFormTextfield(GridPane grid, GameData data, Field field, int row, boolean editable, ChangeListener<String> listener) {
    TextField textBox = null;
    try {
      Object value = field.get(data);
      String label = splitCamelCase(StringUtils.capitalize(field.getName())) + ":";
      BeanPathAdapter<GameData> gameDataBeanPathAdapter = new BeanPathAdapter<>(data);

      Label condLabel = new Label(label);
      GridPane.setHalignment(condLabel, HPos.RIGHT);
      GridPane.setConstraints(condLabel, 0, row);

      Node editorNode = null;
      if(value instanceof Boolean) {
        CheckBox checkbox = new CheckBox();
        checkbox.setSelected((Boolean) value);
        gameDataBeanPathAdapter.bindBidirectional(field.getName(), checkbox.selectedProperty());
        editorNode = checkbox;
      }
      else {
        textBox = new TextField(String.valueOf(value));
        textBox.setEditable(editable);
        gameDataBeanPathAdapter.bindBidirectional(field.getName(), textBox.textProperty());
        editorNode = textBox;
      }

      GridPane.setMargin(editorNode, new Insets(5, 5, 5, 10));
      GridPane.setConstraints(editorNode, 1, row);
      grid.getChildren().addAll(condLabel, editorNode);


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
