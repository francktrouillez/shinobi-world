package Model;

import java.util.ArrayList;

import View.Sound;


public class BlockBreakable extends Block implements Deletable, Activable { //méthodes pour supprimer l'objet ou l'activer

    private ArrayList<DeletableObserver> observers = new ArrayList<DeletableObserver>();
    private int lifepoints = 0;


    //methode lors de la creation d un nouveau jeu
    public BlockBreakable(int X, int Y, int lifepoints, Plateau p) {
        super(X, Y, "breakableFull",p);
        this.lifepoints = lifepoints;

    }


    //methode pour la sauvegarde afin de reconstruire les blocs dans l etat dans lequel ils etaient
    public BlockBreakable(int X, int Y, int lifepoints, Plateau p, String color) {
        super(X, Y, color,p);
        this.lifepoints = lifepoints;
    }


    public void activate(){

        if (lifepoints <= 1){
            crush();
        }
        else {
            lifepoints--;
            if (lifepoints < 2) {

            	this.color = "breakableLow";
            }
            else if (lifepoints < 4) {
            	this.color = "breakableMid";
            }

        }
        Sound.playEffect("rockCrush");
    }


    public void crush(){
        notifyDeletableObserver();
    }



    public void attachDeletable(DeletableObserver po) {
        observers.add(po);
    }


    public void notifyDeletableObserver() {
        for (DeletableObserver o : observers) {
            o.delete(this, null, null);
        }


    }


    public boolean isObstacle() {
        return true;
    }


	public int getLifePoints() {
		return lifepoints;
	}

}
