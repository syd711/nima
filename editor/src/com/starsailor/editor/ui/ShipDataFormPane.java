package com.starsailor.editor.ui;

import com.starsailor.data.GameData;
import com.starsailor.data.ShipData;

import java.util.Arrays;

/**
 *
 */
public class ShipDataFormPane extends FormPane {

  public ShipDataFormPane(MainPane mainPane) {
    super(mainPane, Arrays.asList("bodyData", "steeringData"));
  }

  @Override
  public void setData(GameData gameData) throws Exception {
    super.setData(gameData);

    ShipData shipData = (ShipData) gameData;
    createSection(shipData, "Ship Data");
    createSection(shipData.getBodyData(), "Body Data");
  }
}
