package ch.comstock.hivecontroller.channels;

import java.util.concurrent.LinkedBlockingQueue;

import org.pmw.tinylog.Logger;

import ch.comstock.hivecontroller.engine.Engine;
import ch.comstock.hivecontroller.engine.Message;
import ch.comstock.hivecontroller.mqtt.MsgType;
/**
 * 
 * @author MajorTwip
 *
 *Baseclass for every channel
 */
public class Channel {
	private String name;
	private String valueTopic;
	private String cmdTopic;
	private String module;
	
	/**
	 * Constructor
	 * @param name Name of the Channel
	 * @param valueTopic MQTT-Topic to send back value
	 * @param cmdTopic MQTT-Topic to subscribe to to get commands
	 */
	public Channel(String name, String valueTopic, String cmdTopic, String module) {
		this.setName(name);
		this.valueTopic = valueTopic;
		this.cmdTopic = cmdTopic;
		this.module = module;
	}
	
	public Channel(Channel channel) {
		this.name = channel.getName();
		this.cmdTopic = channel.getCmdTopic();
		this.valueTopic=channel.getValueTopic();
		this.module = channel.getModule();
	}
	
	/**
	 * @return the valueTopic
	 */
	public String getValueTopic() {
		return valueTopic;
	}
	/**
	 * @param valueTopic the valueTopic to set
	 */
	public void setValueTopic(String valueTopic) {
		this.valueTopic = valueTopic;
	}
	/**
	 * @return the cmdTopic
	 */
	public String getCmdTopic() {
		return cmdTopic;
	}
	
	/**
	 * @param cmdTopic the cmdTopic to set
	 */
	public void setCmdTopic(String cmdTopic) {
		this.cmdTopic = cmdTopic;
	}
	
	/**
	 * Override toString
	 */
	@Override
	public String toString() {
		String string = 	"Channel '"+name +"'\n" +
							"valueTopic: " +valueTopic +"\n"+
							"cmdTopic: " +cmdTopic;
		
		return string;
	}

	/**
	 * @return the Channelname
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name Channelname to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sends Subscribing-Order to the Queue
	 * @param msgOut The Sending-Queue for MQTT
	 */
	public void subscribe(LinkedBlockingQueue<Message> msgOut) {
		Message msg = new Message(MsgType.SUB,cmdTopic);
		if(!msgOut.offer(msg)) {
			Logger.warn("Could not add Subscription for channel {} to Sendingqueue",cmdTopic);
		}
	}

	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}
	
	public void set(String value) {
		
	}
	public String get() {
		return "Not Impemented";
	}
	
	public void sendVal(Engine eng) {
		eng.sendMsg(valueTopic, this.get());
	}

}
