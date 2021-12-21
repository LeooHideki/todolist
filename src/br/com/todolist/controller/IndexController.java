package br.com.todolist.controller;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.com.todolist.io.TarefaIO;
import br.com.todolist.model.StatusTarefa;
import br.com.todolist.model.Tarefa;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import jdk.nashorn.internal.ir.CatchNode;
import jdk.nashorn.internal.ir.SetSplitState;

public class IndexController implements Initializable, ChangeListener<Tarefa> {

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
	private TextField tfCodigo;

	@FXML
	private Label lbObrig;

	@FXML
	private Label lbAberta;

	@FXML
	private Label lbAdiada;

	@FXML
	private Label lbConcluida;

	@FXML
	private Label lbDataConcluida;

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
		if (tarefa != null) {
			int dias = Integer.parseInt(JOptionPane.showInputDialog(null, "Quantos dias vacê deseja adiar?",
					"Informe quantos dias", JOptionPane.QUESTION_MESSAGE));
			LocalDate novaData = tarefa.getDataLimite().plusDays(dias);
			tarefa.setDataLimite(novaData);
			tarefa.setStatus(StatusTarefa.ADIADA);
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
			try {
				TarefaIO.saveTarefas(tarefas);
				JOptionPane.showMessageDialog(null, "Nova data:" + tarefa.getDataLimite().format(fmt),
						"Data Adiada com sucesso", JOptionPane.INFORMATION_MESSAGE);
				carregarTarefas();
				limpar();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao salvar as tarefas:" + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	@FXML
	void btApagar(ActionEvent event) {
		if (tarefa != null) {
			int resposta = JOptionPane.showConfirmDialog(null, "Deseja excluir a tarefa " + tarefa.getId() + "?",
					"Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (resposta == 0) {
				tarefas.remove(tarefa);
				try {
					TarefaIO.saveTarefas(tarefas);
					carregarTarefas();
					limpar();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Erro ao excuir tarefa" + e.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}

			}
		}
	}

	@FXML
	void btConcluir(ActionEvent event) {
		if (tarefa != null) {
			tarefa.setStatus(StatusTarefa.CONCLUIDA);
			tarefa.setDataConcluida(LocalDate.now());
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
			try {
				TarefaIO.saveTarefas(tarefas);
				JOptionPane.showMessageDialog(null, "Data:" + tarefa.getDataConcluida().format(fmt), "Tarefa concluída",
						JOptionPane.INFORMATION_MESSAGE);
				carregarTarefas();
				limpar();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao salvar as tarefas:" + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	@FXML
	void btLimpar(ActionEvent event) {
		limpar();
	}

	@FXML
	void btSalvar(ActionEvent event) {
		// VALIDANDO OS CAMPOS
		if (tfNome.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe seu nome", "Erro", JOptionPane.ERROR_MESSAGE);
			tfNome.requestFocus();
			lbObrig.setVisible(true);
		} else if (dtData.getValue() == null) {
			JOptionPane.showMessageDialog(null, "Informe uma data", "Erro", JOptionPane.ERROR_MESSAGE);
			dtData.requestFocus();
			lbObrig.setVisible(true);
		} else if (tfDescricao.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe a tarefa", "Erro", JOptionPane.ERROR_MESSAGE);
			tfDescricao.requestFocus();
			lbObrig.setVisible(true);
		} else if (dtData.getValue().isBefore(LocalDate.now())) {
			JOptionPane.showMessageDialog(null, "Data inválida", "Erro", JOptionPane.ERROR_MESSAGE);
			dtData.requestFocus();
		} else {
			// VERIFICA SE A TAREFA NULA
			if (tarefa == null) {
				// INSTANCIANDO A TAREFA
				tarefa = new Tarefa();

				tarefa.setDataCriacao(LocalDate.now());
				tarefa.setStatus(StatusTarefa.ABERTA);
			}
			// POPULAR TAREFA
			tarefa.setNome(tfNome.getText());
			tarefa.setDataLimite(dtData.getValue());
			tarefa.setDescricao(tfDescricao.getText());
			tarefa.setComentarios(tfObs.getText());

			// TODO INSERIR NO "BANCO DE DADOS"
			try {
				if (tarefa.getId() == 0) {
					TarefaIO.insert(tarefa);
				} else {
					TarefaIO.saveTarefas(tarefas);
				}
				// LIMPAR OS CAMPOS
				limpar();
				carregarTarefas();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Arquivo não encontrado: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao gravar: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	@FXML
	void menuExportar(ActionEvent event) {
		FileFilter filter = new FileNameExtensionFilter("Arquivos HTML", "html", "htm");
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File arqSelecionado = chooser.getSelectedFile();
			if (!arqSelecionado.getAbsolutePath().endsWith(".html")) {
				arqSelecionado = new File(arqSelecionado + ".html");
			}
			try {
				TarefaIO.exportHtml(tarefas, arqSelecionado);
				Desktop desktop = Desktop.getDesktop();
				desktop.open(arqSelecionado);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao exportar tarefas: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();

			}
		}
	}

	@FXML
	void menuSair(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void menuSobre(ActionEvent event) {
		AnchorPane root;
		try {
			root = (AnchorPane) FXMLLoader.load(getClass().getResource("/br/com/todolist/view/Sobre.fxml"));
			Scene scene = new Scene(root, 283, 418);
			scene.getStylesheets()
					.add(getClass().getResource("/br/com/todolist/view/application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void limpar() {
		tarefa = null;
		tfNome.setText("");
		dtData.setValue(null);
		tfDescricao.setText("");
		tfObs.setText("");
		tfStatus.setText("");
		tfCodigo.setText("");
		tfNome.requestFocus();
		lbObrig.setVisible(false);

		btSalvar.setDisable(false);
		btAdiar.setDisable(true);
		btApagar.setDisable(true);
		btConcluir.setDisable(true);
		dtData.setDisable(false);
		tfCodigo.setDisable(true);

		tfNome.setEditable(true);
		tfDescricao.setEditable(true);
		tfObs.setEditable(true);

		lbDataConcluida.setVisible(false);

		tvTarefa.getSelectionModel().clearSelection();

		try {
			tfCodigo.setText(TarefaIO.readNext() + "");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Arquivo não encontrado :" + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// DEFINIR OS PARÂMETROS PARA AS COLUNAS DO TABLEVIEW
		try {
			tfCodigo.setText(TarefaIO.readNext() + "");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Arquivo não encontrado :" + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		tcData.setCellValueFactory(new PropertyValueFactory<>("dataLimite"));
		tcTarefa.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tcData.setCellFactory(call -> {
			// CORRIGE A FORMATAÇÃO DA COLUNA DA DATA
			return new TableCell<Tarefa, LocalDate>() {
				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
					if (!empty) {
						setText(item.format(fmt));
					} else {
						setText("");
					}

				}
			};

		});

		tvTarefa.setRowFactory(call -> new TableRow<Tarefa>() {
			protected void updateItem(Tarefa item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null) {
					setStyle("");
				} else if (item.getStatus() == StatusTarefa.CONCLUIDA) {
					setStyle("-fx-background-color: #ccffcc");
				} else if (item.getDataLimite().isBefore(LocalDate.now())) {
					setStyle("-fx-background-color: #ffcccc");
				} else if (item.getStatus() == StatusTarefa.ADIADA) {
					setStyle("-fx-background-color: #ffffcc");
				} else {
					setStyle("-fx-background-color: #cce0ff");
				}
			};
		});
		// EVENTO DE SELEÇÃO DE ITEM NA TABELA
		tvTarefa.getSelectionModel().selectedItemProperty().addListener(this);

		carregarTarefas();
		
	}

	public void carregarTarefas() {
		try {
			tarefas = TarefaIO.read();
			tvTarefa.setItems(FXCollections.observableArrayList(tarefas));
			tvTarefa.refresh();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar as tarefas :" + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	@Override
	public void changed(ObservableValue<? extends Tarefa> observable, Tarefa oldValue, Tarefa newValue) {
		// PASSO A REFERÊNCIA PARA A VARIÁVEL GLOBAL
		tarefa = newValue;
		if (tarefa != null) {
			tfNome.setText(tarefa.getNome());
			tfDescricao.setText(tarefa.getDescricao());
			dtData.setValue(tarefa.getDataLimite());
			tfObs.setText(tarefa.getComentarios());
			tfStatus.setText(tarefa.getStatus() + "");
			dtData.setDisable(true);
			dtData.setOpacity(1);
			tfCodigo.setText(tarefa.getId() + "");
			btApagar.setDisable(false);

			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MMM/yyyy");

			if (tarefa.getStatus() == StatusTarefa.CONCLUIDA) {
				btSalvar.setDisable(true);
				btAdiar.setDisable(true);
				btConcluir.setDisable(true);
				lbDataConcluida.setVisible(true);
				lbDataConcluida.setText("Tarefa concluída no dia " + tarefa.getDataConcluida().format(fmt));

				tfNome.setEditable(false);
				tfDescricao.setEditable(false);
				tfObs.setEditable(false);

			} else if (tarefa.getStatus() == StatusTarefa.ADIADA) {
				btAdiar.setDisable(true);
				btSalvar.setDisable(false);
				btConcluir.setDisable(false);

				tfNome.setEditable(true);
				tfDescricao.setEditable(true);
				tfObs.setEditable(true);

				lbDataConcluida.setVisible(false);

			} else {
				btAdiar.setDisable(false);
				btConcluir.setDisable(false);
				btSalvar.setDisable(false);

				tfNome.setEditable(true);
				tfDescricao.setEditable(true);
				tfObs.setEditable(true);

				lbDataConcluida.setVisible(false);

			}

		}

	}

}
