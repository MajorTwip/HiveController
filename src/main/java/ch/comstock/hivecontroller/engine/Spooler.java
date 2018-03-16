package ch.comstock.hivecontroller.engine;

import java.util.LinkedList;

public class Spooler {
	private LinkedList<Message> messages;
	
	public Spooler() {
		this.messages = new LinkedList<>();
	}
	
	public synchronized void addMessage(Message message) {
		messages.add(message);
	}
	
	public synchronized Message getRemove() {
		return messages.removeFirst();
	}
	

}
