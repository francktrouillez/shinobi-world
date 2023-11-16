package Model;

import java.util.ArrayList;

import View.Sound;

public class Gold extends GameObject implements Focusable, Deletable {
	
	private int value;
	private Player preneur;
	
	private ArrayList<DeletableObserver> observers = new ArrayList<DeletableObserver>();
	
	
	public Gold(int x, int y, int value, Plateau p) {
		super(x,y, "gold", p);
		this.value = value;
	}
	
	
	public void attachDeletable(DeletableObserver po) {
		observers.add(po);
	}
	
	public void notifyDeletableObserver() {
		synchronized(Game.keyPlateau) {
        for (DeletableObserver o : observers) {
            o.delete(this, null, preneur);
        }
		}
	}

	//methode qui rajoute l argent au portefeuille du joueur et l enleve du plateau
	public void activate(Player p) {
		this.preneur = p;
		p.setGold(p.getGold()+this.value);
		notifyDeletableObserver();
		Sound.playEffect("gold");
		
	}
	
	
	//Accesseurs
	
	
	public int getValue() {
		return value;
	}
	
	public boolean isObstacle() {
		// TODO Auto-generated method stub
		return false;
	}
}
