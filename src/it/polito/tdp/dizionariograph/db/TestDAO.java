package it.polito.tdp.dizionariograph.db;

import java.util.LinkedList;
import java.util.List;

public class TestDAO {
	
	public static void main(String[] args) {
		
		WordDAO wd = new WordDAO();
		
//		System.out.println(wd.getAllWordsFixedLength(4));
		
//		String parola = "parola";
		String parola = "arco";
		List<String> paroleVicine = new LinkedList<>();
		paroleVicine = wd.parolaVicina(parola);
		for(String p: paroleVicine)
			System.out.println(p);
	}

}
