package ch.comstock.hivecontroller.engine;

import java.util.HashMap;

import org.pmw.tinylog.Logger;
import ch.comstock.hivecontroller.channels.Channel;

public abstract class Handler {
	public static void handle(Message msg, Engine eng) {
		HashMap<String,Channel> channels = eng.getChannels();
		Channel chan = null;
		synchronized(channels) {
			if(channels.containsKey(msg.getTarget())) {
				chan = channels.get(msg.getTarget());
			}
			else {
				Logger.warn("Received Message for unknown Channel {}",msg.getTarget());
			}
		}
		if(chan!=null) {
			switch (msg.getType()) {
				case SET:
					chan.set(msg.getValue());
					break;
					
				case GET:
					eng.sendMsg(chan.getValueTopic(), chan.get().toString());
					break;
	
				default:
					break;
				}
		}


	}
}
