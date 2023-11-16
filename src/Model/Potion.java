package Model;
import java.util.*;

public class Potion extends GameObject implements Focusable, Deletable {
	
	private Player p;
	private int duree;
	
	private ArrayList<DeletableObserver> observers = new ArrayList<DeletableObserver>();

	
	public Potion(int x,int y, Plateau p, int duree) {
		super(x,y,"potion",p);
		this.duree = duree;
	}
	
	public void attachDeletable(DeletableObserver po) {
		observers.add(po);
		
	}

	public void notifyDeletableObserver() {
		
		synchronized(Game.keyPlateau) {
			for (DeletableObserver o : observers) {
				o.delete(this, null, p);
			}
		}
	}

	//methode pour ajouter une potion a l inventaire du joueur et la supprimer des objets du plateau
	public void activate(Player p) {
		p.addToLoot(this);
		this.p = p;
		notifyDeletableObserver();
	}
	
	
	//Accesseurs
	
	
	public boolean isObstacle() {
		// TODO Auto-generated method stub
		return false;
	}
	public int getDuree() {
		return this.duree;
	}
}
