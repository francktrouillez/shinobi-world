package Model;

import Model.Thread.ExplosionThread;
import View.Sound;

//classe qui n a pour seul but que la creation d une animation accompagnee d un effet sonore
public class Explosion extends GameObject{
	
	//duree de l animation
	private int explosionTime = 500;
	
	
	public Explosion(Fireball f) {
		
		super(f.getPosX(),f.getPosY(),"explosion", f.getPlateau());
		f.getPlateau().getObjects().add(this);
		Thread e = new Thread(new ExplosionThread(this));
		e.start();
		Sound.playEffect("explosion");
	}
	
	
	//Accesseurs
	
	
	public int getExplosionTime() {
		return explosionTime;
	}
	
	
	public boolean isObstacle() {
		return false;
	}

}
