package Model;

import java.io.*;
public class Fichier {



	//methode qui permet de creer un nouvel emplacement fichier texte avec un certain path et
	//d ecrire a l interieur un contenu texte

	public void write(String path, String texte) {
		try {
		FileWriter fw = new FileWriter(path);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(texte);
		bw.close();
		}
		catch(IOException e) {
			System.out.println("Erreur dans la cration du fichier");
		}

	}


	//methode qui permet de rajouter du texte a un fichier en ecrasant l ancien contenu et en reecrivant le contenu modifie

	public void add(String path, String texte) {
		String contenu = read(path);

		contenu += texte;
		write(path, contenu);
	}



	//methode qui recherche le fichier texte correspondant au path et
	//si elle le trouve, de renvoyer le texte contenu a l interieur

	public String read(String path) {
		File fichier = new File(path);
	    int car;
	    StringBuffer contenu = new StringBuffer("");
	    FileInputStream ftemp = null;
	    try {
	      ftemp = new FileInputStream(fichier);
	      while( (car = ftemp.read()) != -1)
	        contenu.append((char)car);
	      ftemp.close();
	    }

	    catch(FileNotFoundException e) {
	      System.out.println("Fichier introuvable");
	    }

	    catch(IOException ioe) {
	      System.out.println("Exception " + ioe);
	    }

	    String res = contenu.toString();
	    return res;
	  }
	}

