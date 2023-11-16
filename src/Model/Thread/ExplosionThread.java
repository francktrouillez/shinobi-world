package Model.Thread;

import Model.Explosion;
import Model.Game;
import Model.Plateau;

public class ExplosionThread implements Runnable{
	
	private Explosion explosion;
	private Plateau p;
	private int explosionTime;
	private boolean done = false;
	private boolean haveToWait = true;
	
	public ExplosionThread(Explosion e) {
		this.explosion = e;
		this.explosionTime = e.getExplosionTime();
		this.p = e.getPlateau();
		
	}

	//methode qui a pour but de faire apparaitre l image de l explosion pendant un certain temps
	public void run() {
		while (!done) {
				try {
					if (haveToWait) {
						Thread.sleep(explosionTime);
						haveToWait = false;
					}
					synchronized(Game.keyPlateau) {
						done = true;
						p.getObjects().remove(explosion);
					}
					} catch (InterruptedException e) {
						e.printStackTrace();
				}	
	
		}
	}

}
