package com.nima.hud;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.nima.actors.Player;
import com.nima.components.MapObjectComponent;
import com.nima.managers.DefaultCollisionListener;
import com.nima.managers.EntityManager;

/**
 * The game HUD
 */
public class Hud extends DefaultCollisionListener {

  private final Stage stage;
  private Button dockButton;
  private final Label stationLabel;

  public Hud() {
    stage = new Stage();
    Gdx.input.setInputProcessor(stage);

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
    dockButton = new TextButton("Dock!", style);
    dockButton.padTop(20);
    dockButton.padRight(20);
    dockButton.addListener(new InputListener() {
      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("button");
      }
    });
    table.add(dockButton).expandX().align(Align.right);

    dockButton.setVisible(false);

    stage.addActor(table);

    EntityManager.getInstance().addCollisionListener(this);
  }

  public void render() {
    stage.act();
    stage.draw();
  }


  @Override
  public void collisionStart(Player player, Entity mapObjectEntity) {
    MapObjectComponent mapObjectComponent = mapObjectEntity.getComponent(MapObjectComponent.class);
    String name = mapObjectComponent.getName();

    stationLabel.setText("Enter '" + name + "'");
  }

  @Override
  public void collisionEnd(Player player, Entity mapObjectEntity) {
    stationLabel.setText("");
  }
}
