package ch.comstock.hivecontroller.mqtt;
import javax.naming.ConfigurationException;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.typesafe.config.*;
import com.typesafe.config.ConfigException;


public class MQTTclient{
	String topicBase = "/";
	int qos = 2;
	String broker;
	String clientId = "NoConf_HiveController";
    MemoryPersistence persistence = new MemoryPersistence();
	MqttClient mqttClient;
	
	public MQTTclient(Config conf, boolean autostart) throws ConfigException,MqttException{
		if(autostart) {
			setConfig(conf);
        	System.out.println(broker);
		}
        this.mqttClient = new MqttClient(this.broker, this.clientId, this.persistence);
        if(autostart) {
        	MqttConnectOptions opts = setMQTTParams();
        	connect(opts);
        }
	}
	
	public void connect(MqttConnectOptions opts) throws MqttException{
        System.out.println("Connecting to broker: "+broker);
        mqttClient.connect(opts);
        System.out.println("Connected");
        System.out.println("Publishing message");
        MqttMessage message = new MqttMessage("Test".getBytes());
        message.setQos(qos);
        mqttClient.publish(topicBase, message);
        System.out.println("Message published");
        mqttClient.disconnect();
        System.out.println("Disconnected");
        System.exit(0);
	}
	
	
	public MqttConnectOptions setMQTTParams() {
		System.out.println("setMQTTParams");
		MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
		System.out.println("ended");
        return connOpts;
	}
	
	public void setConfig(Config conf) throws ConfigException {
		System.out.println("setConfig");

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
		System.out.println("setConfig ended");

	}
}
