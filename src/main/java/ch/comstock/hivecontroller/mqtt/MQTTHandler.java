package ch.comstock.hivecontroller.mqtt;

import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.pmw.tinylog.Logger;

import ch.comstock.hivecontroller.engine.Message;

/**
 * MQTT-Callbacks
 * @author MajorTwip
 *
 */
public class MQTTHandler implements MqttCallback {
	private LinkedBlockingQueue<Message> msgList;
	
	public MQTTHandler(LinkedBlockingQueue<Message> msgList) {
		this.msgList = msgList;
	}

	@Override
	public void connectionLost(Throwable ex) {
		Logger.warn(ex.getMessage());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void messageArrived(String topic, MqttMessage msg) throws Exception {
		Message newMsg = new Message(MsgType.getTypeFromPayload(msg.toString()), topic, MsgType.stripCmd(msg.toString()));
		msgList.add(newMsg);
	}

}
