import Controller.Keyboard;

import Model.Game;

import View.Window;

public class Main {
    public static void main(String[] args) throws InterruptedException {
   	
    	Game game = new Game();
    	Keyboard keyboard = new Keyboard(game);
        Window window = new Window("Shinobi World",game);
        window.setKeyListener(keyboard);

        
    }
}
