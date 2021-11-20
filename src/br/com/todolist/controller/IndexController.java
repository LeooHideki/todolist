package br.com.todolist.controller;

import java.time.LocalDate;

import javax.swing.JOptionPane;

import br.com.todolist.model.StatusTarefa;
import br.com.todolist.model.Tarefa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class IndexController {

    @FXML
    private DatePicker dtData;

    @FXML
    private TextArea tfObs;

    @FXML
    private TextField tfDescricao;                                       

    @FXML
    private TextField tfStatus;

    @FXML
    private TextField tfNome;
    
    @FXML
    private Label lbObrig;
    
    @FXML
    private Button btAdiar;
    
    @FXML
    private Button btApagar;
    
    @FXML
    private Button btConcluir;
    
    @FXML
    private Button btLimpar;
    
    @FXML
    private Button btSalvar;
    
    @FXML
    private Tarefa tarefa;

    @FXML
    void btAdiar(ActionEvent event) {

    }

    @FXML
    void btApagar(ActionEvent event) {

    }

    @FXML
    void btConcluir(ActionEvent event) {

    }

    @FXML
    void btLimpar(ActionEvent event) {
    	limpar();
    }

    @FXML
    void btSalvar(ActionEvent event) {
    	//VALIDANDO OS CAMPOS
    	if(tfNome.getText().isEmpty()) {
    		JOptionPane.showMessageDialog(null, "Informe seu nome", "Erro", JOptionPane.ERROR_MESSAGE);
    		tfNome.requestFocus();
    		lbObrig.setStyle("-fx-text-fill: red");
    	}else if(dtData.getValue() == null) {
    		JOptionPane.showMessageDialog(null, "Informe uma data", "Erro", JOptionPane.ERROR_MESSAGE);
    		dtData.requestFocus();
    		lbObrig.setStyle("-fx-text-fill: red");
    	}else if(tfDescricao.getText().isEmpty()) {
    		JOptionPane.showMessageDialog(null, "Informe a tarefa", "Erro", JOptionPane.ERROR_MESSAGE);
    		tfDescricao.requestFocus();
    		lbObrig.setStyle("-fx-text-fill: red");
    	}else if(dtData.getValue().isBefore(LocalDate.now())){
    		JOptionPane.showMessageDialog(null, "Data inválida", "Erro", JOptionPane.ERROR_MESSAGE);
        	dtData.requestFocus();
    	}else {
    		// INSTANCIANDO A TAREFA
    		tarefa = new Tarefa();
    		// POPULAR TAREFA
    		tarefa.setNome(tfNome.getText());
    		tarefa.setDataCriacao(LocalDate.now());
    		tarefa.setStatus(StatusTarefa.ABERTA);
    		tarefa.setDataLimite(dtData.getValue());
    		tarefa.setDescricao(tfDescricao.getText());
    		tarefa.setComentarios(tfObs.getText());
    		
    		// TODO INSERIR NO "BANCO DE DADOS"
    		System.out.println(tarefa.formatToSave());
    		//LIMPAR OS CAMPOS
    		limpar();
    		
    		
    	}
    }
    
    private void limpar() {
    	tarefa = null;
    	tfNome.setText("");
    	dtData.setValue(null);
    	tfDescricao.setText("");
    	tfObs.setText("");
    	tfStatus.setText("");
    	tfNome.requestFocus();
    	lbObrig.setStyle("-fx-text-fill: azure");
    }

}
