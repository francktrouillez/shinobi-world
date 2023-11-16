package Model;

import java.util.ArrayList;

public class Mangas {
	
	//liste contenant les personnages propre a une famille
	private static ArrayList<Player> dbz = new ArrayList<Player>();
	private static ArrayList<Player> naruto = new ArrayList<Player>();
	private static ArrayList<Player> pokemon = new ArrayList<Player>();
	
	//valeur des comptes des differentes familles lors de la creation du jeu
	private static int accountDbz = 0;
	private static int accountNaruto = 0;
	private static int accountPokemon = 0;
	
	public static int getAccount(String name) {
		int res = 0;
		switch(name) {
		case "dbz" : res = accountDbz; break;
		case "naruto" : res = accountNaruto; break;
		case "pokemon" : res = accountPokemon; break;
		}
		return res;
	}
	
	//methode qui change la valeur du compte de tous les personnages d une meme famille
	public static void setAccount(int account, String name) {
		if (account <=0) {
			account = 0;
		}
		switch(name) {
		case "dbz" : accountDbz = account; break;
		case "naruto" : accountNaruto = account; break;
		case "pokemon" : accountPokemon = account; break;
		} 
	}
	
	//methode pour ajouter un joueur a une famille
	public static void addPlayer(Player p, String name) {
		switch(name) {
		case "dbz" : dbz.add(p); break;
		case "naruto" : naruto.add(p); break;
		case "pokemon" : pokemon.add(p); break;
		} 
	}
}
