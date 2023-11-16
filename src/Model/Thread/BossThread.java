package Model.Thread;

import java.util.ArrayList;
import java.util.Random;

import Model.BlockBreakable;
import Model.Boss;
import Model.Directable;
import Model.Game;
import Model.GameObject;
import Model.Mechant;
import Model.Player;
import View.Sound;

public class BossThread implements Runnable{

	private Boss b;

	private Random rand = new Random();

	private int coolDown = 0;

	public BossThread(Boss b) {
		this.b=b;

	}

	//tant que le boss est vivant, il se deplace vers le joueur et l attaque quand il lui fait face
	//s il est mort, il faut faire apparaitre la porte vers la partie recompense de la zone mission
	//il faut egalement supprimer les objets restants pour la reinitialisation de la mission quand le joueur aura quitte la zone
	public void run() {

		double difficulty = Boss.getDifficulty();
		int randomWait;
		int waitingTime;


		while (b.isAlive() && !b.getGame().isNoPlayer(b.getPlateau())) {
			try {

				randomWait = rand.nextInt(3)+1; //temps d attente entre les mouvements du boss
				waitingTime = (int)((400 + 200*randomWait)/difficulty);
				Thread.sleep(waitingTime);
				coolDown++;
				if (coolDown%10!= 0) {
					synchronized(Game.keyPlateau) {

						//on cherche a trouver la direction dans laquelle le bsoss doit aller pour minimiser la distance avec le joueur actif

						Game g = b.getGame();

						if (g.getActivePlateau() == b.getPlateau()) {

							int posXPlayer = g.getActivePlayer().getPosX();
							int posYPlayer = g.getActivePlayer().getPosY();

							int deltaX = posXPlayer - b.getPosX();
							int deltaY = posYPlayer - b.getPosY();
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

							b.setDirection(direction);

							//s il n y a rien en face, le boss continue d avancer
							//si le joueur fait face au joueur, il le frappe
							//si le joueur fait face a un bloc cassable qui lui obstrue le chemin, il le frappe
							//si le joueur fait face a un obstacle incassable, il change dans une direction perpendicualaire aleatoire

							ArrayList<GameObject> go = new ArrayList<GameObject>();
							if (direction == Directable.NORTH || direction == Directable.SOUTH) {
								go = b.getPlateau().getAllGameObjectsAt(b.getPosX(), b.getFrontY()) ;
							}
							else {
								go = b.getPlateau().getAllGameObjectsAt(b.getFrontX(), b.getPosY()) ;
							}

							boolean obstacle = false;
							for (GameObject gameobject : go) {
								if (gameobject instanceof Player) {
									obstacle = true;
									if (g.isActivePlayer((Player)gameobject)){
										b.hit((Player)gameobject);
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
								case Directable.NORTH : b.move(0, -1); break;
								case Directable.SOUTH : b.move(0, 1); break;
								case Directable.EAST : b.move(1, 0); break;
								case Directable.WEST : b.move(-1, 0); break;
								}

						}

						}
					}
				}

				else {
					coolDown = 0;
					Sound.playEffect("jutsu");
					Thread.sleep(1550);
					try {
						b.summon(rand.nextInt(4)+1); //le boss s arrete de bouger un instant et invoque des monstres autour de lui
					}
					catch(Exception e) {
						System.out.println("problme avec l apparition de monstres");
					}
				}

			}
			catch(Exception e) {System.out.println("problme avec le deplacement du boss");

			}
		}

		synchronized(Game.keyPlateau) {
			for (GameObject go:b.getPlateau().getObjects()) {
				if (go instanceof Player) {
					((Player) go).gainXP(b.getXPValue()); //si le boss est mort, on donne de l experience au joueur
				}
			}

				b.getPlateau().addDoor(Directable.NORTH); //on fait egalement apparaitre la porte pour passer a la zone de recompense du plateau mission

		}


		boolean cond = true;
		boolean cond1;
		while (cond) {
			cond1 = false;
			GameObject gameobject = null;
			synchronized(Game.keyPlateau) {
				for (GameObject go : b.getPlateau().getObjects()) {
					if (go instanceof Mechant) {
						gameobject = go;
						cond1 = true;
						break;
					}
				}
				if (cond1) {
					((Mechant)gameobject).setLife(0);

				}
				else {
					cond = false;
				}
			}



		}
		try {
			//si le boss est mort, on l enleve des objets du plateau et on augmente sa difficulte pour la prochaine fois ou le joueur fait la mission 3
			synchronized(Game.keyPlateau) {
				if (!b.isAlive()) {
					Boss.increaseDifficulty();
				}
				b.destroy();
			}

		}
		catch(Exception e) {

		}
	}

}
