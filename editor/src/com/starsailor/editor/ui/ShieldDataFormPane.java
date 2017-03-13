package com.starsailor.editor.ui;

import com.starsailor.model.GameData;
import com.starsailor.model.ShieldData;
import com.starsailor.model.SpineData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.Arrays;

/**
 *
 */
public class ShieldDataFormPane extends FormPane {

  private SpineData spineData;

  public ShieldDataFormPane(MainPane mainPane) {
    super(mainPane, Arrays.asList("bodyData", "steeringData", "spineData", "distanceData", "statusData"));
  }

  @Override
  public void setData(GameData gameData) throws Exception {
    super.setData(gameData);

    ShieldData shieldData = (ShieldData) gameData;
    boolean extendable = ((ShieldData) gameData).getParent() != null;

    if(gameData != null) {
      createSection(gameData, "Shield Data", false, this);
    }

    spineData = new SpineData(shieldData.getSpineData());
    if(!shieldData.isSpineDataExtended()) {
      spineData = shieldData.getSpineData();
    }

    createSection(spineData, "Spine Data", extendable, new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if(observable instanceof BooleanProperty) {
          boolean selected = (boolean) newValue;
          if(selected) {
            shieldData.setSpineData(null);
          }
          else {
            shieldData.setSpineData(spineData);
          }
        }
      }
    });
  }
}
