package com.nima.hud;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

/**
 * The game HUD
 */
public class Hud {

  private final Stage stage;

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

    Label label = new Label("This is the HUD!", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    table.add(label).expandX().padTop(20);

    stage.addActor(table);
  }

  public void render() {
    stage.act();
    stage.draw();
  }
}
