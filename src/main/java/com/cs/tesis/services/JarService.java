package com.cs.tesis.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.cs.tesis.trace.aspect.Trie;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class JarService extends Service<JarService.Result> {

    private ObjectProperty<File> file = new SimpleObjectProperty<>();
    private StringProperty filePath = new SimpleStringProperty();
	
    @Override
    protected Task<JarService.Result> createTask() {
        return new LoaderTask(getFile());
    }
    
    public File getFile() {
        return file.get();
    }

    public void setFile(File file) {
        this.file.set(file);
        if (file != null) {
        	this.filePath.set(file.toString());
        } else {
        	this.filePath.set(null);
        }
        
    }
    
    public StringProperty filePathProperty() {
        return filePath;
    }

    public ObjectProperty<File> fileProperty() {
        return file;
    }
    
    public static class Result {

        private final String mainClass;

        /*private final Trie<String> packages;*/

        public Result(String mainClass) {
            this.mainClass = mainClass;
            //this.packages = packages;
        }

        public String getMainClass() {
            return mainClass;
        }

        /*public Trie<String> getPackages() {
            return packages;
        }*/
    }
    
    public static class LoaderTask extends Task<Result> {

        private static final String MAIN_CLASS = "Main-Class";
        private static final String ZIP_SEPARATOR = "/";

        private final File file;

        public LoaderTask(File file) {
            this.file = file;
        }

        @Override
        protected Result call() throws IOException {
            JarInputStream jarInputStream = new JarInputStream(new FileInputStream(file));

            Attributes mainAttributes = jarInputStream.getManifest().getMainAttributes();
            String mainClass = mainAttributes.getValue(MAIN_CLASS);

            /*Trie<String> packages = new Trie<>("All packages");

            JarEntry jarEntry;
            while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
                String className = jarEntry.getName();
                if (className.endsWith(".class")) {
                    List<String> path = Arrays.asList(jarEntry.getName().split(ZIP_SEPARATOR));
                    packages.add(path);
                }
            }*/

            //return new Result(mainClass, packages);
            return new Result(mainClass);
        }
    }
    
}
