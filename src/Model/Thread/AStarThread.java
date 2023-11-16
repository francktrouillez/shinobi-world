package Model.Thread;

import Model.AStar;
import Model.Game;
import Model.GameObject;
import Model.Player;

public class AStarThread implements Runnable{ 
	private Game g;
	private Player p;
	private int x;
	private int y;
	private GameObject go;

	
	//thread qui permet de donner tout les intervalles de temps une nouvelle direction et de se deplacer dans celle ci pour aller vers l objet
	public AStarThread(Game g, Player p, GameObject go) {
		this.g= g;
		this.p = p;
		this.x = go.getPosX();
		this.y = go.getPosY();
		this.go = go;
		
	}
	
	@Override
	public void run() {
		int direction = 0;
		synchronized(p) { 
		while(direction != -1) {
			direction = (new AStar(p.getPosX(), p.getPosY(), x, y, g.getActivePlateau().getObjects())).getNextStep();
			switch (direction) { 
				case 0 : g.movePlayer(1,0,p); break;
				case 1 : g.movePlayer(0,-1,p); break;
				case 2 : g.movePlayer(-1,0,p); break;
				case 3 : g.movePlayer(0,1,p); break;
			}
			this.x = go.getPosX();
			this.y = go.getPosY();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
	}
		

}
