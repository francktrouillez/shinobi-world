package Model;

import java.util.ArrayList;

public class Food extends GameObject implements Deletable, Focusable{


	private ArrayList<DeletableObserver> observers = new ArrayList<DeletableObserver>();

	private int valeur;
	private Player preneur;


	//constructeur pour la sauvegarde d une nourriture dans l inventaire d un joueur
	public Food(int x, int y, int valeur) {
		super(x,y,"food");
		this.valeur = valeur;

	}

	//constructeur en jeu
	public Food(int x, int y, int valeur, Plateau p) {
		super(x,y,"food",p);
		this.valeur = valeur;

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



	//methode pour rajouter une instance de nourriture a l inventaire d un joueur quand il l active et la retirer du plateau
	public void activate(Player p) {
		p.getGame().print("De la nourriture a t ajoute  l'inventaire, sa valeur est:"+this.getValue());
		p.addToLoot(this);
		this.preneur = p;
		notifyDeletableObserver();

	}



	//Accesseurs


	public boolean isObstacle() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getValue() {
		return this.valeur;
	}


}
