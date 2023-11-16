package Model;

import java.util.ArrayList;

import Model.Thread.WaveThread;
import View.Sound;

public class Wave extends GameObject{
	private Player p;
	private int damage;
	private double size = 1;
	
	public Wave(Player p, int damage) {
		super(p.getPosX(),p.getPosY(), "wave", p.getPlateau());
		this.p = p;
		this.damage = damage;
		doDamage();
		Sound.playEffect("wave");
	}
	
	//methode qui inflige des degats a tous les objets dans le rayon de portee de la wave
	private void doDamage() {

		int posX = p.getPosX();
		int posY = p.getPosY();
		
		//coordonnees de toutes les cases dans un rayon de 3 blocs autour du joueur
		ArrayList<Integer> coordX = new ArrayList<Integer>(); 
		ArrayList<Integer> coordY = new ArrayList<Integer>();
		
		for (int i = -3; i <= 3;i++) { 
			for (int j = -3 + Math.abs(i); j <= 3- Math.abs(i);j++) {
				coordX.add(posX + i);
				coordY.add(posY + j);
			}
		}
		synchronized(Game.keyPlateau) {
			ArrayList<BlockBreakable> bb = new ArrayList<BlockBreakable>();
			for (GameObject go:p.getPlateau().getObjects()) {
				int x = go.getPosX();
				int y = go.getPosY();
				//si un objet avec de la vie se trouve dans la zone de degat, alors il perd de la vie
				for (int i = 0; i < coordX.size();i++) {
					if (x == coordX.get(i) && y == coordY.get(i)) {
						if (go instanceof Player && p != go) {
							((Player) go).loseLife(damage);
						}
						else if (go instanceof Mechant) {
							((Mechant) go).loseLife(damage);
						}else if (go instanceof Boss) {
							((Boss) go).loseLife(damage);
						}
						else if (go instanceof BlockBreakable) {
							bb.add((BlockBreakable)go);
							
						}
					}
				}
				
			}
			for (BlockBreakable b:bb) {
				for(int i = 0; i < 3;i++) {
					b.activate();
				}
				
			}
		}
		synchronized(Game.keyPlateau) {
			getPlateau().addObject(this);
		}
		WaveThread ht = new WaveThread(this); //thread qui n a qu une fonction graphique pour dessiner la wave 
		Thread t = new Thread(ht);
		t.start();
		
	}
	
	//methode pour detruire l objet
	public void destroy() {
		synchronized(Game.keyPlateau) {
			getPlateau().removeObject(this);
		}
	}
	
	
	//Accesseurs
	
	
	public void setSize(double size) {
		this.size = size;
	}
	
	public double getSize() {
		return size;
	}
	
	
	public Player getPlayer() {
		return p;
	}


	public boolean isObstacle() {
		return false;
	}
}
