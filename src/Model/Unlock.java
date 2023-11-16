package Model;


//classe qui contient les conditions a l ouverture d une porte en fonction de son type
public class Unlock {
	public boolean getCondition(String unlockfeature, Player p) {
		boolean res = true;
		switch(unlockfeature) {
		case("unlockapprenti"): res = unlock1(p);
		break;
		case("unlockconfirme"): res = unlock2(p);
		break;
		case("unlockhokage"): res = unlock3(p);
		break;
		case("unlockmission"): res = unlockmission(p);
		break;
		case("backToAcceuil"): res = finishedMission(p);
		break;
		case("maison"): res = true;
		break;

		}
		return res;
	}

	//methode qui regarde si le joueur possede la cle pour sortir de la zone de mission
	//pour la mission 2 et 3, lorsque le joueur quitte la zone de mission, on supprime les objets propres a la mission pour pouvoir la reinitialiser apres
	//on enleve les mechants et/ou le boss, s ils sont encore en vie
	//on enleve la porte qui est apparue suite a la reussite du defi

	private boolean finishedMission(Player p) {
		boolean res = p.getMissionEnd();
		if(!res) {
			p.getGame().print("VOUS OSEZ FUIR DEVANT LE DANGER ! retournez au combat");
		}
		else {
			System.out.println(p.getMission());
			p.setMissionEnd(false);
			p.getGame().print("La mission est termine, bien jou !");

			int mission = p.getMission()+1;

			p.setPureXP(p.getXP()+1000*(mission));
			p.setGold(p.getGold()+1000*(mission));

			if (mission == 1) {
			}

			else if (mission == 2) {

				Plateau plateau = p.getGame().getUnivers().getPlateau(2, 0);
				boolean cond = true;
				boolean cond1;
				GameObject gameobject = null;
				while (cond) {
					cond1 = false;

					for (GameObject go : plateau.getObjects()) {
						if (go instanceof Mechant) {
							gameobject = go;
							cond1 = true;
							break;
						}
					}
					if (cond1) {
						plateau.removeObject(gameobject);

					}
					else {
						cond = false;
					}


				}
				for (GameObject go : plateau.getAllGameObjectsAt(plateau.getSize()/2, 0)) {
					if (go instanceof Door) {
						gameobject = go;
						break;
					}
				}
				plateau.removeObject(gameobject);

			}else if (mission == 3) {
				Plateau plateau = p.getGame().getUnivers().getPlateau(3, 0);
				GameObject gameobject = null;
				for (GameObject go : plateau.getObjects()) {
					if (go instanceof Boss) {
						gameobject = go;

						break;
					}
				}
				plateau.removeObject(gameobject);
				for (GameObject go : plateau.getAllGameObjectsAt(plateau.getSize()/2, 0)) {
					if (go instanceof Door) {
						gameobject = go;
						break;
					}
				}
				plateau.removeObject(gameobject);
			}
			p.setMission(-1);
		}
		return res;
	}

	//methodes pour verifier si le joueur possede le grade suffisant pour rentrer dans la zone ou une mission dans le cas de la zone de mission


	//grade accepte: apprenti
	public boolean unlock1(Player p) {
		boolean res = false;
		int lvl = getlvl(p);
		if(lvl >= 1) {
			res = true;
		}

		return res;

	}

	//grade accepte: confirme
	public boolean unlock2(Player p) {
		boolean res = false;
		int lvl = getlvl(p);
		if(lvl >= 2) {
			res = true;
		}
		else {
			p.getGame().print("Vous n'avez pas le niveau suffisant, vous devez au moins tre confirm pour entrer");
		}
		return res;
	}

	//grade accepte: hokage
	public boolean unlock3(Player p) {
		boolean res = false;
		int lvl = getlvl(p);
		if(lvl >= 3) {
			res = true;
		}
		else {
			p.getGame().print("Vous n'avez pas le niveau suffisant, vous devez tre hokage pour entrer");
		}
		return res;
	}


	//verifie si le joueur possede ou non une mission
	public boolean unlockmission(Player p) {
		boolean res = false;
		if(p.haveMission()) {
			p.getGame().print("La mission n"+(p.getMission()+1)+ " peut commencer, prparez vous au combat");
			res = true;
			p.setOnMission(false);
		}
		else {
			p.getGame().print("Pas de mission disponible pour l'instant");
		}

		return res;
	}

	//methode pour obtenir en niveau le grade de l apprenti
	public int getlvl(Player p) {
		int lvl = 0;
		if(p instanceof Apprenti) {
			lvl = 1;
		}
		if(p instanceof Confirme) {
			lvl = 2;
		}
		if(p instanceof Hokage) {
			lvl =3;
		}
		return lvl;
	}
}

