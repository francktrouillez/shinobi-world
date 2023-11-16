package View.Thread;
import java.util.*;

public class AuraThread implements Runnable{

	private ArrayList<Integer> intList = new ArrayList<Integer>();
	
	public AuraThread() {
		Random rand = new Random();
		
		for (int i = 0; i< 2; i++) {
			int nb = rand.nextInt(4); //la premiere image affichee est choisie au hasard
			intList.add(nb);
		}
	}
	
	//methode a pour but de faire defiler les images du sprite
	public void run() {
		
		while (true) {
			try {	
				Thread.sleep(100);
				for (int i = 0; i <intList.size(); i++) {
					intList.set(i, (intList.get(i)+1)%4);
				}
			}
			catch(Exception e) {
				
			}
			
		}
		
	}
	
	//en fonction de l evolution du personnage, l aura affichee est differente
	public int getNumberOf(String name) {
		int res = -1;
		if (name.equals("aura1")) {
			res = intList.get(0);
		}
		else if (name.equals("aura2")) {
			res = intList.get(1);
		}
		return res;
		
	}
	

}
