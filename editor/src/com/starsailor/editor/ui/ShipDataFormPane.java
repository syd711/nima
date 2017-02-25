package com.starsailor.editor.ui;

import com.starsailor.data.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.Arrays;

/**
 *
 */
public class ShipDataFormPane extends FormPane {

  public ShipDataFormPane(MainPane mainPane) {
    super(mainPane, Arrays.asList("bodyData", "steeringData", "spineData", "distanceData", "statusData"));
  }

  @Override
  public void setData(GameData gameData) throws Exception {
    super.setData(gameData);
    if(gameData == null) {
      return;
    }

    ShipData shipData = (ShipData) gameData;
    boolean extendable = ((ShipData) gameData).getParent() != null;

    SpineData spineData = new SpineData(shipData.getSpineData());
    spineData.setExtendParentData(shipData.isSpineDataExtended());
    createSection(spineData, "Spine Data", extendable, new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if(observable instanceof BooleanProperty) {
          boolean selected = (boolean) newValue;
          if(selected) {
            shipData.setSpineData(null);
          }
          else {
            shipData.setSpineData(spineData);
          }
        }
      }
    });

    StatusData statusData = new StatusData(shipData.getStatusData());
    statusData.setExtendParentData(shipData.isStatusDataExtended());
    createSection(statusData, "Status Data", extendable, new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if(observable instanceof BooleanProperty) {
          boolean selected = (boolean) newValue;
          if(selected) {
            shipData.setStatusData(null);
          }
          else {
            shipData.setStatusData(statusData);
          }
        }
      }
    });


    BodyData bodyData = new BodyData(shipData.getBodyData());
    bodyData.setExtendParentData(shipData.isBodyDataExtended());
    createSection(bodyData, "Body Data", extendable, new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if(observable instanceof BooleanProperty) {
          boolean selected = (boolean) newValue;
          if(selected) {
            shipData.setBodyData(null);
          }
          else {
            shipData.setBodyData(bodyData);
          }
        }
      }
    });


    SteeringData steeringData = new SteeringData(shipData.getSteeringData());
    steeringData.setExtendParentData(shipData.isSteeringDataExtended());
    createSection(steeringData, "Steering Data", extendable, new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if(observable instanceof BooleanProperty) {
          boolean selected = (boolean) newValue;
          if(selected) {
            shipData.setSteeringData(null);
          }
          else {
            shipData.setSteeringData(steeringData);
          }
        }
      }
    });

    DistanceData distanceData = new DistanceData(shipData.getDistanceData());
    distanceData.setExtendParentData(shipData.isDistanceDataExtended());
    createSection(distanceData, "Distance Data", extendable, new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if(observable instanceof BooleanProperty) {
          boolean selected = (boolean) newValue;
          if(selected) {
            shipData.setDistanceData(null);
          }
          else {
            shipData.setDistanceData(distanceData);
          }
        }
      }
    });
  }
}
