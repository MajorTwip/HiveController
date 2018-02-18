package ch.comstock.hivecontroller.engine;

import java.util.LinkedList;

import org.pmw.tinylog.Logger;

import ch.comstock.hivecontroller.mqtt.Message;

public class Engine implements Runnable{
	LinkedList<Message> inMsg;
	LinkedList<Message> outMsg;


	
	public Engine(LinkedList<Message> inMsg, LinkedList<Message> outMsg) {
		this.inMsg = inMsg;
		this.outMsg = outMsg;
	}
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			Message msg = null;
			try {
				msg = getMsg(this.inMsg);
				Logger.trace("Message received by Engine.\nTopic: {} \nPayload: {}",msg.getTarget(),msg.getValue());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
			if(msg!=null) {
				synchronized (outMsg) {
					outMsg.addLast(msg);
					outMsg.notifyAll();
				}
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
