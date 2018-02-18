package ch.comstock.hivecontroller.mqtt;

import java.util.LinkedList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTHandler implements MqttCallback {
	private LinkedList<Message> msgList;
	
	public MQTTHandler(LinkedList<Message> msgList) {
		this.msgList = msgList;
	}

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageArrived(String topic, MqttMessage msg) throws Exception {
		Message newMsg = new Message(MsgType.IN, topic, msg.toString());
		synchronized(msgList) {
			msgList.addLast(newMsg);
			msgList.notifyAll();
		}
	}

}
