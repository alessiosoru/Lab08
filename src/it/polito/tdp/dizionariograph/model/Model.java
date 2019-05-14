package it.polito.tdp.dizionariograph.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.dizionariograph.db.WordDAO;

public class Model {
	
	private Graph<String, DefaultEdge> grafo;
	private List<String> parole;
	private WordDAO dao;
	private Map<String, String> paroleIdMap;
	private Map<String, String> visita;
	int numeroLettere;

	public Model() {		
		dao = new WordDAO();
		numeroLettere=-1;
	}

	public void createGraph(int numeroLettere) {
		this.numeroLettere=numeroLettere;
		// creo il grafo
		this.grafo = new SimpleGraph(DefaultEdge.class);
		
		// aggiungo i vertici
		this.parole = dao.getAllWordsFixedLength(numeroLettere);
		Graphs.addAllVertices(this.grafo, this.parole);
		System.out.println(this.grafo.vertexSet().toString());
		// creo una idMap
		this.paroleIdMap = new HashMap<>();
//		for(String s : parole)
//			paroleIdMap.put(s, s);
		
		// aggiungo gli archi (metodo di aggiunta 2)
		for(String source : this.grafo.vertexSet()) {
			
			//  chiedendo direttamente al DB
////			long start = System.nanoTime();
//			List<String> targets = dao.parolaVicina(source);
////			long stop = System.nanoTime();
			
			// stesso precedente, con metodo Java
//			long start2 = System.nanoTime();
//			List<String> targets = paroleVicine(source); // troppo lento
			List<String> targets = paroleSimili(source); // verifico se le parole sono vicine dal numero di differenze
//			long stop2 = System.nanoTime();
			
//			System.out.format("Tempo trascorso con DB: %f ms\n", (stop-start)/1e6);
//			System.out.format("Tempo trascorso con metodo Java: %f ms\n", (stop2-start2)/1e6);
			
			
			for(String target : targets)
				this.grafo.addEdge(source, target);
		}
	}

	public List<String> paroleSimili(String source) {
		
//		char[] sourceChars = source.toCharArray();
		System.out.println("Cerco simili di "+source);
		List<String> paroleVicine = new LinkedList<>();
		Map<String, String> paroleVicineMap = new HashMap<>();
		
		for(String parola : this.parole) {
		
//			char[] parolaChars = parola.toCharArray();
			
			if(parola.length()==source.length() && !parola.equals(source)) {
			
				int differenze = 0;
				
				for(int i=0; i<source.length();i++) {
					if(source.charAt(i) != parola.charAt(i))
						differenze++;		
				}
				
				if(differenze==1 && !paroleVicineMap.containsKey(parola)) {
					paroleVicine.add(parola);
					paroleVicineMap.put(parola, parola);
					System.out.println(parola);
				}
			}
		}
		
		return paroleVicine;
	}

	public List<String> paroleVicine(String source) {
		
		List<String> paroleVicine = new LinkedList<>();
		for(String parolaConfrontoIniz: this.parole) {
			for(int posizione = 0; posizione<source.length(); posizione++) {
				String parolaLike;
				char[] parolaLikeChars = source.toCharArray();
				parolaLikeChars[posizione] = '_';
				parolaLike = String.valueOf(parolaLikeChars);
				
				String parolaConfronto = parolaConfrontoIniz;
				char[] parolaConfrontoChars = source.toCharArray();
				parolaConfrontoChars[posizione] = '_';
				parolaConfronto = String.valueOf(parolaConfrontoChars);
//				System.out.println(parolaConfronto);
				
				// verifico di non aggiungere la parola stessa,
				// che non sia già stata trovata
				// e che le due parole, con '_' al posto della lettera
				// in entrambe le posizioni,	siano uguali
				// nel caso aggiungo
				
				if(parolaLike.compareTo(parolaConfronto)==0 &&
						parolaConfrontoIniz.compareTo(source)!=0 &&
						!paroleVicine.contains(parolaConfrontoIniz)) {
					
						paroleVicine.add(parolaConfrontoIniz);
						
				}
			}
			
		}
		return paroleVicine;
	}

	public List<String> displayNeighbours(String parolaInserita) {
		if(parolaInserita.length()!=this.numeroLettere) {
			List<String> error = new LinkedList();
			error.add("Parola inserita con lunghezza differente, reinserire");
			return error;
		}
//			throw new RuntimeException("Parola inserita con lunghezza differente, reinserire");
		
		List<String> vicini = new ArrayList<>();
		
		vicini.addAll(Graphs.neighborListOf(this.grafo, parolaInserita));
		
//		for(String parola:this.parole) {
//			if(this.grafo.containsEdge(parola, parolaInserita) ||
//					this.grafo.containsEdge(parolaInserita, parola)) {
//				vicini.add(parola);
//			}
//		}
//		GraphIterator<String, DefaultEdge> it = new BreadthFirstIterator(this.grafo, parolaInserita);
//		visita.put(parolaInserita, parolaInserita);
//		it.addTraversalListener(new TraversalListener<String, DefaultEdge>(){
//
//			@Override
//			public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> ev) {
//				String sorgente = grafo.getEdgeSource(ev.getEdge());
//				String target = grafo.getEdgeTarget(ev.getEdge());
//				
//				if(!visita.containsKey(target)&&visita.containsKey(sorgente))
//					visita.put(target, sorgente);
//				else if(!visita.containsKey(sorgente)&&visita.containsKey(target))
//					visita.put(sorgente, target);
//				
//				
//				
//			}
//
//			@Override
//			public void vertexFinished(VertexTraversalEvent<String> arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void vertexTraversed(VertexTraversalEvent<String> arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
//		
//		while(it.hasNext()) {
//			vicini.add(it.next());
//		}
		
		
		return vicini;
	}

	public String findMaxDegree() {
		int gradoMax=-1;
		String verticeGradoMax=null;
		List<String> viciniVerticeGradoMax = new LinkedList<String>();
		for(String parola:this.grafo.vertexSet()){
			int grado = this.grafo.degreeOf(parola);
			if(grado>gradoMax) {
				gradoMax=grado;
				verticeGradoMax=parola;
				viciniVerticeGradoMax=this.displayNeighbours(parola);
			}
		}
		if(gradoMax!=-1) {
			String listaVicini="";
			for(String vicino:viciniVerticeGradoMax)
				listaVicini=listaVicini+vicino+"\n";
				
			return "Grado massimo: "+gradoMax+". Corrisponde al vertice - "+verticeGradoMax.toString()+
					" - con i seguenti vicini:\n"+listaVicini;
		}
		else
			return "Non si trova il vertice con grado max";
	}
}
