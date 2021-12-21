package br.com.todolist.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SobreController {
	
	@FXML
	private Button btOk;
	
	@FXML
	public void btOkClick(ActionEvent event) {
		Stage stage = (Stage) btOk.getScene().getWindow();
		stage.close();
	}
}
