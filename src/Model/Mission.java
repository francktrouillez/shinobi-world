package Model;

public class Mission extends GameObject implements Focusable {
	private int mission;



	public Mission(int x, int y, int mission, Plateau p) {
		super(x,y, "mission", p);
		this.mission = mission-1;
	}


	//permet au joueur de recevoir une certaine mission qui depend du type
	public void activate(Player p) {

		p.getGame().print("Vous avez recu la mission n"+(mission+1)+" !");
		p.setOnMission(true);  //indique au joueur qu il possde une mission
		p.setMission(this.mission); //indique au joueur quelle mission il possede

	}


	//Accesseurs


	public boolean isObstacle() {
		return true;
	}

	public int getMission(){
		return mission;

	}
}
