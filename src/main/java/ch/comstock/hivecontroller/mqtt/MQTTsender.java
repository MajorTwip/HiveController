package ch.comstock.hivecontroller.mqtt;

import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.pmw.tinylog.Logger;

import ch.comstock.hivecontroller.engine.Message;
/**
 * Runnable which waits for {@link Message} to either Send them or Subscribe
 * @author MajorTwip
 *
 */
public class MQTTsender implements Runnable{
	private MQTTclient client;
	private LinkedBlockingQueue<Message> outMsg;
	private int qos;
	/**
	 * 
	 * @param client nomally the Parent-MQTTClient
	 * @param outMsg Message-Queue on whom to listen
	 * @param qos QOS of the message
	 */
	public MQTTsender(MQTTclient client, LinkedBlockingQueue<Message> outMsg, int qos) {
		this.client = client;
		this.outMsg = outMsg;
		this.qos = qos;
	}
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			Message msg;
			try {
				msg = outMsg.take();
				switch(msg.getType()) {
				case OUT:
					MqttMessage mqttMsg = new MqttMessage(msg.getValue().getBytes());
					mqttMsg.setQos(qos);
					client.sendMQTT(msg.getTarget(), mqttMsg);
					Logger.trace("Message to send by Engine.\nTopic: {} \nPayload: {}",msg.getTarget(),msg.getValue());
					break;
				case SUB:
					client.subscribeMQTT(msg.getTarget());
					break;
				default:
					Logger.debug("MQTTsender received a Message who's neither SUB nor OUT but {}" ,msg.getType().toString());
				}
				
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Logger.warn("MQTT-Sender interrupted");
				e.printStackTrace();
			}
		}
	}
	
	
}
