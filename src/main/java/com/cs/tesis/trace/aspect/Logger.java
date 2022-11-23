package com.cs.tesis.trace.aspect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Logger {
	private static Logger instance = new Logger();
	private FileWriter filewriter = null;

	public static Logger getLoggingClient() {
		return instance;
	}

	private FileWriter getFileWriter() {
		try {
			if (filewriter == null) {
				filewriter = new FileWriter(new File("Trace.txt"));
				addHeader(filewriter);
			}
		} catch (IOException e) {
			System.out.println("Error in Loggin Client: " + e);
		}
		return filewriter;
	}
	
	private void addHeader(FileWriter writer) {
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<trace_Metamodel:ExecutionTrace\r\n" + 
				"    xmi:version=\"2.0\"\r\n" + 
				"    xmlns:xmi=\"http://www.omg.org/XMI\"\r\n" + 
				"    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n" + 
				"    xmlns:trace_Metamodel=\"http://www.example.org/trace_Metamodel\"\r\n" + 
				"    xsi:schemaLocation=\"http://www.example.org/trace_Metamodel trace_Metamodel.ecore\">\r\n";
		
		try {
			writer.write(header);
			writer.flush();
		} catch (IOException e) {
			System.out.println("Error in Logging Client: " + e);
		}
		
		
	}

	public void instrument(List<String> record) {
		FileWriter writer = getFileWriter();
		try {
			for (int count = 0; count < record.size(); count++) {
				writer.write((String) record.get(count));
				writer.write("\r\n");
			}
			writer.flush();
		} catch (IOException e) {
			System.out.println("Error in Logging Client: " + e);
		}
	}
}
