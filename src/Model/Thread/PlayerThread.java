package Model.Thread;

import Model.Apprenti;
import Model.Confirme;
import Model.Hokage;
import Model.Player;

public class PlayerThread implements Runnable{
	private Player p;
	
	//diminution des statistiques
	private final static int lifeR = 1;
	private final static int damageR = 1;
	private final static int energyR = 10;
	private final static int faimR = 1;
	private final static int soifR = 1;
	private final static int vessieR = 1;
	private final static int bobR = 8;
	
	//facteur de diminution propre au grade
	private int negative = 1;
	
	private int compteurLife = 0;
	
	public PlayerThread(Player p) {
		this.p = p;
		
	}

	
	//methode qui a pour but de faire diminuer les statistiques apres chaque intervalle de temps
	public void run() {
			while (true) {			
				try {
					compteurLife ++;
					
					//choix du facteur de diminution en fonction du joueur
					//plus le grade est eleve, moins ses besoins descendront vite
					if (p instanceof Apprenti) {
						negative = 3;
					} else if (p instanceof Confirme) {
						negative = 2;
					} else if (p instanceof Hokage) {
						negative = 1;
					}
					Thread.sleep(100);
					
					//statistiques et besoins du joueur
					int life = (int)p.getLife();
					int energy = p.getRealEnergy();
					int faim = p.getFaim();
					int soif = p.getSoif();
					int vessie = p.getVessie();
					int alcool = p.getAlcool();
					this.p.setEnergy(energy);
					
					if(!p.getInvincibility()) { //dans le cas d une potion, le joueur est invincible et on bloque ses besoins
						p.setVessie(vessie + (vessieR*negative));
						p.setFaim(faim + (faimR*negative));
						p.setSoif(soif + (soifR*negative));
					}
					
					p.setAlcool(alcool - (bobR));
					
					//le joueur ne peut recuperer de l energie que s il n a pas besoin de boire ou manger
					if (p.getSoif() < Player.MAX_SOIF && p.getFaim() < Player.MAX_FAIM) {
						p.setEnergy(energy + (energyR));
					}
					
					
					//la vie ne peut diminuer avec le thread que si un des trois besoins est devenu urgent
					if (p.getFaim() == Player.MAX_FAIM || p.getSoif() == Player.MAX_SOIF || p.getVessie() == Player.MAX_VESSIE ) {
						if (compteurLife%5 == 0) {
							compteurLife = 0;
							p.setLife((int)p.getLife() - (damageR));
						}
						
					}
					
					//le joueur ne peut recuperer de la vie que si on energie est maximum
					else if (p.getRealEnergy() == Player.MAX_ENERGY ) {
						p.setLife(life + (lifeR));
						
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
	}

}
