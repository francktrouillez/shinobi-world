package Model;

import java.util.ArrayList;

public abstract class Drink extends GameObject implements Deletable, Focusable{

	private ArrayList<DeletableObserver> observers = new ArrayList<DeletableObserver>();
	private int value;
	protected Player preneur;
	
	
	//constructeur pour la sauvegarde d une boisson dans l inventaire d un joueur
	public Drink(int x,int y, String color, int value) {
		super(x,y, color);
		this.value = value;
	}
	
	//constructeur pour l apparition dans le jeu
	public Drink(int x,int y, String color, int value, Plateau p) {
		super(x,y,color,p);
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


	//methode qui sera definie selon la sous classe de boisson
	public abstract void activate(Player p);

	
	//Accesseurs
	
	
	public int getValue() {
		return this.value;
	}
	
	public boolean isObstacle() {
		return false;
	}
	
}
