
package Model;

import java.util.ArrayList;

public class Univers {
	private ArrayList<Plateau> plateaux = new ArrayList<Plateau>(); //liste avec tous les plateaux de l univers


	public Univers() { //size est la taille de la matrice de plateau, ex: size =3 --> 3x3


	}


	//la liste de booleen renseigne sur l emplacement des portes aux bord Sud, Est, West ou Nord
	//la liste d entier renseigne quant a elle sur le type de la porte au bord

	public void initializeUnivers() {

		//plateau d acceuil
		boolean[] plateau_MM = {true,true,true,true};
		int[] porteval_MM = {0,2,1,3};

		//plateaux de la vallee desolee
		boolean[] plateau_MG = {false,true,false,true};
		int[] porteval_MG = {0,1,0,1};

		boolean[] plateau_BG = {true,false,false,false};
		int[] porteval_BG = {1,0,0,0};

		//plateaux de la jungle
		boolean[] plateau_HM = {false,true,true,false};
		int[] porteval_HM = {0,0,0,0};

		boolean[] plateau_HG = {false,false,false,true};
		int[] porteval_HG = {0,0,0,0};

		//plateaux terres sombres
		boolean[] plateau_BM = {true,false,false,true};
		int[] porteval_BM = {2,0,0,2};

		boolean[] plateau_BD = {false,false,true,false};
		int[] porteval_BD = {0,0,2,0};

		//plateaux mission1
		boolean[] plateau_MD = {true,false,false,false};
		int[] porteval_MD = {0,0,0,0};

		boolean[] plateau_HD = {true,true,false,false};
		int[] porteval_HD = {4,0,0,0};

		//plateaux mission2
		boolean[] plateau_MDD = {false,false,false,false};
		int[] porteval_MDD = {0,0,0,0};

		boolean[] plateau_HDD = {true,true,false,false};
		int[] porteval_HDD = {4,0,0,0};

		//plateaux mission3
		boolean[] plateau_MDDD = {false,false,false,false};
		int[] porteval_MDDD = {0,0,0,0};

		boolean[] plateau_HDDD = {true,true,false,false};
		int[] porteval_HDDD = {4,0,0,0};


		//zones a decouvrir
		plateaux.add(new Plateau(-1,1,plateau_HG,porteval_HG,1));
		plateaux.add(new Plateau(0,1,plateau_HM,porteval_HM,1));
		plateaux.add(new Plateau(-1,0,plateau_MG,porteval_MG,2));
		plateaux.add(new Plateau(0,0,plateau_MM,porteval_MM,0));
		plateaux.add(new Plateau(-1,-1,plateau_BG,porteval_BG,2));
		plateaux.add(new Plateau(0,-1,plateau_BM,porteval_BM,3));
		plateaux.add(new Plateau(1,-1,plateau_BD,porteval_BD,3));

		//mission1
		plateaux.add(new Plateau(1,0,plateau_MD,porteval_MD,4));
		plateaux.add(new Plateau(1,1,plateau_HD,porteval_HD,4));

		//mission 2
		plateaux.add(new Plateau(2,0,plateau_MDD,porteval_MDD,5));
		plateaux.add(new Plateau(2,1,plateau_HDD,porteval_HDD,5));

		//mission3
		plateaux.add(new Plateau(3,0,plateau_MDDD,porteval_MDDD,6));
		plateaux.add(new Plateau(3,1,plateau_HDDD,porteval_HDDD,6));


		//plateau maison

		boolean[] plateau_MGG = {false,false,false,true};
		int[] porteval_MGG = {0,0,0,0};

		Plateau home = new Plateau(-2,0,plateau_MGG, porteval_MGG, 7,20);
		plateaux.add(home);
	}

	//methode pour ajouter un plateau a l univers
	public void addPlateau(Plateau p) {
		plateaux.add(p);
	}


	//Accesseurs


	public int[] getPos(Plateau p) {
		return plateaux.get(plateaux.indexOf(p)).getPos();  //pos (x,y) du plateau
	}

	public Plateau getPlateau(int x, int y) { //plateau qui se trouve  la pos(x,y)
		Plateau res = null;
		for (Plateau p: plateaux) {
			if (x == p.getPos()[0] && y == p.getPos()[1]) {
				res = p;
				break;
			}
		}
		return res;
	}


	public ArrayList<Plateau> getAllPlateaux(){
		return plateaux;
	}

}
