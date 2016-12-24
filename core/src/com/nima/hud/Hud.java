package com.nima.hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

/**
 * The game HUD
 */
public class Hud {

  private final Stage stage;

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

    Label label = new Label("This is the HUD!", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    table.add(label).expandX().padTop(20);


    TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
    style.font = new BitmapFont();
    style.fontColor = Color.RED;
    Button button = new TextButton("Dock!", style);
    button.padTop(20);
    button.padRight(20);
    button.addListener(new InputListener() {
      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("button");
      }
    });
    table.add(button).expandX().align(Align.right);

    button.setVisible(false);

    stage.addActor(table);
  }

  public void render() {
    stage.act();
    stage.draw();
  }
}
