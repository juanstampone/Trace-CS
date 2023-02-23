package com.cs.tesis.trace;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;

import org.aspectj.weaver.loadtime.WeavingURLClassLoader;

import com.cs.tesis.services.JarService;
import com.cs.tesis.services.ProfilerService;
import com.cs.tesis.trace.aspect.Logger;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MainUIController implements Initializable {

    //private static final Image packageIcon = loadImage("/icons/package.png");
    //private static final Image classIcon = loadImage("/icons/class.png");
    private static final Image progressIcon = loadImage("/icons/progress.png");
    private static final Image doneIcon = loadImage("/icons/done.png");
	
    @FXML private ImageView btnClose;
    @FXML private ImageView btnMinimize;
    @FXML private ImageView btnUpload;
    @FXML private ImageView btnClearOutPut;
    @FXML private Label lblMainClass;
    @FXML private AnchorPane topWindow;
    @FXML private Label errorLabel;
    @FXML private Label fileLabel;
    @FXML private Label btnEjecutar;
    @FXML private Label btnExportar;
    @FXML private TreeView<String> txtOutput;
      
    private JarService loaderService = new JarService();
    private ProfilerService profilerService = new ProfilerService();
    
    private Stage pStage; // Stage de mainWindow
    
    private double xOffset = 0; // xOffset e yOffset permiten almacenar la posicion de la ventana al ser desplazada por el usuario
    private double yOffset = 0; 
    
    public MainUIController(Stage primaryStage) {
    	pStage = primaryStage;   	
	}
    
    private static Image loadImage(String url) {
        return new Image(JarService.class.getResourceAsStream(url));
    }
    
    public void startMethod(int level, String statement) {
        TreeItem<String> root = txtOutput.getRoot();
        while (level > 0) {
            List<TreeItem<String>> children = root.getChildren();
            root = children.get(children.size() - 1);
            level--;
        }
        TreeItem<String> child = new TreeItem<>(statement, new ImageView(progressIcon));
        child.setExpanded(true);
        root.getChildren().add(child);
    }

    public void endMethod(int level, String statement) {
        TreeItem<String> root = txtOutput.getRoot();
        while (level > 0) {
            List<TreeItem<String>> children = root.getChildren();
            root = children.get(children.size() - 1);
            level--;
        }
        TreeItem<String> child = new TreeItem<>(statement, new ImageView(doneIcon));
        child.setExpanded(true);
        root.getChildren().add(child);
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {

    	Logger.getLoggingClient().attachController(this);
    	lblMainClass.textProperty().bind(profilerService.mainClassProperty());
    	fileLabel.textProperty().bind(loaderService.filePathProperty());
        profilerService.fileProperty().bind(loaderService.fileProperty());
        TreeItem<String> methodRoot = new TreeItem<>("Method call tree:");
        methodRoot.setExpanded(true);
        txtOutput.setRoot(methodRoot);
        
        loaderService.setOnSucceeded(event -> {
            JarService.Result result = loaderService.getValue();
            String mainClass = result.getMainClass();
            profilerService.setMainClass(mainClass);
        });
        
    	// MOVIMIENTO DE VENTANA DESDE TOP WINDOWS
		topWindow.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });	
		
		topWindow.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pStage.setX(event.getScreenX() - xOffset);
                pStage.setY(event.getScreenY() - yOffset);
            }
        });
		
		// CERRAR VENTANA MEDIANTE BOTON CLOSE
		btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

		     @Override
		     public void handle(MouseEvent event) {
		    	 loaderService.cancel();
		    	 profilerService.cancel();
		         pStage.close();		         
		         event.consume();
		     }
		});
		
		// MINIMIZAR VENTANA MEDIANTE BOTON MINIMIZE
		btnMinimize.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

		     @Override
		     public void handle(MouseEvent event) {
		         pStage.setIconified(true);	         
		         event.consume();
		     }
		});
		
		btnUpload.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

		     @Override
		     public void handle(MouseEvent event) {
		    	 
		    	 FileChooser fileChooser = new FileChooser();
		         fileChooser.setTitle("Open JAR File");
		         fileChooser.getExtensionFilters().addAll(
		                 new ExtensionFilter("JAR Files", "*.jar"),
		                 new ExtensionFilter("All Files", "*.*")
		         );
		         File selectedFile = fileChooser.showOpenDialog(pStage);
		         if (selectedFile != null) {
		             loaderService.setFile(selectedFile);
		             loaderService.start();
		         }         
		     }
		});
		
		btnEjecutar.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

		     @Override
		     public void handle(MouseEvent event) {
		    	 
		    	 if (loaderService.getFile() != null) {
			         Logger.getLoggingClient().pushLevel(Logger.MethodHeader);
			         
		    		 profilerService.start();
		    	 }		    	 
                             
		     }
		});
		
		btnExportar.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

		     @Override
		     public void handle(MouseEvent event) {
		    	 
		    	  FileChooser fileChooser = new FileChooser();
		          fileChooser.setTitle("Save data");
		          fileChooser.setInitialFileName("Report.txt");
		          File selectedFile = fileChooser.showSaveDialog(pStage);
		          if (selectedFile != null) {
		              Logger.getLoggingClient().exportLogger(selectedFile);
		          }    	 
                            
		     }
		});
		
		btnClearOutPut.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

		     @Override
		     public void handle(MouseEvent event) {
		    	 
		    	 loaderService.reset();
		    	 profilerService.reset();
		    	 loaderService.setFile(null);
		    	 profilerService.setFile(null);
		    	 profilerService.setMainClass(null);
		    	 Logger.getLoggingClient().cleanLogger();
		    	 txtOutput = new TreeView<String>();
		    	 
		    	 TreeItem<String> methodRoot = new TreeItem<>("Method call tree:");
		         methodRoot.setExpanded(true);
		         txtOutput.setRoot(methodRoot);          
		     }
		});
		
	}

}
