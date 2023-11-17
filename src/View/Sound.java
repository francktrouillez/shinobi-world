package View;

import java.util.ArrayList;

import View.Thread.SoundThread;

public class Sound {

	private static boolean activated = true;
	private static ArrayList<SoundThread> effects = new ArrayList<SoundThread>();
	private static ArrayList<SoundThread> music = new ArrayList<SoundThread>();

	private Sound() {

	}

	//methode pour couper le son
	public static void off() {
		activated = false;
	}

	//methode pour allumer le son
	public static void on() {
		activated = true;
	}

	//methode pour joueur un effet
	public static void playEffect(String path) {

		if (activated) {
			try {
				path = "sounds/"+path+".wav";
				SoundThread a = new SoundThread(path);
				effects.add(a);
				Thread b = new Thread(a);
				b.start();

			} catch(Exception e) {}

		}
	}

	//methode pour joueur une musique de fond
	public static void playBackground(String path) {
		if (activated) {
			try {
				path = "sounds/"+path+".wav";
				stopMusic();
				SoundThread a = new SoundThread(path,true);
				music.add(a);
				Thread b = new Thread(a);
				b.start();
			}

			catch(Exception e) {}
		}

	}

	//methode pour stopper le son
	public static void pause() {
		SoundThread.pause();
	}

	//methode pour resumer le son la ou il s est stoppe
	public static void resume() {
		SoundThread.resume();
	}

	//methode pour totalement arreter une musique et detruire le thread correspondant
	public static void stopMusic() {
		for (SoundThread st : music) {
			st.quit();

		}
		music = new ArrayList<SoundThread>();
	}

	//methode pour totalement arreter un effet et quitter le thread correspondant
	public static void stopEffects() {
		for (SoundThread st : effects) {
			st.quit();

		}
		effects = new ArrayList<SoundThread>();
	}



	//methode pour arreter la musique et les effets
	public static void stopAll() {
		stopMusic();
		stopEffects();
	}

	//methode pour allumer ou eteindre en fonction de l etat precendent
	public static void switchOnOff() {

		activated = !activated;
		if (!activated) {
			stopAll();
		}
		else {
			playBackground("background");
		}
	}

	//Les mthodes suivantes ont pour but de modifier le son pour
		//les sons dej lancs ou non. Cependant, il faut encore ameliorer cette partie-ci
		//car pour l'instant, seule la musique est affecte par ces mthodes, et pas les effets
		//C est pourquoi ces mthodes ne sont pas utilises dans le code

		//methode pour mettre le volume a une certaine valeur
		public static void setVolumeEffect(int value) {
			//Entre 0 et 100
			for (SoundThread st: effects) {
				st.setVolume(value);
			}
		}

		//methode pour mettre le volume a une certaine valeur
		public static void setVolumeMusic(int value) {
			//Entre 0 et 100
			for (SoundThread st: music) {
				st.setVolume(value);
			}
		}


		public static void setGlobalVolume(int volume) {
			//Valeur entre 0 et 100
			SoundThread.setGlobalVolume(volume);
			for (SoundThread st: music) {
				st.refreshVolume();
			}
			for (SoundThread st: effects) {
				st.refreshVolume();
			}
		}

		//methode pour augmenter le volume actuel global
		public static void goUp() {
			SoundThread.setGlobalVolume(SoundThread.getGlobalVolume()+10);
			for (SoundThread st: music) {
				st.refreshVolume();
			}
			for (SoundThread st: effects) {
				st.refreshVolume();
			}
		}

		//methode pour diminuer le volume actuel global
		public static void goDown() {
			SoundThread.setGlobalVolume(SoundThread.getGlobalVolume()-10);
			for (SoundThread st: music) {
				st.refreshVolume();
			}
			for (SoundThread st: effects) {
				st.refreshVolume();
			}
		}


}
