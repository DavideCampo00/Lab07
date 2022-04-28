/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.PrimitiveIterator.OfDouble;

import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="cmbNerc"
    private ComboBox<Nerc> cmbNerc; // Value injected by FXMLLoader

    @FXML // fx:id="txtYears"
    private TextField txtYears; // Value injected by FXMLLoader

    @FXML // fx:id="txtHours"
    private TextField txtHours; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    private Model model;
    
    @FXML
    void doRun(ActionEvent event) {
    	txtResult.clear();
    	int anni=0;
    	int ore=0;
    	Nerc nerc=cmbNerc.getValue();
    	if(nerc==null) {
    		txtResult.setText("Selezionare un NERC");
    	}
    	int nercId=nerc.getId();
    	
    	try {
			 anni=Integer.parseInt(txtYears.getText());
			 ore=Integer.parseInt(txtHours.getText());
		} catch (Exception e) {
			e.printStackTrace();
			txtResult.setText("Inserire dei numeri interi");
			return;
		}
    	List<PowerOutages> output=this.model.seleziona(nercId, anni, ore);
    	int total=0;
    	int hours=(int)this.model.calcolaOre(output);
    	for(PowerOutages p:output) {
    		total+=p.getCustomersAffected(); 		
    	}
    	txtResult.appendText("Tot people affected: "+total+"\n"+"Tot hours of outage: "+hours+"\n");
    	for(PowerOutages p:output) {
    		txtResult.appendText(p.toString()+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert cmbNerc != null : "fx:id=\"cmbNerc\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        // Utilizzare questo font per incolonnare correttamente i dati;
        txtResult.setStyle("-fx-font-family: monospace");
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbNerc.getItems().clear();
    	List<Nerc> list=this.model.getNercList();
    	for(Nerc n:list) {
    		cmbNerc.getItems().add(n);  		
    	}
    }
}
