package br.com.todolist.application;
	
import br.com.todolist.io.TarefaIO;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			TarefaIO.createFiles();
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/com/todolist/view/Index.fxml"));
			Scene scene = new Scene(root,1200,650);
			scene.getStylesheets().add(getClass().getResource("/br/com/todolist/view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("To Do List");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/br/com/todolist/imagens/icone2.png")));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
}
