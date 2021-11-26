package br.com.todolist.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import br.com.todolist.io.TarefaIO;
import br.com.todolist.model.StatusTarefa;
import br.com.todolist.model.Tarefa;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import jdk.nashorn.internal.ir.CatchNode;

public class IndexController implements Initializable {

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
    private TableColumn<Tarefa, LocalDate> tcData;
    
    @FXML
    private TableColumn<Tarefa, String> tcTarefa;
    
    @FXML
    private TableView<Tarefa> tvTarefa;
    
    private List<Tarefa> tarefas;
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
    		try {
    			TarefaIO.insert(tarefa);
    			//LIMPAR OS CAMPOS
        		limpar();
    		}catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Arquivo não encontrado: "+e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
    		}catch (IOException e) {
    			JOptionPane.showMessageDialog(null, "Erro ao gravar: "+e.getMessage(),"Erro", JOptionPane.ERROR_MESSAGE);
    			e.printStackTrace();
			} 		
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//DEFINIR OS PARÂMETROS PARA AS COLUNAS DO TABLEVIEW
		tcData.setCellValueFactory(new PropertyValueFactory<>("dataLimite"));
		tcTarefa.setCellValueFactory(new PropertyValueFactory<>("descricao"));
	}
	
	public void carregarTarefas() {
		try {
			tarefas = TarefaIO.read();
			tvTarefa.setItems(FXCollections.observableArrayList(tarefas));
			tvTarefa.refresh();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar as tarefas :"+e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
