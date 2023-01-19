package com.cs.tesis.trace;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;

import org.aspectj.weaver.loadtime.WeavingURLClassLoader;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MainUIController extends Application implements Initializable {


    @FXML private ImageView btnClose;
    @FXML private ImageView btnMinimize;
    @FXML private ImageView btnUpload;
    @FXML private ImageView btnClearOutPut;
    @FXML private AnchorPane topWindow;
    @FXML private Label errorLabel;
    @FXML private Label fileLabel;
    @FXML private Label btnEjecutar;
    @FXML private TextArea txtOutput;
    
    
    //private Analityc analityc = new Analityc();
    private ObservableList<String> listaDispositivos = FXCollections.observableArrayList(); 
      
    private FileChooser fileChooser = new FileChooser();
    //private Desktop desktop = Desktop.getDesktop();
    
    private Stage pStage; // Stage de mainWindow
    
    private double xOffset = 0; // xOffset e yOffset permiten almacenar la posicion de la ventana al ser desplazada por el usuario
    private double yOffset = 0; 
    private boolean styled = false; // Permite verificar si ya fue removido el marco de la ventana. 
    private Stage sStage = new Stage(); // Stage de la segunda ventana: 2. Ingrese el valor de la inversion deseada
    
    //private JsoupRun imageSmartPhone = new JsoupRun();
    //private Task getImageWorker; 
	//private JsoupRun testConexion = new JsoupRun();
    
    public MainUIController(Stage primaryStage) {
    	pStage = primaryStage; 
	}
    
	@Override
	public void start(Stage secondStage) throws Exception {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("investmentWindow.fxml"));
		//investmentWindowController c = new investmentWindowController(pStage, secondStage, analityc); 
		//loader.setController(c);
		Pane mainPane = (Pane) loader.load();
		Scene scene = new Scene(mainPane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		secondStage.setScene(scene);
		secondStage.setX(xOffset);
		secondStage.setY(yOffset);
		if (!styled) {
			secondStage.initStyle(StageStyle.UNDECORATED);
			styled = true;
		}
		secondStage.setWidth(pStage.getWidth());
		secondStage.setHeight(pStage.getHeight());
		secondStage.show();
		mainPane.requestFocus();
	}
	
	// SIGUIENTE VENTANA investmentWindow mediante btnNext
	public void nextInvestmentWindow() {
		System.out.println("Metodo por afuera");		
	}
	
	private void executeJar() {

		StringBuffer sb = new StringBuffer();
		try {
			
			if (fileLabel.getText() != null) {
					
				try {
					File file = new File(fileLabel.getText()) ;
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
			        }*/
			        
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
			}
		}
		catch(Exception ex) {
			
		}
		
		txtOutput.setEditable(false);
		txtOutput.setText(sb.toString());
		
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {

        	    	
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
		    	 
		    	 File file = fileChooser.showOpenDialog(pStage);
                 if (file != null) {
                	 fileLabel.setText(file.toString());                 
                 }               
		     }
		});
		
		btnEjecutar.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

		     @Override
		     public void handle(MouseEvent event) {
		    	 
		    	 if (!fileLabel.getText().isEmpty()) {
		    		 executeJar();
		    	 }
		    	 
                             
		     }
		});
		
		btnClearOutPut.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

		     @Override
		     public void handle(MouseEvent event) {
		    	 
		    	 txtOutput.setText("");   	 
                            
		     }
		});
		
	}

}
