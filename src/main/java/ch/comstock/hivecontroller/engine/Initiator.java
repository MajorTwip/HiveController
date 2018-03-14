package ch.comstock.hivecontroller.engine;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.pmw.tinylog.Logger;

import com.typesafe.config.Config;

import ch.comstock.hivecontroller.channels.Channel;
import ch.comstock.hivecontroller.channels.GPIOchannel;
import ch.comstock.hivecontroller.channels.GPIOchannelDirection;
import ch.comstock.hivecontroller.utils.Topics;

public abstract class Initiator {

	public static HashMap<String,Channel> createMap( Config conf) {
		Logger.trace("Starting with the initialisation of Channels");
		HashMap<String,Channel> channels = new HashMap<>();
		for(Config channel:conf.getConfigList("channels")) {
			Logger.trace(channel.toString());
			
			String module = channel.getString("module");
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
					int gpio = channel.getInt("gpio");
					GPIOchannelDirection direction = GPIOchannelDirection.OUT;
					
					if(channel.hasPath("direction")) {
						if(channel.getString("direction").equalsIgnoreCase("in")) {
							direction = GPIOchannelDirection.IN;
						}else if(!channel.getString("direction").equalsIgnoreCase("out")){
							Logger.warn("Direction for channel {} must be 'in' or 'out'");
						}
					}
					
					if(channel.hasPath("value")) {
						chan = new GPIOchannel(name, valTopic, cmdTopic, direction,gpio,channel.getBoolean("value"));
					}else {
						chan = new GPIOchannel(name, valTopic, cmdTopic, direction,gpio);
					}
					
			}
			if(chan!=null) {
				channels.put(name, chan);
				Logger.debug("added Channel: \n" +chan.toString());
			}
		}
		return channels;
	}
	public static HashMap<String,Channel> createMapSubscribe( Config conf, LinkedBlockingQueue<Message> outMsg) {
		HashMap<String, Channel> channels = createMap(conf);
		Set<String> keys = channels.keySet();
		for(String key : keys) {
			Channel channel = channels.get(key);
			channel.subscribe(outMsg);
		}
		return channels;
	}
}
