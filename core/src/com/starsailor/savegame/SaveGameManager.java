package com.starsailor.savegame;

import com.badlogic.gdx.Gdx;
import com.starsailor.GameStateManager;
import com.starsailor.actors.Galaxy;
import com.starsailor.actors.Player;
import com.starsailor.managers.EntityManager;
import com.starsailor.model.JsonDataFactory;
import com.starsailor.ui.UIManager;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Manager class for loading and storing the current game state.
 */
public class SaveGameManager {
  public enum SaveSlot {SLOT_1, SLOT_2, SLOT_3};

  public static SaveSlot activeSlot = SaveSlot.SLOT_1;

  public static void load(SaveSlot slot) {
    SaveGameItem saveGame = JsonDataFactory.loadDataEntity(resolveSaveGameFile(slot, null), SaveGameItem.class);
    List<SaveGameItem> items = saveGame.getItems();
    for(SaveGameItem item : items) {

    }

  }

  //----------------------- Game Saving ------------------------------------------------

  public static void save() {
    UIManager.getInstance().getHudStage().getSavePanel().activate();

    saveGalaxy();
    saveGameState();
  }

  private static void saveGameState() {
    SaveGameItem saveGame = new SaveGameItem();
    saveGame.store("timestamp", new Date());
    saveGame.store("galaxy", Galaxy.getInstance().getName());
    save(saveGame, "savegame");
  }

  /**
   * Stores the state of the current galaxy.
   * Used when the player changes the galaxy
   */
  private static void saveGalaxy() {
    SaveGameItem saveGame = new SaveGameItem();
    saveGame.store("timestamp", new Date());

    List<Saveable> entities = EntityManager.getInstance().getEntities(Saveable.class);
    for(Saveable entity : entities) {
      if(entity instanceof Player) {
        continue;
      }
      SaveGameItem entityState = new SaveGameItem();
      entity.save(entityState);
      saveGame.addSaveGameItem(entityState);
    }

    save(saveGame, Galaxy.getInstance().getName());
  }

  /**
   * Stors the given save game entity for the given slot
   * @param saveGame the savegame data to write
   * @param entity the entity of the slot to save
   */
  private static void save(SaveGameItem saveGame, String entity) {
    File file = resolveSaveGameFile(activeSlot, entity);
    JsonDataFactory.saveDataEntity(file, saveGame);
    Gdx.app.log(GameStateManager.class.getName(), "Saved game to " + file.getAbsolutePath());
  }


  //----------------------- Helper ------------------------------------------------
  /**
   * Save games are stored into folder for their slot
   * @param slot the slot to resolve the filename for
   * @param entity the entity name to load or save
   */
  private static File resolveSaveGameFile(SaveSlot slot, String entity) {
    String folder = slot.name().toLowerCase();
    return new File("savegames/" + folder + "/" + entity + ".json");
  }
}
