package ch.comstock.hivecontroller.mqtt;
import java.util.LinkedList;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.pmw.tinylog.Logger;

import com.typesafe.config.*;
import com.typesafe.config.ConfigException;


public class MQTTclient{
	String topicBase = "/";
	String defaultCmdSuffix = "command";
	String defaultValueSuffix = "value";
	int qos = 2;
	String broker;
	String clientId = "NoConf_HiveController";
    MemoryPersistence persistence = new MemoryPersistence();
    
	MqttClient mqttClient;
	MQTTHandler handler;
	LinkedList<String> subscribeList;
	Thread sender;

	
	public MQTTclient(Config conf, LinkedList<Message> msgList, LinkedList<Message> outMsg) throws ConfigException,MqttException{
		this.handler = new MQTTHandler(msgList);
		subscribeList = new LinkedList<String>();
		setConfig(conf);
        this.mqttClient = new MqttClient(this.broker, this.clientId, this.persistence);
        mqttClient.setCallback(handler);
       	MqttConnectOptions opts = setMQTTParams();
       	connect(opts);
       	subscribeConf(conf);
       	this.sender = new Thread(new MQTTsender(mqttClient,outMsg, qos));
       	this.sender.start();
	}
	

	

	
	private void connect(MqttConnectOptions opts) throws MqttException{
		Logger.info("Connecting to broker: "+broker);
        mqttClient.connect(opts);
        Logger.info("Connected");
	}
	
	
	private MqttConnectOptions setMQTTParams() {
		Logger.debug("setMQTTParams");
		MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        return connOpts;
	}
	
	private void setConfig(Config conf) throws ConfigException {
		Logger.trace("setConfig");

		if(conf.hasPath("mqtt.broker")) {
			this.broker = conf.getString("mqtt.broker");
			Logger.trace("mqtt.broker set to: {}",this.broker );
		}else {
			throw new ConfigException.Missing("mqtt.broker");
		}
		
		if(conf.hasPath("mqtt.topicBase")) {
			this.topicBase = conf.getString("mqtt.topicBase");
			Logger.trace("mqtt.topicBase set to: {}",this.topicBase );
		}
		if(conf.hasPath("mqtt.defaultCmdSuffix")) {
			this.defaultCmdSuffix = conf.getString("mqtt.defaultCmdSuffix");
			Logger.trace("mqtt.defaultCmdSuffix set to: {}",this.defaultCmdSuffix);
		}
		if(conf.hasPath("mqtt.defaultValueSuffix")) {
			this.defaultValueSuffix = conf.getString("mqtt.defaultValueSuffix");
			Logger.trace("mqtt.defaultValueSuffix set to: {}",this.defaultValueSuffix);
		}
		if(conf.hasPath("mqtt.clientId")) {
			this.clientId = conf.getString("mqtt.clientId");
			Logger.trace("mqtt.clientId set to: {}",this.clientId );
		}
		if(conf.hasPath("mqtt.qos")) {
			this.qos = conf.getInt("mqtt.qos");
			Logger.trace("mqtt.qos set to: {}",this.qos);
		}

		
		if(this.topicBase.charAt(this.topicBase.length()-1)!='/') {
			this.topicBase=this.topicBase + "/";
		}
		
		if(this.defaultCmdSuffix.charAt(0)!='/') {
			this.defaultCmdSuffix = "/" + this.defaultCmdSuffix;
		}
		
		if(this.defaultValueSuffix.charAt(0)!='/') {
			this.defaultValueSuffix = "/" + this.defaultValueSuffix;
		}
		Logger.trace("setConfig ended");

	}
	
	private void subscribeConf(Config conf) {
		for(Config channel:conf.getConfigList("channels")) {
			Logger.trace(channel.toString());
			String topic = topicBase + channel.getString("name") + defaultCmdSuffix;
			subscribeMQTT(topic);
		}
	}
	
	public void subscribeMQTT(String topic) {
		Logger.debug("Subscribe to Topic {}", topic);
		subscribeList.add(topic);
		if(mqttClient != null) {
			try {
				mqttClient.subscribe(topic);
			} catch (MqttException e) {
				Logger.warn("Could not subscribe to {}",topic);
				e.printStackTrace();
			}
		}else {
			Logger.warn("Could not subscribe to {}, client not connected", topic);
		}
	}
	
	public LinkedList<String> getSubscriptions(){
		return subscribeList;
	}


	
	
}
