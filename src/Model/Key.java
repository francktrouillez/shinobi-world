package Model;

import java.util.ArrayList;

import View.Sound;

public class Key extends GameObject implements Deletable,Focusable {
	
	private ArrayList<DeletableObserver> observers = new ArrayList<DeletableObserver>();
	
	private Player preneur;
	
	public Key(int x, int y, Plateau p) {
		super(x,y,"key",p);
		System.out.println("key");
	}

	

	//methode qui permet, lorsque le joueur ramasse la cle, de lui dire qu il a totalement fini la mission et le rend maintenant capable de sortir de la zone de mission
	public void activate(Player p) {
		this.preneur = p;
		p.setMissionEnd(true);
		notifyDeletableObserver();
		Sound.stopMusic();
		Sound.playEffect("congrats");
		try {
			Thread.sleep(4000);
		}catch(Exception e) {}
		
		Sound.playBackground(getPath());
		
	}
	
	public void attachDeletable(DeletableObserver po) {
		observers.add(po);
	}
	public void notifyDeletableObserver() {
		synchronized(Game.keyPlateau) {
        for (DeletableObserver o : observers) {
            o.delete(this, null, preneur);
        }
		}
	}
	
	private String getPath() {
		int zone = getPlateau().getZone();
		String res = null;
		if(zone == 4) {
			res = "battle1";
		}
		else if(zone == 5) {
			res = "battle2";
		}
		else if(zone == 6) {
			res = "battle3";
		}
		return res;
	}

	
	//Accesseurs
	
	
	public boolean isObstacle() {
		// TODO Auto-generated method stub
		return false;
	}
}

