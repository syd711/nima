package com.nima.hud;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.nima.actors.Player;
import com.nima.components.MapObjectComponent;
import com.nima.managers.DefaultCollisionListener;
import com.nima.managers.EntityManager;
import com.nima.managers.GameStateManager;

/**
 * The game HUD
 */
public class Hud extends DefaultCollisionListener {

  private final Stage stage;
  private Button dockButton;
  private final Label stationLabel;
  private final Texture bground;

  public Hud() {
    stage = new Stage();

    Table table = new Table();
    table.top();
    table.setFillParent(true);

    Stack healthBarStack = new Stack();
    Image healthBarBG = new Image(new Texture("hud/healthbg.png"));
    Image healthBarFG = new Image(new Texture("hud/healthfg.png"));
    healthBarStack.add(healthBarBG);
    healthBarStack.add(healthBarFG);

    table.add(healthBarStack).expandX().align(Align.left);

    stationLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    table.add(stationLabel).expandX().padTop(20);


    TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
    style.font = new BitmapFont();
    style.fontColor = Color.RED;
    dockButton = new TextButton("Leave Station", style);
    dockButton.padTop(20);
    dockButton.padRight(20);
    dockButton.addListener(new ChangeListener() {
      @Override
      public void changed (ChangeEvent event, Actor actor) {
        GameStateManager.getInstance().leaveStationMode();
      }
    });
    table.add(dockButton).expandX().align(Align.right);

    dockButton.setVisible(false);

    stage.addActor(table);

    bground = new Texture(Gdx.files.internal("stations/background/planet.jpg"));

    EntityManager.getInstance().addCollisionListener(this);
  }

  public void render() {
    stage.act();

    if(GameStateManager.getInstance().isStationMode()) {
      dockButton.setVisible(true);
      stage.getBatch().begin();
      stage.getBatch().draw(bground, 0, 0, 1050, 1050);
      stage.getBatch().end();
      Gdx.input.setInputProcessor(stage);
    }
    else {
      dockButton.setVisible(false);
    }

    stage.draw();
  }


  @Override
  public void collisionStart(Player player, Entity mapObjectEntity) {
    MapObjectComponent mapObjectComponent = mapObjectEntity.getComponent(MapObjectComponent.class);
    if(mapObjectComponent != null) {
      String name = mapObjectComponent.getName();

      stationLabel.setText("Enter '" + name + "'");
    }

  }

  @Override
  public void collisionEnd(Player player, Entity mapObjectEntity) {
    stationLabel.setText("");
  }
}
