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
    	String ruolo=this.boxRuolo.getValue();
    	this.model.creaGrafo(ruolo);
    	List<Adiacenza> lista=new ArrayList<Adiacenza>(this.model.getArtistiConnessi(ruolo));
    	for(Adiacenza a:lista)
    	{
    		this.txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	String num=this.txtArtista.getText();
    	Integer numI=0;
    	try {
    		numI=Integer.parseInt(num);
    	}catch(NumberFormatException e)
    	{
    		e.printStackTrace();
    	}
    	if(this.model.contieneGrafo(numI)==false)
    	{
    		txtResult.appendText("NON PRESENTE NEL GRAFO");
    	}
    	else
    	{
    	List<Artist> artisti=this.model.trovaPercorso(numI);
    	this.txtResult.appendText("IL PERCORSO PIU' LUNGO HA LUNGHEZZA "+artisti.size()+"ED E' COMPOSTO DA :\n");
    	for(Artist a:artisti)
    	{
    		this.txtResult.appendText(a.toString()+"\n");
    	}
    	}
    } 

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String ruolo=this.boxRuolo.getValue();
    	this.model.creaGrafo(ruolo);
    	txtResult.appendText("GRAFO CREATO!\n");
    	txtResult.appendText("# vertici: "+this.model.getVertici()+"\n");
    	txtResult.appendText("# archi: "+this.model.getArchi()+"\n");
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(this.model.getRuoli());
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
