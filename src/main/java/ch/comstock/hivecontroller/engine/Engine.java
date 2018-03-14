package ch.comstock.hivecontroller.engine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.pmw.tinylog.Logger;

import com.typesafe.config.Config;

import ch.comstock.hivecontroller.channels.Channel;
import ch.comstock.hivecontroller.mqtt.MsgType;
/**
 * The engine, or Supervisorclass.
 * @author MajorTwip
 *
 */
public class Engine implements Runnable{
	LinkedBlockingQueue<Message> inMsg;
	LinkedBlockingQueue<Message> outMsg;
	Config conf;
	String topicBase;
	String defaultValueSuffix;
	
	HashMap<String,Channel> channels;


	/**
	 * 
	 * @param inMsg Queue of received messages
	 * @param outMsg Queue of messages to send over MQTT
	 * @param conf The global config
	 */
	public Engine(LinkedBlockingQueue<Message> inMsg, LinkedBlockingQueue<Message> outMsg, Config conf) {
		this.inMsg = inMsg;
		this.outMsg = outMsg;
		this.conf = conf;
		this.topicBase = conf.getString("mqtt.topicBase");
		this.defaultValueSuffix = conf.getString("mqtt.defaultValueSuffix");
		if(topicBase.charAt(this.topicBase.length()-1)!='/') {
			topicBase = topicBase + "/";
		}
		if(defaultValueSuffix.charAt(0)!='/') {
			defaultValueSuffix =  "/" + defaultValueSuffix;
		}
		
		sendStatus();
		
		channels = Initiator.createMapSubscribe(conf, outMsg);
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
		}
	}
	
	private Message getMsg(LinkedBlockingQueue<Message> msgQueue) throws InterruptedException {
		synchronized(inMsg){
			while(inMsg.isEmpty()) {
				inMsg.wait();
			}
			return inMsg.remove();
		}
	}
	
	private String composeCmdTopic(String name) {
		String topic = topicBase + name + defaultValueSuffix;
		return topic;
	}
	
	private void sendMsg(String topic, String payload) {
		Message msg = new Message(MsgType.OUT,topic,payload);
		synchronized (outMsg) {
			outMsg.add(msg);
			outMsg.notifyAll();
		}
	}
	
	private void sendStatus() {
		sendMsg(topicBase + "heartbeat", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
	}
	
	
}
