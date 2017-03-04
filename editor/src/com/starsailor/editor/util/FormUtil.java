package com.starsailor.editor.util;

import com.starsailor.model.GameData;
import com.starsailor.model.GameDataWithId;
import com.starsailor.editor.UIController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public class FormUtil {
  public static String showInputDialog(String title, String header, String label) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle(title);
    dialog.setHeaderText(header);
    dialog.setContentText(label);

    // Traditional way to get the response value.
    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()){
      return result.get();
    }
    return null;
  }


  public static void showError(final String message, final Throwable e) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fehler");
        alert.setHeaderText("Fehler");
        alert.setContentText(new String(("Ups, das hätte nicht passieren dürfen: " + message + " [" + e.getMessage() + "]").getBytes(), Charset.forName("utf-8")));
        alert.showAndWait();
      }
    });
  }

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

  public static ListView addListChooser(GridPane grid, GameData data, Field field, int row, List<GameDataWithId> sourceData, List<GameDataWithId> targetData) {
    try {
      String label = splitCamelCase(StringUtils.capitalize(field.getName())) + " (source -> target):";
      List<String> value = (List<String>) field.get(data);
      Label condLabel = new Label(label);
      GridPane.setHalignment(condLabel, HPos.RIGHT);
      GridPane.setConstraints(condLabel, 0, row);

      HBox wrapper = new HBox(10);
      wrapper.setMaxHeight(160);

      ListView<GameDataWithId> source = new ListView<GameDataWithId>();
      source.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
      ObservableList<GameDataWithId> available = FXCollections.observableArrayList(sourceData);
      source.setItems(available);

      ListView<GameDataWithId> target = new ListView<GameDataWithId>();
      target.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
      ObservableList<GameDataWithId> assignments = FXCollections.observableArrayList(targetData);
      target.setItems(assignments);


      VBox buttons = new VBox(10);
      Button toRight = new Button("->");
      toRight.setOnAction(new EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
          GameDataWithId selectedItem = source.getSelectionModel().getSelectedItem();
          if(selectedItem != null) {
            assignments.add(selectedItem);
            available.remove(selectedItem);

            try {
              field.set(data, toIdList(assignments));
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            }
          }
        }
      });


      Button toLeft = new Button("<-");
      toLeft.setOnAction(new EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
          GameDataWithId selectedItem = target.getSelectionModel().getSelectedItem();
          if(selectedItem != null) {
            assignments.remove(selectedItem);
            available.add(selectedItem);

            try {
              field.set(data, toIdList(assignments));
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            }
          }
        }
      });

      buttons.getChildren().addAll(toRight, toLeft);


      wrapper.getChildren().addAll(source, buttons, target);

      GridPane.setMargin(wrapper, new Insets(5, 5, 5, 10));
      GridPane.setConstraints(wrapper, 1, row);
      grid.getChildren().addAll(condLabel, wrapper);

      return target;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static ComboBox addBindingComboBoxWithDefaults(GridPane grid, GameData data, Field field, int row, List<String> values) {
    try {
      String label = splitCamelCase(StringUtils.capitalize(field.getName())) + ":";
      String value = (String) field.get(data);
      Label condLabel = new Label(label);
      GridPane.setHalignment(condLabel, HPos.RIGHT);
      GridPane.setConstraints(condLabel, 0, row);

      ObservableList<Object> options = FXCollections.observableArrayList(values);
      ComboBox comboBox = new ComboBox(options);
      comboBox.setValue(value);
      comboBox.valueProperty().addListener(new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
          try {
            field.set(data, ""+newValue);
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }

          data.setExtendParentData(false);
        }
      });

      GridPane.setMargin(comboBox, new Insets(5, 5, 5, 10));
      GridPane.setConstraints(comboBox, 1, row);
      grid.getChildren().addAll(condLabel, comboBox);

      return comboBox;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  public static ComboBox addBindingComboBox(GridPane grid, GameData data, Field field, int row, List<GameDataWithId> values) {
    try {
      String label = splitCamelCase(StringUtils.capitalize(field.getName())) + ":";

      Label condLabel = new Label(label);
      GridPane.setHalignment(condLabel, HPos.RIGHT);
      GridPane.setConstraints(condLabel, 0, row);

      ObservableList<Object> options = FXCollections.observableArrayList(values);
      ComboBox comboBox = new ComboBox(options);

      Object value = field.get(data);
      if(value != null) {
        int id = (int) value;
        GameDataWithId model = UIController.getInstance().getGameDataLoader().getModel(id);
        comboBox.setValue(model);
      }
      comboBox.valueProperty().addListener(new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
          try {
            GameDataWithId gameDataWithId = (GameDataWithId) newValue;
            if(gameDataWithId != null) {
              field.set(data, gameDataWithId.getId());
            }
            else {
              field.set(data, null);
            }
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }

          data.setExtendParentData(false);
        }
      });

      GridPane.setMargin(comboBox, new Insets(5, 5, 5, 10));
      GridPane.setConstraints(comboBox, 1, row);
      grid.getChildren().addAll(condLabel, comboBox);

      return comboBox;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  public static Node addBindingComboBox(GridPane grid, GameData data, Field field, int row, File folder, String suffix) {
    Node editorNode = null;
    try {
      Object value = field.get(data);
      String label = splitCamelCase(StringUtils.capitalize(field.getName())) + ":";
      Label condLabel = new Label(label);
      GridPane.setHalignment(condLabel, HPos.RIGHT);
      GridPane.setConstraints(condLabel, 0, row);

      ObservableList<String> options =
          FXCollections.observableArrayList(getFilesAsComboEntries(folder, suffix));

      ComboBox comboBox = new ComboBox(options);
      comboBox.setValue(value);
      comboBox.valueProperty().addListener(new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
          try {
            field.set(data, newValue);
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }

          data.setExtendParentData(false);
        }
      });
      editorNode = comboBox;

      GridPane.setMargin(editorNode, new Insets(5, 5, 5, 10));
      GridPane.setConstraints(editorNode, 1, row);
      grid.getChildren().addAll(condLabel, editorNode);


    } catch (Exception e) {
      e.printStackTrace();
    }
    return editorNode;
  }

  public static Node addBindingFormField(GridPane grid, GameData data, Field field, int row, boolean editable) {
    Node editorNode = null;
    try {
      Object value = field.get(data);
      String label = splitCamelCase(StringUtils.capitalize(field.getName())) + ":";

      Label condLabel = new Label(label);
      GridPane.setHalignment(condLabel, HPos.RIGHT);
      GridPane.setConstraints(condLabel, 0, row);


      if(value instanceof Boolean) {
        CheckBox checkbox = new CheckBox();
        checkbox.setSelected((Boolean) value);
        checkbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
          @Override
          public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            try {
              field.set(data, newValue);
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            }
          }
        });
        editorNode = checkbox;
      }
      else {
        TextField textBox = new TextField(String.valueOf(value));
        textBox.setEditable(editable);
        textBox.textProperty().addListener(new ChangeListener<String>() {
          @Override
          public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            try {
              Object value = newValue;
              if(field.getType().equals(long.class)) {
                value = Long.parseLong(newValue);
              }
              else if(field.getType().equals(float.class)) {
                value = Float.parseFloat(newValue);
              }
              else if(field.getType().equals(float.class)) {
                value = Integer.parseInt(newValue);
              }
              field.set(data, value);
            } catch (Exception e) {
              e.printStackTrace();
            }

            data.setExtendParentData(false);
          }
        });
        editorNode = textBox;
      }

      GridPane.setMargin(editorNode, new Insets(5, 5, 5, 10));
      GridPane.setConstraints(editorNode, 1, row);
      grid.getChildren().addAll(condLabel, editorNode);


    } catch (Exception e) {
      e.printStackTrace();
    }
    return editorNode;
  }

  public static Field getField(Class clazz, String name) {
    Field[] declaredFields = clazz.getDeclaredFields();
    for(Field declaredField : declaredFields) {
      declaredField.setAccessible(true);
      if(declaredField.getName().equals(name)) {
        return declaredField;
      }
    }

    return getField(clazz.getSuperclass(), name);
  }

  private static String[] getFilesAsComboEntries(File folder) {
    return folder.list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return new File(dir, name).isDirectory();
      }
    });
  }

  private static String[] getFilesAsComboEntries(File folder, String suffix) {
    if(suffix == null) {
      return getFilesAsComboEntries(folder);
    }

    String[] list = folder.list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(suffix);
      }
    });

    List<String> result = new ArrayList<>();
    for(String s : list) {
      if(s.indexOf(".") != -1) {
        result.add(s.substring(0, s.lastIndexOf(".")));
      }
    }
    return result.toArray(new String[0]);

  }

  static List<Integer> toIdList(List<GameDataWithId> ids) {
    List<Integer> result = new ArrayList<>();
    for(GameDataWithId id : ids) {
      result.add(id.getId());
    }
    return result;
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

  public static void setColorForTitledPane(Node node, Color color) {
    Node parent = node.getParent();
    while(!(parent instanceof GridPane)) {
      parent = parent.getParent();
    }

    ((Pane) parent).setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
  }
}
