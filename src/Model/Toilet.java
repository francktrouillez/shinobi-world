package Model;

public class Toilet extends GameObject implements Focusable{
	


	public Toilet(int X, int Y, Plateau p) {
		super(X, Y, "toilet", p);
	}

	//methode met a zero la vessie du joueur
	public void activate(Player p) {
		p.setVessie(0);
	}

	public boolean isObstacle() {
		return false;
	}



}
