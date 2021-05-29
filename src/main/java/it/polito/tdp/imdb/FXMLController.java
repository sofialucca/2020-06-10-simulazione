/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	txtResult.clear();
    	Actor a = this.boxAttore.getValue();
    	if(a == null) {
    		txtResult.appendText("ERRORE: selezionare un attore\n");
    		return;
    	}
    	List<Actor> result = model.getAttoriRaggiungibili(a);
    	if(result.size() == 0) {
    		txtResult.appendText("Non ci sono attori simili a: ");
    		txtResult.appendText(a.toString());
    	}else {
    		txtResult.appendText("ATTORI SIMILI A: " + a);
    		for(Actor ac : result) {
    			txtResult.appendText("\n" + ac.toString());
    		}
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	String genere = this.boxGenere.getValue();
    	if(genere == null) {
    		txtResult.appendText("ERRORE: scegliere un genere");
    		return;
    	}
    	model.creaGrafo(genere);
    	List<Actor> attori = model.getActors();
    	
    	txtResult.appendText("Grafo creato:\n");
    	txtResult.appendText("# vertici: " + attori.size());
    	txtResult.appendText("\n# archi: " +model.getEdgeSize());
    	
    	this.boxAttore.getItems().setAll(attori);
    	this.boxAttore.setDisable(false);
    	this.btnSimili.setDisable(false);
    	this.txtGiorni.setDisable(false);
    	this.btnSimulazione.setDisable(false);
    }

    @FXML
    void doSimulazione(ActionEvent event) {

    	txtResult.clear();
    	
    	if(!isValid()) {
    		return;
    	}
    	String nGiorni = txtGiorni.getText();
    	model.setSim(Integer.parseInt(nGiorni));
    	txtResult.appendText("PER LA PRODUZIONE DI UN FILM " + this.boxGenere.getValue() + " SONO STATI INTERVISTATI IN " + nGiorni + " GIORNI");
    	txtResult.appendText("\n\nTOTALI PAUSE: " + model.getPause() +"\n");
    	for(Actor a: model.getIntervistati()) {
    		txtResult.appendText("\n" + a.toString());
    	}
    	
    }

    private boolean isValid() {
		String input = this.txtGiorni.getText();
		if(input.equals("")) {
			txtResult.appendText("ERRORE: inserire un numero di giorni");
			return false;
		}
		try {
			Integer.parseInt(input);
		}catch(NumberFormatException nfe) {
			txtResult.appendText("ERRORE: insrire un numero intero.");
			return false;
		}
		return true;
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxGenere.getItems().setAll(model.getGeneri());
    }
}
