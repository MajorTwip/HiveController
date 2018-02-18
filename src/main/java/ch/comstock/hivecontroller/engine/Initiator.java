package ch.comstock.hivecontroller.engine;

import java.util.HashMap;

import org.pmw.tinylog.Logger;

import com.typesafe.config.Config;

import ch.comstock.hivecontroller.channels.Channel;
import ch.comstock.hivecontroller.channels.GPIOchannel;
import ch.comstock.hivecontroller.channels.GPIOchannelDirection;
import ch.comstock.hivecontroller.utils.Topics;

public abstract class Initiator {

	public static HashMap<String,Channel> createMap( Config conf) {
		Logger.trace("Starting with the initialisation of Channels");
		HashMap<String,Channel> channels = null;
		for(Config channel:conf.getConfigList("channels")) {
			Logger.trace(channel.toString());
			switch(channel.getString("module")) {
				case "gpio":
					GPIOchannel chan = new GPIOchannel(Topics.getValTopic(conf,channel.getString("name")),
							Topics.getCmdTopic(conf,channel.getString("name")), GPIOchannelDirection.IN);
			}
		}
		return channels;
	}
}
