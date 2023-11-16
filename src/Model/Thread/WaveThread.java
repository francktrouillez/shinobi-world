package Model.Thread;

import Model.Wave;

public class WaveThread implements Runnable{
	private Wave w;
	
	public WaveThread(Wave w) {
		this.w  = w;
	}

	//methode qui fait apparaitre de maniere croissante l image associee a la wave avant de la detruire
	public void run() {
		for (int i = 10; i <= 80; i++) {
			w.setSize((double)(i)/10);
			
			try {
				Thread.sleep(3);
			} catch(Exception e) {}
			
		}
		try {
			Thread.sleep(300);
		} catch(Exception e) {}
		w.destroy();
	}
}
