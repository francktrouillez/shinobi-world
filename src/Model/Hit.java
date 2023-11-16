package Model;

import Model.Thread.HitThread;
import View.Sound;

//classe qui a uniquement un role graphique et dont une instance sera cree a chaque kick donne par un joueur ou un mechant/boss
public class Hit extends GameObject implements Directable{

	private int orientation;
	private double size = 1;
	
	
	public Hit(int[] coord, int orientation, Plateau p) {
		
		super(coord[0],coord[1],"hit", p);
		this.orientation = orientation;
		synchronized(Game.keyPlateau) {
			p.addObject(this);
		}
		HitThread ht = new HitThread(this);  //thread qui permet d augmenter progressivement la taille de l image avant de detruire l objet
		Thread t = new Thread(ht);
		t.start();
		Sound.playEffect("hit");
	}
	
	
	
	//methode pour detruire l objet
	public void destroy() {
		synchronized(Game.keyPlateau) {
			getPlateau().removeObject(this);
		}
	}
	
	
	//Accesseurs
	

	public boolean isObstacle() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setSize(double size) {
		this.size = size;
	}
	
	public double getSize() {
		return size;
	}

	public int getDirection() {
		// TODO Auto-generated method stub
		return orientation;
	}
}
