package Model;

import View.Sound;

public class Apprenti extends Player{

	private int EVOLUTION_XP = 2500;
	
	//constructeur pour un nouveau jeu
	public Apprenti(String name, String manga,int x, int y, Plateau p) {
		super(name, manga,x,y,"apprenti",p);
	}
	
	//constructeur a partir d une sauvegarde
	public Apprenti(String name, String manga, int[] info, Plateau p) {
		super(name,manga,info, p,"apprenti");
		
	}
    
	//creation d un nouveau confirme en recuperant les statistiques et objets a l instant de l evolution
	public Confirme evolve() {
		 Confirme c = new Confirme(this.name, this.manga,getStats(), getLoot(), this.getPlateau());
		 c.setTime(this.getHorloge());
		 c.pureXP = -(int)this.getHorloge().getCompteur();
		 Sound.playEffect("evolve");
		 return c;
	}
	
	//le joueur doit evaluer lorsque le total temps de vie (fait tres peu augmenter) et experience gagnee au combat ou en mission est superieur au l experience totale necessaire
	public boolean isTimeToEvolve() {
		return (getXP() - this.getTimeNaissance()) > EVOLUTION_XP;
		
	}
	
	//experience
	
	public int getEvolutionXP() {
		return EVOLUTION_XP;
	}
	
	public void gainXP(int xp) {
		this.pureXP += xp;
	}
	
	public int getPureXP() {
		return pureXP;
	}
	
	public int getXP() {
		return (int)this.getHorloge().getCompteur() + pureXP;
	}
	

}
