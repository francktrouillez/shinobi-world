package Model.Thread;

import Model.Canon;
import Model.Game;

public class CanonThread implements Runnable {
	
	private Game g;
	private Canon c;
	
	public CanonThread(Game g, Canon c) {
		this.g = g;
		this.c = c;
	}
	
	//si un joueur se trouve sur le meme plateau qu un canon et la direction de ce canon pointe vers le joueur, alors le canon tire
	public void run() {
		while(true) {
			try {
			if(g.getActivePlayer().getPlateau() == c.getPlateau()) {
				if(c.getDirection()%2 == 1) {		
					if(c.getPosX() == g.getActivePlayer().getPosX() && c.getPosY() < g.getActivePlayer().getPosY()) {
						c.tire();
					}
				}
				else {
					if(c.getPosY() == g.getActivePlayer().getPosY() && c.getPosX() < g.getActivePlayer().getPosX()) {
							c.tire();	
					}				}
			}
				Thread.sleep(500);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
	}
}
