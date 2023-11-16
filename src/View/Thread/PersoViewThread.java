package View.Thread;
import java.util.*;

public class PersoViewThread implements Runnable{

	private ArrayList<Integer> intList = new ArrayList<Integer>();
	private ArrayList<String> nameList = new ArrayList<String>();
	private ArrayList<Boolean> sprintList = new ArrayList<Boolean>();
	
	public PersoViewThread() {
		Random rand = new Random();
		
		//nom des differents sprites sheet
		nameList.add("wolf");
		nameList.add("goblin");
		nameList.add("skeletonWarrior");
		nameList.add("rattata");
		nameList.add("orochimaru");
		
		nameList.add("goku0");
		nameList.add("goku1");
		nameList.add("goku2");
		nameList.add("vegeta0");
		nameList.add("vegeta1");
		nameList.add("vegeta2");
		
		nameList.add("naruto0");
		nameList.add("naruto1");
		nameList.add("naruto2");
		nameList.add("sasuke0");
		nameList.add("sasuke1");
		nameList.add("sasuke2");
		
		nameList.add("dracaufeu0");
		nameList.add("dracaufeu1");
		nameList.add("dracaufeu2");
		nameList.add("pikachu0");
		nameList.add("pikachu1");
		nameList.add("pikachu2");
		
		// dans une direction donnee du personnage, on choisit au hasard une des 5 images comme image initiale, on choisit une des 5 colomnes de la sprite sheet
		for (int i = 0; i< nameList.size(); i++) {
			int nb = rand.nextInt(4); 
			intList.add(nb);
			sprintList.add(false);
		}
	}
	

	public void run() {
		
		while (true) {
			try {	
				Thread.sleep(125);
				// pour une ligne (direction du personnage) de la sprite sheet, on remplace l image acutelle par la suivante, le modulo 5 provient du fait qu il y a 5 images par direction
				for (int i = 0; i < intList.size(); i++) {
					if (sprintList.get(i)) {
						intList.set(i, (intList.get(i)+1)%5); 
					}
				}
				Thread.sleep(125);
				for (int i = 0; i < intList.size(); i++) {
						intList.set(i, (intList.get(i)+1)%5); 
				}
			}
			catch(Exception e) {
				
			}
			
		}
		
	}
	
	//methode qui retourne le numero de l image dans une direction donnee, c est a dire la colomne de l image
	public int getNumberOf(String name) {
		int index = nameList.indexOf(name);
		return intList.get(index);
	}
	
	public void setSpeed(String name, boolean sprinting) {
		int index = nameList.indexOf(name);
		sprintList.set(index, sprinting);
	}
	

}
