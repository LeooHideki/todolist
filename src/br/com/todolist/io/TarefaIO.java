package br.com.todolist.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.sun.xml.internal.bind.v2.model.core.ID;

import br.com.todolist.model.StatusTarefa;
import br.com.todolist.model.Tarefa;

public class TarefaIO {
	
	 private static final String FOLDER = System.getProperty("user.home")+"/todolist";
	 private static final String FILE_ID = FOLDER + "/id.csv"; 
	 private static final String FILE_TAREFA = FOLDER + "/tarefas.csv";
	 
	 public static void createFiles() {
		 try {
			 File folder = new File(FOLDER);
			 File fileId = new File(FILE_ID);
			 File fileTarefa = new File(FILE_TAREFA);
		 
			 if(!folder.exists()) {
				 folder.mkdir();
				 fileTarefa.createNewFile();
				 fileId.createNewFile();
				 
				 FileWriter writer = new FileWriter(fileId);
				 writer.write("1");
				 writer.close();
			 }
		 }catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 //GRAVAR TAREFA
	 public static void insert(Tarefa tarefa) throws IOException {
		 File arqTarefa = new File(FILE_TAREFA);
		 File arqId = new File(FILE_ID);
		 Scanner sc = new Scanner(arqId);
		 tarefa.setId(sc.nextLong());
		 sc.close();
		 FileWriter writer = new FileWriter(arqTarefa, true);
		 writer.append(tarefa.formatToSave());
		 writer.close();
		//GRAVAR O NOVO ID NO ARQUIVO DE ID
		 writer = new FileWriter(arqId);
		 writer.write((tarefa.getId() + 1) +"");
		 writer.close();
	 }
	 //LER TAREFA
	 public static List<Tarefa> read() throws IOException{
		 File arqTarefa = new File(FILE_TAREFA);
		 List<Tarefa> tarefas = new ArrayList<>();
		 FileReader reader = new FileReader(arqTarefa);
		 BufferedReader buff = new BufferedReader(reader);
		 
		 String linha;
		 while((linha = buff.readLine()) != null) {
			 //GUARDA INFORMA��O NO VETOR AT� O ";"
			 String[] vetor = linha.split(";");
			 
			 Tarefa t = new Tarefa();
			 t.setId(Long.parseLong(vetor[0]));
			 t.setNome(vetor[1]);
			 
			 DateTimeFormatter padraoData = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
			 t.setDataCriacao(LocalDate.parse(vetor[2], padraoData));
			 t.setDataLimite(LocalDate.parse(vetor[3], padraoData));
			 if(!vetor[4].isEmpty()) {
				 t.setDataConcluida(LocalDate.parse(vetor[4], padraoData));
			 }
			 t.setDescricao(vetor[5]);
			 t.setComentarios(vetor[6]);
			 int indStatus = Integer.parseInt(vetor[7]);
			 t.setStatus(StatusTarefa.values()[indStatus]);
			 tarefas.add(t);
		 }
		 reader.close();
		 buff.close();
		 return tarefas;
	 }
}
