package Model;

import java.util.ArrayList;

public class Kick {
	
	private Player p;
	private int damage;
	
	
	public Kick(Player p, int damage) {
		this.p = p;
		this.damage = damage;
		doDamage();
	}
	
	//methode qui permet d infliger des degats sur une rangee de 3 blocs perpendiculaire a la direction du joueur
	private void doDamage() {
		int decX = 0;
		int decY = 0;
		int direction = p.getDirection();
		switch(direction) {
		case Directable.NORTH : decY = -1;break;
		case Directable.SOUTH : decY = 1; break;
		case Directable.EAST : decX = 1; break;
		case Directable.WEST : decX = -1; break;
		}
		int posX = p.getPosX()+decX;
		int posY = p.getPosY()+decY;
		
		int[] coord1 = {posX,posY}; //position des 3 blocs qui seront dans la portee d attaque du kick
		int[] coord2 = {posX,posY};
		int[] coord3 = {posX,posY};
		if (decY == 0) {
			coord2[0] = posX;
			coord2[1] = posY+1;
			coord3[0] = posX;
			coord3[1] = posY-1;
			
		}
		else {
			coord2[0] = posX+1;
			coord2[1] = posY;
			coord3[0] = posX-1;
			coord3[1] = posY;
		}
		synchronized(Game.keyPlateau) {
			ArrayList<BlockBreakable> bb = new ArrayList<BlockBreakable>();
			for (GameObject go:p.getPlateau().getObjects()) {
				int x = go.getPosX();
				int y = go.getPosY();
				
				//on regarde si un ou plusieurs objets se trouve dans la portee d attaque, c est a dire si leur position correspond a un des trois blocs
				//et on inflige des degats selon le type de l objet
				if ((x == coord1[0] && y == coord1[1]) || (x == coord2[0] && y == coord2[1])  || (x == coord3[0] && y == coord3[1]) ) {
					if (go instanceof Player) {
						((Player) go).loseLife(damage);
					}
					else if (go instanceof Mechant) {
						((Mechant) go).loseLife(damage);
					} else if (go instanceof Boss) {
						((Boss) go).loseLife(damage);
					}
					else if (go instanceof BlockBreakable) {
						bb.add((BlockBreakable)go);
						
					}
				}
			}
			for (BlockBreakable b:bb) {
				for(int i = 0; i < 3;i++) {
					b.activate();
				}
				
			}
		}
		
		
		new Hit(coord1,direction,p.getPlateau()); //creation de l objet graphique associe au kick

		
			
		
	}
	
	
	//Accesseurs
	
	
	public Player getPlayer() {
		return p;
	}
}
