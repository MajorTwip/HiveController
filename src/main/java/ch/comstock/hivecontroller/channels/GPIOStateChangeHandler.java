package ch.comstock.hivecontroller.channels;

import java.util.concurrent.LinkedBlockingQueue;

import org.pmw.tinylog.Logger;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import ch.comstock.hivecontroller.engine.Message;
import ch.comstock.hivecontroller.mqtt.MsgType;

public class GPIOStateChangeHandler implements GpioPinListenerDigital {
	private String valTopic;
	LinkedBlockingQueue<Message> outMsg;
	
	public GPIOStateChangeHandler(String valTopic, LinkedBlockingQueue<Message> outMsg) {
		this.valTopic = valTopic;
		this.outMsg = outMsg;
	}

	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		Message msg = new Message(MsgType.OUT, valTopic, event.getState().toString());
		outMsg.add(msg);
		Logger.trace("Added message to sendingQueue: {}",msg.toString());
	}

}
