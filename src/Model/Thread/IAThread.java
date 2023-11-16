package Model.Thread;

import java.util.ArrayList;
import java.util.Random;

import Model.ArmurerieDoor;
import Model.Game;
import Model.GameObject;
import Model.Plateau;
import Model.Player;
import Model.Toilet;

public class IAThread implements Runnable{

	private Player p;
	private Game g;
	private Random rand = new Random();

	private int time;

	private GameObject go;


	public IAThread(Player p, Game g) {
		this.p = p;
		this.g = g;


	}

	//methode permet aux bots de se deplacer de maniere aleatoire et de bouger vers un endroit lorsqu un de ses besoins est trop urgent
	public void run() {
			//le joueur actif ne possede pas le comportement d un bot
			while (!g.isActivePlayer(p) && g.getPlayers().indexOf(p) != -1) {


				try {
					//permet d indiquer au bot s il doit se deplacer et pour quel(s) besoin(s)
					p.setNeedToDrink();
					p.setNeedToEat();
					p.setNeedToPee();
					p.setNeedToGo();

					//s il ne doit pas se deplacer, alors il a un comportement aleatoire
					if(!p.getNeedToGo()) {

						time = rand.nextInt(2)+2;
						Thread.sleep(time*200);

						p.RandomMove();


					}
					//s il doit se deplacer, en fonction du besoin il va soit vers l armurerie pour acheter a boire ou a manger soit vers la toilette
					else if(p.getNeedToGo()) {
						Thread.sleep(500);

						//le joueur, dans le cas ou ses trois besoins sont urgents, se deplacera toujours en premier vers l armurerie
						if(p.getNeedToDrink() || p.getNeedToEat()) {
							goToPosAstar("armurerie");
						}
						else if(p.getNeedToPee()) {

							goToPosAstar("toilet");
						}

					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

	}

	//methode qui utilise la classe astarThread pour se deplacer jusqu a l endroit et une fois arrive, d activer l objet selon le besoin du bot
	public void goToPosAstar(String s) {

		Plateau plateau = g.getUnivers().getPlateau(0, 0);

		if(!p.getOnMove()) {

			if(s == "toilet") {
				for(GameObject go: plateau.getObjects()) {
					if(go instanceof Toilet) {

						this.go = go;
						break;

					}

				}
			}
			else if(s == "armurerie") {
				for(GameObject go: plateau.getObjects()) {
					if(go instanceof ArmurerieDoor) {

						this.go = go;
						break;
					}

				}
			}

			Thread t = new Thread(new AStarThread(p.getGame(),p,go));
			t.start();
			g.print(p.getName().toUpperCase().charAt(0)+p.getName().substring(1) +" se dplace pour subvenir  ses besoins");
			p.setOnMove(true); //le joueur est maintenant en deplacement non aleatoire vers l objet


		}

		//si le joueur se trouve a la position de l objet, alors il l active
		if(p.getPosX() == this.go.getPosX() && p.getPosY() == this.go.getPosY()) {

			ArrayList<GameObject> listgo = p.getPlateau().getAllGameObjectsAt(p.getPosX(), p.getPosY()) ;
			for(GameObject go: listgo) {
				if(go instanceof Toilet) {
					((Toilet)go).activate(p);
					g.print(p.getName().toUpperCase().charAt(0)+p.getName().substring(1)+" a t au toilette");
				}

				//tant que sa faim ou sa soif n est pas descendu en dessous d une certaine valeur critique, il achete, place dans son inventaire et consomme
				if(go instanceof ArmurerieDoor) {
					if(p.getNeedToDrink()) {

						while(p.getSoif() > Player.MAX_SOIF/5 && !g.isActivePlayer(p)) {
							((ArmurerieDoor)go).activate(p);
							p.drink();
						}

					}
					if(p.getNeedToEat()) {
						while(p.getFaim() > Player.MAX_FAIM/5 && !g.isActivePlayer(p)) {
							((ArmurerieDoor)go).activate(p);
							p.eat();

						}

					}
				}
			}
			p.setOnMove(false); //ses besoins ont ete satsifait, il ne doit plus se deplacer vers un endroit precis


		}
	}

}





