package Model.Thread;

import java.util.ArrayList;
import java.util.Random;

import Model.BlockBreakable;
import Model.Directable;
import Model.Game;
import Model.GameObject;
import Model.Mechant;
import Model.Plateau;
import Model.Player;

public class MechantCacThread implements Runnable{

	private Mechant m;
	
	private Random rand = new Random();
	
	public MechantCacThread(Mechant m) {
		this.m = m;
		
	}
	
	
	//tant qu un mechant est vivant, il se deplace vers le joueur et l attaque quand il lui fait face

	public void run() {

		//on verifie que le joueur se trouve sur le meme plateau que le mechant et que le mechant est en vie
		
		while (m.isAlive() && !m.getGame().isNoPlayer(m.getPlateau())) {

			try {
			
				int randomWait = rand.nextInt(3)+1;
				Thread.sleep(400 + 200*randomWait);
				synchronized(Game.keyPlateau) {
				
				
				Game g = m.getGame();
				
				//on cherche a calculer la direction pour se deplacer vers le joueur de maniere la plus rapide possible
				
				if (g.getActivePlateau() == m.getPlateau()) {
					
					int posXPlayer = g.getActivePlayer().getPosX();
					int posYPlayer = g.getActivePlayer().getPosY();
					
					int deltaX = posXPlayer - m.getPosX();
					int deltaY = posYPlayer - m.getPosY();
					int direction;
					
					if (Math.abs(deltaX) > Math.abs(deltaY)) {
						if (deltaX >0) {
							direction = Directable.EAST;
						}
						else {
							direction = Directable.WEST;
						}
					}
					else if (Math.abs(deltaY) > Math.abs(deltaX)) {
						if (deltaY >0) {
							direction = Directable.SOUTH;
						}
						else {
							direction = Directable.NORTH;
						}
					}
					
					else {
						int hasard = rand.nextInt(2);
						if (hasard == 0) {
							if (deltaX >0) {
								direction = Directable.EAST;
							}
							else {
								direction = Directable.WEST;
							}
						}
						else {
							if (deltaY >0) {
								direction = Directable.SOUTH;
							}
							else {
								direction = Directable.NORTH;
							}
						}
					}
					
					m.setDirection(direction);
					
					//si un objet se trouve devant le mechant, il le frappe si c est le joueur ou un bloc cassable lui bloquant le passage
					//sinon il continue d avancer
					
					ArrayList<GameObject> go = new ArrayList<GameObject>();
					if (direction == Directable.NORTH || direction == Directable.SOUTH) {
						go = m.getPlateau().getAllGameObjectsAt(m.getPosX(), m.getFrontY()) ;
					}
					else {
						go = m.getPlateau().getAllGameObjectsAt(m.getFrontX(), m.getPosY()) ;
					}
					
					boolean obstacle = false;
					for (GameObject gameobject : go) {
						if (gameobject instanceof Player) {
							obstacle = true;
							if (g.isActivePlayer((Player)gameobject)){
								m.hit((Player)gameobject);
							}
							break;
						}
						else if (gameobject.isObstacle()) {

							obstacle = true;
							if (gameobject instanceof BlockBreakable) {
								((BlockBreakable)gameobject).activate();
								
							}
							break;
						}
					}
					
					if (!obstacle) {
						switch(direction) {
						case Directable.NORTH : m.move(0, -1); break;
						case Directable.SOUTH : m.move(0, 1); break;
						case Directable.EAST : m.move(1, 0); break;
						case Directable.WEST : m.move(-1, 0); break;
						}
					}
					
				
					
				
				}
				
				}
			}
			catch(Exception e) {System.out.println("rip");
			
			}
		}
		synchronized(Game.keyPlateau) {
			for (GameObject go:m.getPlateau().getObjects()) {
				if (go instanceof Player) {
					((Player) go).gainXP(m.getXPValue()); //on donne de l experience au joueur quand le mechant meurt
					
				}
				
			}
			if (isMechantMission() && isLastMechant() && isWithPlayer()) {
				m.getPlateau().addDoor(Directable.NORTH); //si le dernier mechant pour la mission 2 meurt, alors on fait apparaitre la porte pour aller vers la partie recompense de la zone de mission

			}
		}
		try {
			synchronized(Game.keyPlateau) {
				m.destroy(); //on detruit le mechant
			}
		}
		catch(Exception e) {
		}
			
			
	}
	
	
	//methode qui regarde si le mechant fait partie la mission 2	
	private boolean isMechantMission() {
		return m.getPlateau().getPos()[0] == 2 && m.getPlateau().getPos()[1] >=0; 
	}
	
	//methode qui regarde si c est le dernier mechant sur le plateau
	private boolean isLastMechant() {
		Plateau plateau = m.getPlateau();
		boolean res = true;
		for (GameObject go : plateau.getObjects()) {
			if (go instanceof Mechant && go != m) {
				res = false;
				break;
			}
		}
		return res;
	}
	
	//methode qui regarde si le mechant et le joueur sont bien sur le meme plateau
	private boolean isWithPlayer() {
		return m.getPlateau().getObjects().indexOf(m.getGame().getActivePlayer()) != -1;
	}

}
