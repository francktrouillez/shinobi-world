package Model;



public class Alcool extends Drink {

	private int alcool;

	//constructeur pour la sauvegarde lorsque l alcool est dans l inventaire
	public Alcool(int x,int y, int value, int alcool) {
		super(x,y,"alcool", value);
		this.alcool = alcool;
	}

	//constructeur pour un nouveau jeu ou un alcool par terre
	public Alcool(int x,int y, int value, int alcool, Plateau p) {
		super(x,y,"alcool",value, p);
		this.alcool = alcool;
	}


	//methode pour rajouter l alcool a l inventaire
	public void activate(Player p) {
		p.getGame().print("Une bouteille d'alcool a t ajout  l'inventaire, sa valeur est : "+this.getAlcool());
		p.addToLoot(this);
		this.preneur = p;
		notifyDeletableObserver();
	}

	//Accesseur


	public int getAlcool() {
		return alcool;
	}



}
