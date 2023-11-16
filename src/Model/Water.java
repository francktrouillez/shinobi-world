package Model;

public class Water extends Drink{



	//constructeur pour la sauvegarde
	public Water(int x, int y, int value) {
		super(x, y, "water", value);

	}

	//constructeur pour l apparition dans le jeu
	public Water(int x,int y, int value, Plateau p) {
		super(x,y, "water",value,p);

	}

	//methode qui ajoute l eau a l inventaire et l enleve des objets du plateau
	public void activate(Player p) {
		// TODO Auto-generated method stub
		p.getGame().print("De l'eau a t ajout  l'inventaire, sa valeur est: "+getValue());
		p.addToLoot(this);
		this.preneur = p;
		notifyDeletableObserver();
	}



}
