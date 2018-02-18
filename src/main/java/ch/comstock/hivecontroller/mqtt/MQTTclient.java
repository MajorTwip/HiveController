package ch.comstock.hivecontroller.mqtt;
import java.util.LinkedList;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.pmw.tinylog.Logger;

import com.typesafe.config.*;
import com.typesafe.config.ConfigException;


public class MQTTclient{
	String topicBase = "/";
	int qos = 2;
	String broker;
	String clientId = "NoConf_HiveController";
    MemoryPersistence persistence = new MemoryPersistence();
	MqttClient mqttClient;
	MQTTHandler handler;
	
	public MQTTclient(Config conf, boolean autostart, LinkedList<Message> msgList) throws ConfigException,MqttException{
		handler = new MQTTHandler(msgList);
		if(autostart) {
			setConfig(conf);
		}
        this.mqttClient = new MqttClient(this.broker, this.clientId, this.persistence);
        mqttClient.setCallback(handler);
        if(autostart) {
        	MqttConnectOptions opts = setMQTTParams();
        	connect(opts);
        	mqttClient.subscribe("#");
        }
	}
	
	public void connect(MqttConnectOptions opts) throws MqttException{
		Logger.info("Connecting to broker: "+broker);
        mqttClient.connect(opts);
        Logger.info("Connected");
        Logger.debug("Publishing message");
        MqttMessage message = new MqttMessage("Test".getBytes());
        message.setQos(qos);
        mqttClient.publish(topicBase, message);
        Logger.debug("Message published");
	}
	
	
	public MqttConnectOptions setMQTTParams() {
		Logger.debug("setMQTTParams");
		MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
		Logger.trace("ended");
        return connOpts;
	}
	
	public void setConfig(Config conf) throws ConfigException {
		Logger.trace("setConfig");

		if(conf.hasPath("mqtt.broker")) {
			this.broker = conf.getString("mqtt.broker");
		}else {
			throw new ConfigException.Missing("mqtt.broker");
		}
		
		if(conf.hasPath("mqtt.topicBase")) {
			this.topicBase = conf.getString("mqtt.topicBase");
		}
		if(conf.hasPath("mqtt.clientId")) {
			this.clientId = conf.getString("mqtt.clientId");
		}
		if(conf.hasPath("mqtt.qos")) {
			this.qos = conf.getInt("mqtt.qos");
		}
		Logger.trace("setConfig ended");

	}
}
