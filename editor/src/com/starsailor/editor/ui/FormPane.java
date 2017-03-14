package com.starsailor.editor.ui;

import com.google.gson.annotations.Expose;
import com.starsailor.actors.*;
import com.starsailor.editor.UIController;
import com.starsailor.editor.util.FormUtil;
import com.starsailor.model.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

/**
 *
 */
public class FormPane extends BorderPane implements ChangeListener {
  private VBox dynamicForm;
  private MainPane mainPane;
  private List<String> ignoredFields;
  private GameData gameData;

  private static Map<Class,Boolean> collapsedStates = new HashMap<>();


  public FormPane(MainPane mainPane) {
    this(mainPane, new ArrayList<>());
  }

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
    this.gameData = gameData;
    dynamicForm.getChildren().clear();

    if(gameData != null) {
      createIdSection(gameData);
    }
  }

  private void createIdSection(GameData gameData) throws Exception {
    GridPane categoryDetailsForm = FormUtil.createFormGrid();
    int index = 0;

    Field idField = FormUtil.getField(gameData.getClass(),"id");
    idField.setAccessible(true);
    FormUtil.addBindingFormField(categoryDetailsForm, gameData, idField, index, false);
    index++;

    Field nameField = FormUtil.getField(gameData.getClass(),"name");
    nameField.setAccessible(true);
    TextField textField = (TextField) FormUtil.addBindingFormField(categoryDetailsForm, gameData, nameField, index, true);
    textField.textProperty().addListener(this);
    index++;

    TitledPane section = FormUtil.createSection(dynamicForm, categoryDetailsForm, "Model Info", false);
    section.setExpanded(true);
  }

  protected void createSection(GameData gameData, String title, boolean extendable, ChangeListener changeListener) throws Exception {
    createSection(gameData, title, Collections.emptyList(), extendable, changeListener);
  }

  protected void createSection(GameData gameData, String title, List<String> ignoredSectionFields, boolean extendable, ChangeListener changeListener) throws Exception {
    if(title == null) {
      title = gameData.toString();
    }
    int index = 0;
    GridPane categoryDetailsForm = FormUtil.createFormGrid();

    if(extendable) {
      Field extendParentDataField = FormUtil.getField(gameData.getClass(), "extendParentData");
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
        if(ignoredFields.contains(name) || ignoredSectionFields.contains(name)) {
          continue;
        }

        Node editor = getCustomEditor(categoryDetailsForm, gameData, field, index);
        if(editor == null) {
          FormUtil.addBindingFormField(categoryDetailsForm, gameData, field, index, true);
        }
        index++;
      }
    }
    TitledPane section = FormUtil.createSection(dynamicForm, categoryDetailsForm, title, false);
    section.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        collapsedStates.put(gameData.getClass(), section.isExpanded());
      }
    });

    Boolean expanded = collapsedStates.get(gameData.getClass());
    if(expanded != null) {
      section.setExpanded(expanded);
    }

  }


  private Boolean isExtending(GameData gameData) {
    try {
      Field extendParentDataField = FormUtil.getField(gameData.getClass(), "extendParentData");
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

  public Node getCustomEditor(GridPane grid, GameData sectionData, Field field, int row) throws IllegalAccessException {
    if(field.getName().equals("spine") && gameData instanceof ShipData) {
      return FormUtil.addBindingComboBox(grid, sectionData, field, row, new File("../../core/assets/spines/ships"), null);
    }
    if(field.getName().equals("spine") && gameData instanceof ShieldData) {
      return FormUtil.addBindingComboBox(grid, sectionData, field, row, new File("../../core/assets/spines/shields"), null);
    }
    if(field.getName().equals("spine") && gameData instanceof WeaponData) {
      return FormUtil.addBindingComboBox(grid, sectionData, field, row, new File("../../core/assets/spines/weapons"), null);
    }

    if(field.getName().equals("defaultAnimation") && gameData instanceof ShipData) {
      return FormUtil.addBindingComboBoxWithDefaults(grid, sectionData, field, row, SpineShipAnimations.asStringList());
    }
    if(field.getName().equals("defaultAnimation") && gameData instanceof ShieldData) {
      return FormUtil.addBindingComboBoxWithDefaults(grid, sectionData, field, row, SpineShieldAnimations.asStringList());
    }
    if(field.getName().equals("defaultAnimation") && gameData instanceof WeaponData) {
      return FormUtil.addBindingComboBoxWithDefaults(grid, sectionData, field, row, SpineWeaponAnimations.asStringList());
    }

    if(field.getName().equals("shield")) {
      List<GameDataWithId> entries = UIController.getInstance().getShields();
      return FormUtil.addBindingComboBox(grid, sectionData, field, row, entries);
    }
    else if(field.getName().equals("shipType")) {
      List<GameDataWithId> entries = UIController.getInstance().getShips();
      return FormUtil.addBindingComboBox(grid, sectionData, field, row, entries);
    }
    else if(field.getName().equals("sound")) {
      return FormUtil.addBindingComboBox(grid, sectionData, field, row, new File("../../core/assets/sounds/"), ".wav");
    }
    else if(field.getName().equals("collisionEffect")) {
      return FormUtil.addBindingComboBox(grid, sectionData, field, row, new File("../../core/assets/particles/"), ".p");
    }
    else if(field.getName().equals("sprite")) {
      return FormUtil.addBindingComboBox(grid, sectionData, field, row, new File("../../core/assets/textures/weapons/"), ".png");
    }
    else if(field.getName().equals("category")) {
      return FormUtil.addBindingComboBoxWithDefaults(grid, sectionData, field, row, Arrays.asList("primary", "secondary", "emergency"));
    }
    else if(field.getName().equals("route")) {
      List<String> routes = UIController.getInstance().getRoutes();
      routes.add(0, null);
      return FormUtil.addBindingComboBoxWithDefaults(grid, sectionData, field, row, routes);
    }
    else if(field.getName().equals("routeIndex")) {
      List<String> values = new ArrayList<>();
      for(int i=0; i<50; i++) {
        values.add(String.valueOf(i));
      }
      return FormUtil.addBindingComboBoxWithDefaults(grid, sectionData, field, row, values);
    }
    else if(field.getName().equals("fraction")) {
      return FormUtil.addBindingComboBoxWithDefaults(grid, sectionData, field, row, Fraction.asStringList());
    }
    else if(field.getName().equals("defaultSteering")) {
      return FormUtil.addBindingComboBoxWithDefaults(grid, sectionData, field, row, Steering.defaultSteeringList());
    }
    else if(field.getName().equals("battleSteering")) {
      return FormUtil.addBindingComboBoxWithDefaults(grid, sectionData, field, row, Steering.battleSteeringList());
    }
    else if(field.getName().equals("formationOwner")) {
      List<GameDataWithId> entries = UIController.getInstance().getShipItems();
      entries.add(0, null);
      return FormUtil.addBindingComboBox(grid, sectionData, field, row, entries);
    }
    else if(field.getName().equals("weapons")) {
      StatusData statusData = (StatusData) sectionData;
      List<Integer> weaponIds = statusData.getWeapons();
      List<GameDataWithId> weapons = UIController.getInstance().getGameDataLoader().getModels(weaponIds);
      List allWeapons = UIController.getInstance().getGameDataLoader().getWeapons();
      UIController.getInstance().removeDuplicates(allWeapons, weapons);
      return FormUtil.addListChooser(grid, sectionData, field, row, allWeapons, weapons);
    }
    return null;
  }

}
