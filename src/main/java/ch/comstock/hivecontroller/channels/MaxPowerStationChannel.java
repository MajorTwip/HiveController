package ch.comstock.hivecontroller.channels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.pmw.tinylog.Logger;

import ch.comstock.hivecontroller.engine.Engine;
import ch.comstock.hivecontroller.engine.Message;
import ch.comstock.hivecontroller.mqtt.MsgType;
import ch.comstock.hivecontroller.utils.MHPSjson;

public class MaxPowerStationChannel extends Channel{
	List<Boolean> states = new ArrayList<>(Arrays.asList(false,false,false,false,false,false));
	List<Float> consumption = new ArrayList<>(Arrays.asList(0.0f,0.0f,0.0f,0.0f,0.0f,0.0f));
	String url = "";
	
	public MaxPowerStationChannel(Channel chan, String ip) {
		super(chan);
		this.url = "http://" + ip;
	}
	
	public void subscribe(LinkedBlockingQueue<Message> msgOut) {
		if(!msgOut.offer(new Message(MsgType.SUB,this.getCmdTopic()))) {
			Logger.warn("Could not add Subscription for channel {} to Sendingqueue",this.getCmdTopic());
		}
		for(int i=1; i<=6; i++) {
			String cmdTopic = String.join("-", this.getCmdTopic(), Integer.toString(i));
			Message msg = new Message(MsgType.SUB,cmdTopic);
			if(!msgOut.offer(msg)) {
				Logger.warn("Could not add Subscription for channel {} to Sendingqueue",cmdTopic);
			}
		}
	}
	
	private void getJSON() {
		consumption = MHPSjson.getConsumption(url);
		states = MHPSjson.getState(url);
	}
	
	public void sendVal(Engine eng) {
		getJSON();
		for(int i=0;i<6;i++) {
			eng.sendMsg(getValueTopic()+"-watt-"+String.valueOf(i+1), String.valueOf(consumption.get(i)));
			eng.sendMsg(getValueTopic()+"-state-"+String.valueOf(i+1), String.valueOf(states.get(i)));
		}
	}
	
	
	
}
