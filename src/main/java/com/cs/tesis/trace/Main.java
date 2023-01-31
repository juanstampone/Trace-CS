package com.cs.tesis.trace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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

	
	/*@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
			MainUIController controller = new MainUIController(primaryStage);
			loader.setController(controller);
			Pane mainPane = (Pane) loader.load();
			Scene scene = new Scene(mainPane);
			scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
			primaryStage.setTitle("Agente de Trazas");
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
	}*/
	
	
	@Override
	public void start(Stage stage) throws IOException {
	
    Locale.setDefault(Locale.US);
    String title = String.format("%s v%s", "hola", "hola");
    URL layout = Main.class.getResource("/layout.fxml");
    Image icon = new Image(Main.class.getResourceAsStream("/icon.png"));
    Parent root = FXMLLoader.load(layout);
    Scene scene = new Scene(root, 1280, 720);
    stage.setScene(scene);
    stage.setTitle(title);
    stage.getIcons().add(icon);
    stage.setIconified(false);
    stage.show();
}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/*
	public static void main(String[] args) throws Exception  {
		Main m = new Main();
		m.trace();
		
	}
	
	
	
	
	public void trace() throws Exception{
		try {
			File file = new File("C:\\Users\\Juan\\Desktop\\Library.jar") ;
			String [] args = new String[1];
	        JarInputStream jarInputStream = new JarInputStream(new FileInputStream(file));
	
	        Attributes mainAttributes = jarInputStream.getManifest().getMainAttributes();
	        String mainClass = mainAttributes.getValue("Main-Class");
	
	//        Trie<String> packages = new Trie<>("All packages");
	
	      /*  JarEntry jarEntry;
	        while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
	            String className = jarEntry.getName();
	            if (className.endsWith(".class")) {
	                List<String> path = Arrays.asList(jarEntry.getName().split("/"));
	               // packages.add(path);
	            }
	        }/
	        
			URL appUrl = file.toURI().toURL();
			
	        URL aspectsUrl = Main.class.getProtectionDomain().getCodeSource().getLocation();
	        URL[] classURLs = new URL[]{appUrl, aspectsUrl};
	        URL[] aspectURLs = new URL[]{aspectsUrl};
	        
	        ClassLoader defaultClassLoader = Thread.currentThread().getContextClassLoader();
	        ClassLoader weavingClassLoader = new WeavingURLClassLoader(classURLs, aspectURLs, defaultClassLoader);
	        Thread.currentThread().setContextClassLoader(weavingClassLoader);
	
	        Class<?> app = weavingClassLoader.loadClass(mainClass);
	        Method run = app.getMethod("main", String[].class);
	        run.invoke(null, (Object) args);
	        
	        
	        Thread.currentThread().setContextClassLoader(defaultClassLoader);
		}catch (Exception e) {
			e.printStackTrace();
				System.out.println(e.getStackTrace());
			}
	}*/
	

}
