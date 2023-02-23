package com.cs.tesis.trace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.aspectj.weaver.loadtime.WeavingURLClassLoader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainWindow.fxml"));
			MainUIController controller = new MainUIController(primaryStage);
			loader.setController(controller);
			Pane mainPane = (Pane) loader.load();
			Scene scene = new Scene(mainPane);
			scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
			primaryStage.setTitle("Análisis de código en tiempo de Ejecución");
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
	        File file = new File("/images/ToolIcon.png");
	        Image ToolIcon = new Image(file.toURI().toString());
	        primaryStage.getIcons().add(ToolIcon);
			primaryStage.show();
			mainPane.requestFocus();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	

}
