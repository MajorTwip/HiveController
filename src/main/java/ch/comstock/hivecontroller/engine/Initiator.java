package ch.comstock.hivecontroller.engine;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.pmw.tinylog.Logger;

import com.pi4j.io.gpio.GpioController;
import com.typesafe.config.Config;

import ch.comstock.hivecontroller.channels.Channel;
import ch.comstock.hivecontroller.channels.GPIOchannelDirection;
import ch.comstock.hivecontroller.channels.GPIOchannelOut;
import ch.comstock.hivecontroller.utils.Topics;

public abstract class Initiator {

	/**
	 * .
	 * @param conf The global Configfile
	 * @return HashMap of the channels
	 */
	public static HashMap<String,Channel> createMap( Config conf, GpioController gpioctrl) {
		Logger.trace("Starting with the initialisation of Channels");
		HashMap<String,Channel> channels = new HashMap<>();
		for(Config channel:conf.getConfigList("channels")) {
			Logger.trace(channel.toString());
			String module = "";
			
			if(channel.hasPath("module")) {
				module = channel.getString("module");
			}else {
				if(channel.hasPath("name")) {
					Logger.warn("Channel with name {} has no module defined. Skipped", channel.getString("name"));
				}else {
					Logger.warn("Channel without specified Name nor Module skipped");
				}
				break;
			}
			
			String name = channel.getString("name");
			String cmdTopic;
			String valTopic;
			
			if(channel.hasPath("valueTopic")) {
				valTopic = channel.getString("valueTopic");
			}else {
				valTopic = Topics.getValTopic(conf, name);
			}
			
			if(channel.hasPath("cmdTopic")) {
				cmdTopic = channel.getString("cmdTopic");
			}else {
				cmdTopic = Topics.getCmdTopic(conf, name);
			}
			
			Channel chan = null;
			
			switch(module) {
				case "gpio":
					if(gpioctrl!=null) {
						int gpio = channel.getInt("gpio");
						GPIOchannelDirection direction = GPIOchannelDirection.OUT;
						
						if(channel.hasPath("direction")) {
							if(channel.getString("direction").equalsIgnoreCase("in")) {
								direction = GPIOchannelDirection.IN;
							}else if(!channel.getString("direction").equalsIgnoreCase("out")){
								Logger.warn("Direction for channel {} must be 'in' or 'out'");
							}
						}
						
						if(direction == GPIOchannelDirection.OUT) {
							if(channel.hasPath("value")) {
								chan = new GPIOchannelOut(name, valTopic, cmdTopic, gpio,channel.getBoolean("value"), gpioctrl);
							}else {
								chan = new GPIOchannelOut(name, valTopic, cmdTopic, gpio,gpioctrl);
							}
						}else {
							if(channel.hasPath("value")) {
								//chan = new GPIOchannelOut(name, valTopic, cmdTopic, gpio,channel.getBoolean("value"), gpioctrl);
							}else {
								//chan = new GPIOchannelOut(name, valTopic, cmdTopic, gpio,gpioctrl);
							}
						}
					}else {
						Logger.warn("Pi4J not supported on this platform or not installed correctly.\n Skipping channel {}",name);
					}
			}
			if(chan!=null) {
				channels.put(cmdTopic, chan);
				Logger.debug("added Channel: \n" +chan.toString());
			}
		}
		return channels;
	}
	
	/**
	 * Creates a hashmap that contains the channels and calls their Subscribe-Method
	 * @param conf The global Configfile
	 * @param outMsg The Messagequeue to the MQTTClient
	 * @return
	 */
	public static HashMap<String,Channel> createMapSubscribe( Config conf, LinkedBlockingQueue<Message> outMsg, GpioController gpioctrl) {
		HashMap<String, Channel> channels = createMap(conf, gpioctrl);
		Set<String> keys = channels.keySet();
		for(String key : keys) {
			Channel channel = channels.get(key);
			channel.subscribe(outMsg);
		}
		return channels;
	}
}
