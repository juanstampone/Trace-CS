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




public class Main {

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
