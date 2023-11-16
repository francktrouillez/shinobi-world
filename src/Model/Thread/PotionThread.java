package Model.Thread;


import Model.Player;
//import Model.InvincibleZone;
import Model.Potion;

public class PotionThread implements Runnable {
	private int duree;
	private Player p;

	
	public PotionThread(Potion pot,Player p) {
		this.duree = pot.getDuree();
		this.p = p;

		p.setInvincibility(true);
	}

	//methode qui rend le joueur invincible pendant un certain temps
	public void run() {
		for(int i = 0; i<duree;i++) {
			try {
				Thread.sleep(1000);

			}
			catch(Exception e) {
				
			}
		}

		this.p.setInvincibility(false);
		
		
	}
}
