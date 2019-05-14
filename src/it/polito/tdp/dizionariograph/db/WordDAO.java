package it.polito.tdp.dizionariograph.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.dizionariograph.model.Adiacenza;

public class WordDAO {

	/*
	 * Ritorna tutte le parole di una data lunghezza
	 */
	public List<String> getAllWordsFixedLength(int length) {

		String sql = "SELECT nome FROM parola WHERE LENGTH(nome) = ?;";
		List<String> parole = new ArrayList<String>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, length);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				parole.add(res.getString("nome"));
			}
//			st.close();
//			conn.close();
			return parole;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<String> parolaVicina(String source) {
		List<String> paroleVicine = new LinkedList<>();
		char[] parolaLikeChars = source.toCharArray();
		String sql = "SELECT nome " + 
				"FROM parola " + 
				"WHERE nome LIKE ? "
				+ "AND LENGTH(nome) = ? ";

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			for(int posizione = 0; posizione<source.length(); posizione++) {
				String parolaLike;
				
				parolaLikeChars[posizione] = '_';
				parolaLike = String.valueOf(parolaLikeChars);
//				System.out.println(parolaLike);
				
				st.setString(1, parolaLike);
				st.setInt(2, source.length());

				ResultSet res = st.executeQuery();

				while (res.next()) {
					// verifico di non aggiungere la parola stessa e che non sia già stata trovata
					String parola = res.getString("nome");
					if(parola.compareTo(source)!=0 && !paroleVicine.contains(parola))
						paroleVicine.add(parola);
				}

			}
			st.close();
			conn.close();
			return paroleVicine;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	// non si può fare con le adiacenze
//	public List<Adiacenza> listAdiacenze(int length){
//
//		List<String> adiacenze = new LinkedList<>();
//		String sql = "SELECT nome " + 
//				"FROM parola p1, parola p2" + 
//				"WHERE LENGTH(p1.nome) = ? "
//				+ "AND LENGTH(p2.nome) = ? AND p2.nome LIKE ? ";
//
//		try {
//			Connection conn = ConnectDB.getConnection();
//			PreparedStatement st = conn.prepareStatement(sql);
//			st.setInt(1, length);
//			for(int posizione = 0; posizione<source.length(); posizione++) {
//				String parolaLike;
//				char[] parolaLikeChars = source.toCharArray();
//				parolaLikeChars[posizione] = '_';
//				parolaLike = String.valueOf(parolaLikeChars);
////				System.out.println(parolaLike);
//				
//				st.setString(2, parolaLike);
//				
//
//				ResultSet res = st.executeQuery();
//
//				while (res.next()) {
//					// verifico di non aggiungere la parola stessa e che non sia già stata trovata
//					String parola = res.getString("nome");
//					if(parola.compareTo(source)!=0 && !paroleVicine.contains(parola))
//						paroleVicine.add(parola);
//				}
//
//			}
//			
//			return paroleVicine;
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new RuntimeException("Error Connection Database");
//		}
//	
//	}
//
}
