package Model.Thread;

import Model.Hit;

public class HitThread implements Runnable{

	private Hit h;
	
	public HitThread(Hit h) {
		this.h = h;
	}

	//methode qui a pour but de faire apparaitre de maniere croissante l image d un hit avant de le detruire apres un certain temps
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 10; i <= 30; i++) {
			h.setSize((double)(i)/10);
			try {
				Thread.sleep(2);
			} catch(Exception e) {}
			
		}
		try {
			Thread.sleep(150);
		} catch(Exception e) {}
		h.destroy();
	}
	
}
