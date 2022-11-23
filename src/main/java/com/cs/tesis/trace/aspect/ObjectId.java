package com.cs.tesis.trace.aspect;

import java.util.HashMap;
import java.util.LinkedList;


public class ObjectId {
	private static ObjectId instance = new ObjectId();
	private HashMap<Integer, String> objects = null;
	private static int ID = 0;

	public static ObjectId getObjectId() {
		return instance;
	}

	private HashMap<Integer, String> getObjects() {
		if (objects == null) {
			objects = new HashMap<Integer, String>();
		}
		return objects;
	}

	public String add(String valor) {	
		HashMap<Integer, String> map = getObjects();
		if (!map.containsValue(valor)){
			System.out.println("entro");
			map.put(ID, valor);
			ID++;
			return "@Object."+ (ID-1);
		}
		return "@Object."+ID;
	}

	public LinkedList log() {
		HashMap<Integer, String> map = getObjects();
		LinkedList log = new LinkedList();	
		for (Integer id : map.keySet()) {
			log.add("<objects id=\"" + map.get(id) + "\"/>");
		}		
		return log;
	}
}
