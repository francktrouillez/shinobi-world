package Model.Thread;

import Model.Fireball;
import Model.Game;
import Model.GameObject;
import Model.Plateau;
import Model.Player;

public class FireballThread implements Runnable{
	private Fireball fireball;
	private int waitTime;
	private Plateau p;
	private boolean running = true;
	private boolean collision;


	
	
	public FireballThread(Fireball f) {
		this.fireball = f;
		this.waitTime = 1000/f.getSpeed();
		this.p = f.getPlateau();
		
	}

	//methode qui a pour but de faire avancer la fireball tant qu elle n a pas rencontre d obstacle
	//dans le cas ou elle a rencontre un obstacle, alors on dessine l image de la fireball explose et on sort de la boucle ou elle avance
	
	public void run() {
			while (running && p.getObjects().indexOf(fireball) != -1) {			
				try {
					Thread.sleep(waitTime);

					synchronized(Game.keyPlateau) {
						collision = false;
					
						for (GameObject go: p.getObjects()) {
							
							if (fireball.collide(go) && (go.isObstacle() || go instanceof Player)) {
								fireball.explode(go);
								fireball.setColor("fireballExploded");
								running = false;
								collision = true;
								break;
							}
							
						}
						if (!collision) {
							fireball.avance();
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
				}	
	}
	}

}
