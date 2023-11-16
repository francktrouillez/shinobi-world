package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Model.Game;

public class Keyboard implements KeyListener {
    private Game game;

    public Keyboard(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent event) {

        int key = event.getKeyCode();
      
        switch (key) { 
        case KeyEvent.VK_ESCAPE: 
        	game.getWindow().changePanel("menuPause");
        	break;
        case KeyEvent.VK_RIGHT:
            game.movePlayer(1, 0,game.getActivePlayer());
            break;
        case KeyEvent.VK_LEFT:
            game.movePlayer(-1, 0,game.getActivePlayer());
            break;
        case KeyEvent.VK_DOWN:
            game.movePlayer(0, 1,game.getActivePlayer());
            break;
        case KeyEvent.VK_UP:
            game.movePlayer(0, -1,game.getActivePlayer());
             break;
         case KeyEvent.VK_SPACE:
             game.action(game.getActivePlayer());
             break;
         case KeyEvent.VK_T:
             game.tirePlayer(); 
             break;
        case KeyEvent.VK_P:
             game.playerPos();
             break;
        case KeyEvent.VK_E:
       	 	game.playerEat();
       	 	break;
        case KeyEvent.VK_D:
       	 	game.playerDrink();
       	 	break;
        case KeyEvent.VK_A:
        	game.changeActivePlayer();
        	break;
        case KeyEvent.VK_F:
        	game.kickPlayer();
        	break;
        case KeyEvent.VK_R:
        	game.wavePlayer();
        	break;
        case KeyEvent.VK_C:
        	game.playerConsume();
        	break;
        case KeyEvent.VK_SHIFT:
        	game.playerSwitchSpeed();
        	break;
        case KeyEvent.VK_X:
        	game.gainXPPlayer();
        	break;
        case KeyEvent.VK_G:
        	game.gainGoldPlayer();
        	break;
       }    	
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
