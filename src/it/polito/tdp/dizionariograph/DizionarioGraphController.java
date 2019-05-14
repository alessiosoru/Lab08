package it.polito.tdp.dizionariograph;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.dizionariograph.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DizionarioGraphController {
	
	Model model = new Model();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtNumeroLettere;

    @FXML
    private TextField txtParola;
    
    @FXML
    private TextArea txtResult;

    @FXML
    private Button generaGrafoButton;

    @FXML
    private Button viciniButton;

    @FXML
    private Button gradoMaxButton;

    @FXML
    private Button resetButton;

    @FXML
    void handleGeneraGrafo(ActionEvent event) {
    	// genero grafo
    	int numeroLettere = Integer.parseInt(this.txtNumeroLettere.getText());
    	model.createGraph(numeroLettere);
    	// abilito gli altri campi
    	this.gradoMaxButton.setDisable(false);
    	this.viciniButton.setDisable(false);
    	this.txtParola.setDisable(false);
    	// stampo a video messaggio di generazione
    	this.txtResult.appendText("Grafo generato !\n");

    }

    @FXML
    void handleGradoMax(ActionEvent event) {
    	this.txtResult.appendText(model.findMaxDegree());

    }

    @FXML
    void handleReset(ActionEvent event) {
    	model = new Model();
    	this.txtNumeroLettere.clear();
    	this.txtParola.clear();
    	this.txtResult.clear();
    	// disabilito gli altri campi
    	this.gradoMaxButton.setDisable(true);
    	this.viciniButton.setDisable(true);
    	this.txtParola.setDisable(true	);
    	


    }

    @FXML
    void handleVicini(ActionEvent event) {
    	String parolaRicerca = this.txtParola.getText();
    	List<String> vicini = model.displayNeighbours(parolaRicerca);
    	String listaVicini="";
    	for(String vicino : vicini) {
    		listaVicini = listaVicini +vicino+"\n";
    	}
    	
    	this.txtResult.appendText("Le parole vicine a "+parolaRicerca+" risultano:\n"+listaVicini+"\n");
    }

    @FXML
    void initialize() {
        assert txtNumeroLettere != null : "fx:id=\"txtNumeroLettere\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";
        assert txtParola != null : "fx:id=\"txtParola\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";
        assert generaGrafoButton != null : "fx:id=\"generaGrafoButton\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";
        assert viciniButton != null : "fx:id=\"viciniButton\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";
        assert gradoMaxButton != null : "fx:id=\"gradoMaxButton\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";
        assert resetButton != null : "fx:id=\"resetButton\" was not injected: check your FXML file 'DizionarioGraph.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		
	}
}
