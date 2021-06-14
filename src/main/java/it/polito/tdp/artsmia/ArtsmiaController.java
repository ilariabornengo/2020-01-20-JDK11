package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	String categoria=this.boxRuolo.getValue();
    	List<Adiacenza> lista=new ArrayList<Adiacenza>(this.model.liste(categoria));
    	for(Adiacenza a:lista)
    	{
    		txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	String idS=this.txtArtista.getText();
    	Integer id=0;
    	try {
    		id=Integer.parseInt(idS);
    	}catch(NumberFormatException e)
    	{
    		e.printStackTrace();
    	}
    	List<Artist> best=new ArrayList<Artist>(this.model.percorsoMassimo(id));
    	for(Artist a:best)
    	{
    		txtResult.appendText(a.toString()+"\n");
    	}
    	
    } 

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String categoria=this.boxRuolo.getValue();
    	this.model.creaGrafo(categoria);
    	this.txtResult.appendText("GRAFO CREATO!!\n");
    	this.txtResult.appendText("# archi: "+this.model.getArchi()+"\n");
    	this.txtResult.appendText("# vertici: "+this.model.getVertici()+"\n");
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(this.model.getCategorie());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
