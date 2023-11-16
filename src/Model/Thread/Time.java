package Model.Thread;

import Model.Game;

public class Time implements Runnable {
	private double compteur = 0;
	private int waitingTime;
	private double incrementTime;

	private Game g;

	/*
	 * Objet Time qui va rafraichir la fenetre selon les FPS demandes
	 * Il possede egalement un compteur pour etre utilise comme horloge
	 */

	public Time(Game g) {
		this.g = g;
		g.setTime(this);
		waitingTime = 1000/Game.FPS;
		incrementTime = (double)waitingTime/1000;
	}

	//le run update le panel de jeu a chaque intervalle de temps
	public void run() {
		while (true){
			try {
				Thread.sleep(waitingTime);
				compteur += incrementTime;
				if (g.getWindow().getNameActivePanel().equals("groupPanel")) {
					g.updateAll();
				}
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	//Accesseurs


	public double getCompteur() {
		return compteur;
	}

	public void setCompteur(double c) {
		compteur = c;
	}


}
