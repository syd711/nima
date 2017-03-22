package com.starsailor.actors.states.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.starsailor.Game;
import com.starsailor.GameStateManager;
import com.starsailor.actors.Player;
import com.starsailor.actors.Selectable;
import com.starsailor.actors.Ship;
import com.starsailor.managers.SelectionManager;
import com.starsailor.managers.SteeringManager;
import com.starsailor.ui.UIManager;

/**
 *
 */
public class PlayerTradingState implements State<Player> {

  private Ship tradingShip;

  @Override
  public void enter(Player player) {
    Selectable selection = SelectionManager.getInstance().getSelection();
    tradingShip = (Ship) selection;

    Gdx.app.log(getClass().getName(), player + " entered " + this.getClass().getSimpleName());
    SteeringManager.setFollowTargetSteering(Player.getInstance().steerableComponent, tradingShip.steerableComponent);
    Game.inputManager.setNavigationEnabled(false);
  }

  @Override
  public void update(Player player) {
    float dst = Player.getInstance().positionComponent.getPosition().dst(tradingShip.positionComponent.getPosition());
    if(dst < tradingShip.getWidth()*2) {
      player.steerableComponent.setBehavior(null);
      UIManager.getInstance().getHudStage().getTradingPlayerPanel().activate();
      UIManager.getInstance().getHudStage().getTradingNpcPanel().activate();
      GameStateManager.getInstance().setPaused(true);
      Player.getInstance().changeState(PlayerStates.IDLE);
    }
  }

  @Override
  public void exit(Player player) {
    Game.inputManager.setNavigationEnabled(true);
  }

  @Override
  public boolean onMessage(Player player, Telegram telegram) {
    return false;
  }
}
