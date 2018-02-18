package ch.comstock.hivecontroller.mqtt;

import java.util.LinkedList;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.pmw.tinylog.Logger;

public class MQTTsender implements Runnable{
	private MqttClient client;
	private LinkedList<Message> outMsg;
	private int qos;
	
	public MQTTsender(MqttClient client, LinkedList<Message> outMsg, int qos) {
		this.client = client;
		this.outMsg = outMsg;
		this.qos = qos;
	}
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			Message msg;
			try {
				msg = getMsg(outMsg);
				MqttMessage mqttMsg = new MqttMessage(msg.getValue().getBytes());
				mqttMsg.setQos(qos);
				client.publish(msg.getTarget(), mqttMsg);
				Logger.trace("Message to send by Engine.\nTopic: {} \nPayload: {}",msg.getTarget(),msg.getValue());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			} catch (MqttException e) {
				Logger.warn(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	private Message getMsg(LinkedList<Message> msgQueue) throws InterruptedException {
		synchronized(msgQueue){
			while(msgQueue.isEmpty()) {
				msgQueue.wait();
			}
			return msgQueue.remove();
		}
	}
	
}
