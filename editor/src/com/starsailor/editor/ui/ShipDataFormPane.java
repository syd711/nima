package com.starsailor.editor.ui;

import com.starsailor.model.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.Arrays;

/**
 *
 */
public class ShipDataFormPane extends FormPane {

  private SpineData spineData;
  private StatusData statusData;
  private BodyData bodyData;
  private SteeringData steeringData;
  private DistanceData distanceData;

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


    spineData = new SpineData(shipData.getSpineData());
    if(!shipData.isSpineDataExtended()) {
      spineData = shipData.getSpineData();
    }

    statusData = new StatusData(shipData.getStatusData());
    if(!shipData.isStatusDataExtended()) {
      statusData = shipData.getStatusData();
    }

    bodyData = new BodyData(shipData.getBodyData());
    if(!shipData.isBodyDataExtended()) {
      bodyData = shipData.getBodyData();
    }

    steeringData = new SteeringData(shipData.getSteeringData());
    if(!shipData.isSteeringDataExtended()) {
      steeringData = shipData.getSteeringData();
    }

    distanceData = new DistanceData(shipData.getDistanceData());
    if(!shipData.isDistanceDataExtended()) {
      distanceData = shipData.getDistanceData();
    }

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
