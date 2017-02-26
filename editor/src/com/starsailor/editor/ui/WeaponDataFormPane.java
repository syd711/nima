package com.starsailor.editor.ui;

import com.starsailor.data.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.Arrays;

/**
 *
 */
public class WeaponDataFormPane extends FormPane {

  private BodyData bodyData;
  private SteeringData steeringData;

  public WeaponDataFormPane(MainPane mainPane) {
    super(mainPane, Arrays.asList("bodyData", "steeringData"));
  }

  @Override
  public void setData(GameData gameData) throws Exception {
    super.setData(gameData);
    if(gameData == null) {
      return;
    }

    WeaponData weaponData = (WeaponData) gameData;
    boolean extendable = ((WeaponData) gameData).getParent() != null;



    bodyData = new BodyData(weaponData.getBodyData());
    if(!weaponData.isBodyDataExtended()) {
      bodyData = weaponData.getBodyData();
    }

    steeringData = new SteeringData(weaponData.getSteeringData());
    if(!weaponData.isSteeringDataExtended()) {
      steeringData = weaponData.getSteeringData();
    }

    createSection(bodyData, "Body Data", extendable, new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if(observable instanceof BooleanProperty) {
          boolean selected = (boolean) newValue;
          if(selected) {
            weaponData.setBodyData(null);
          }
          else {
            weaponData.setBodyData(bodyData);
          }
        }
      }
    });


    createSection(steeringData, "Steering Data", extendable, new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if(observable instanceof BooleanProperty) {
          boolean selected = (boolean) newValue;
          if(selected) {
            weaponData.setSteeringData(null);
          }
          else {
            weaponData.setSteeringData(steeringData);
          }
        }
      }
    });

  }
}
