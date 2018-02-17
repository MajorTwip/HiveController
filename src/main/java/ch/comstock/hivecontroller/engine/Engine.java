package ch.comstock.hivecontroller.engine;

import java.util.LinkedList;

import ch.comstock.hivecontroller.mqtt.Message;

public class Engine implements Runnable{
	LinkedList<Message> inMsg;
	
	public Engine(LinkedList<Message> inMsg) {
		this.inMsg = inMsg;		
	}
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			Message msg;
			try {
				msg = getMsg(this.inMsg);
				System.out.println(msg.getTarget());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
	}
	
	private Message getMsg(LinkedList<Message> msgQueue) throws InterruptedException {
		synchronized(inMsg){
			while(inMsg.isEmpty()) {
				inMsg.wait();
			}
			return inMsg.remove();
		}
	}
}
