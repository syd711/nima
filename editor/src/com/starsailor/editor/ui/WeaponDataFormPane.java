package com.starsailor.editor.ui;

import com.starsailor.data.BodyData;
import com.starsailor.data.GameData;
import com.starsailor.data.SteeringData;
import com.starsailor.data.WeaponData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class WeaponDataFormPane extends FormPane {

  private BodyData bodyData;
  private SteeringData steeringData;

  public WeaponDataFormPane(MainPane mainPane) {
    super(mainPane, Arrays.asList("bodyData", "steeringData"));
  }


  private List<String> getIgnoreListForWeapon(WeaponData weaponData) {
    WeaponData.Types type = WeaponData.Types.valueOf(weaponData.getType().toUpperCase());

    switch(type) {
      case LASER: {
        return Arrays.asList("type", "durationMillis", "torque", "bulletDelay", "activationDistance");
      }
      case MISSILE: {
        return Arrays.asList("type", "durationMillis", "torque");
      }
      case PHASER: {
        return Arrays.asList("type", "torque", "bodyData", "steeringData", "activationDistance", "forceFactor");
      }
      case MINE: {
        return Arrays.asList("type", "durationMillis", "torque");
      }
      case FLARES: {
        return Arrays.asList("type", "category", "durationMillis", "activationDistance");
      }
    }
    return Collections.emptyList();
  }

  @Override
  public void setData(GameData gameData) throws Exception {
    super.setData(gameData);
    if(gameData == null) {
      return;
    }

    WeaponData weaponData = (WeaponData) gameData;
    boolean extendable = ((WeaponData) gameData).getParent().getId() != 30000;


    createSection(weaponData, "Bullet Data", getIgnoreListForWeapon(weaponData), extendable, null);

    if(weaponData.getBodyData() != null) {
      bodyData = new BodyData(weaponData.getBodyData());
      if(!weaponData.isBodyDataExtended()) {
        bodyData = weaponData.getBodyData();
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
    }


    if(weaponData.getSteeringData() != null) {
      steeringData = new SteeringData(weaponData.getSteeringData());
      if(!weaponData.isSteeringDataExtended()) {
        steeringData = weaponData.getSteeringData();
      }

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
}
