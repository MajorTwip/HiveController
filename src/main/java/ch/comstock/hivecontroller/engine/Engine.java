package ch.comstock.hivecontroller.engine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import org.pmw.tinylog.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.typesafe.config.Config;

import ch.comstock.hivecontroller.channels.Channel;
import ch.comstock.hivecontroller.channels.GPIOchannelIn;
import ch.comstock.hivecontroller.mqtt.MsgType;
import ch.comstock.hivecontroller.utils.Topics;
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
	
	GpioController gpioctrl = null;
	



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
		this.topicBase = Topics.preparePrefix(conf.getString("mqtt.topicBase"));
		this.defaultValueSuffix = Topics.prepareSuffix(conf.getString("mqtt.defaultValueSuffix"));
			
		try {
			gpioctrl = GpioFactory.getInstance();
	    } catch  (UnsatisfiedLinkError e){
	        System.err.println("platform does not support this driver");      
	    }
	    catch (Exception e) {
	        System.err.println("platform does not support this driver");
	       
	    }
		
		Timer t1 = new Timer();
		t1.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				sendStatus();
			}
		}, 0, 60000);
		
		channels = Initiator.createMap(conf, gpioctrl);
		
		
		
		for(String key:channels.keySet()) {
			Channel chan = channels.get(key);
			chan.subscribe(outMsg);
			if(chan.getClass().equals(GPIOchannelIn.class)){
				GPIOchannelIn chanIn = (GPIOchannelIn) chan;
				chanIn.addListener(outMsg);
			}
		}
	}
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			Message msg = null;
			try {
				msg = inMsg.take();
				Logger.trace("Message received by Engine.\nTopic: {} \nPayload: {}",msg.getTarget(),msg.getValue());
				Handler.handle(msg,this);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
	}
	
	
	public void sendMsg(String topic, String payload) {
		Message msg = new Message(MsgType.OUT,topic,payload);
		outMsg.add(msg);
	}
	
	private void sendStatus() {
		sendMsg(topicBase + "heartbeat", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
	}
	
	public HashMap<String,Channel> getChannels(){
		return channels;
	}
	
	
}
