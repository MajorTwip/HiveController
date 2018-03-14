package ch.comstock.hivecontroller.mqtt;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.pmw.tinylog.Logger;

import com.typesafe.config.*;
import com.typesafe.config.ConfigException;

import ch.comstock.hivecontroller.engine.Message;
import ch.comstock.hivecontroller.utils.Topics;

/**
 * The Mainclass of the MQTT-Client
 * @author MajorTwip
 *
 */
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

	/**
	 * Instanciates an MQTT-Client
	 * @param conf The global configFile
	 * @param msgList	LinkedBlockingqueue with incomming messages
	 * @param outMsg	{@link LinkedBlockingQueue} with Messages to Send/Subscribe
	 * @throws ConfigException
	 * @throws MqttException
	 */
	public MQTTclient(Config conf, LinkedBlockingQueue<Message> msgList, LinkedBlockingQueue<Message> outMsg) throws ConfigException,MqttException{
		this.handler = new MQTTHandler(msgList);
		subscribeList = new LinkedList<String>();
		setConfig(conf);
        this.mqttClient = new MqttClient(this.broker, this.clientId, this.persistence);
        mqttClient.setCallback(handler);
       	MqttConnectOptions opts = setMQTTParams();
       	connect(opts);
       	this.sender = new Thread(new MQTTsender(this,outMsg, qos));
       	this.sender.start();
	}
	

	

	/**
	 * Connects... no shit Sherlock
	 * @param opts MqttConnectionOptions
	 * @throws MqttException
	 */
	private void connect(MqttConnectOptions opts) throws MqttException{
		Logger.info("Connecting to broker: "+broker);
        mqttClient.connect(opts);
        Logger.info("Connected");
	}
	
	/**
	 * Sets some basic Connection-Settings
	 * 
	 * ToDo: why is there no setAutoReconnect
	 * @return MQTTconnectionOptions
	 */
	private MqttConnectOptions setMQTTParams() {
		Logger.debug("setMQTTParams");
		MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(false);
        return connOpts;
	}
	
	/**
	 *  Sets Instance-Varables from config-File
	 * @param conf The global Configfile
	 * @throws ConfigException 
	 */
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
		}else {
			Logger.warn("mqtt.topicBase is not configured. Using: {}",this.topicBase );
		}
		if(conf.hasPath("mqtt.defaultCmdSuffix")) {
			this.defaultCmdSuffix = conf.getString("mqtt.defaultCmdSuffix");
			Logger.trace("mqtt.defaultCmdSuffix set to: {}",this.defaultCmdSuffix);
		}else {
			Logger.debug("mqtt.defaultCmdSuffix is not configured. Using: {}",this.defaultCmdSuffix);
		}
		if(conf.hasPath("mqtt.defaultValueSuffix")) {
			this.defaultValueSuffix = conf.getString("mqtt.defaultValueSuffix");
			Logger.trace("mqtt.defaultValueSuffix set to: {}",this.defaultValueSuffix);
		}else {
			Logger.debug("mqtt.defaultValueSuffix is not configured. Using: {}",this.defaultValueSuffix);
		}
		if(conf.hasPath("mqtt.clientId")) {
			this.clientId = conf.getString("mqtt.clientId");
			Logger.trace("mqtt.clientId set to: {}",this.clientId );
		}else {
			Logger.debug("mqtt.clientId is not configured. Using: {}",this.clientId );
		}
		if(conf.hasPath("mqtt.qos")) {
			this.qos = conf.getInt("mqtt.qos");
			Logger.trace("mqtt.qos set to: {}",this.qos);
		}

		this.topicBase = Topics.preparePrefix(this.topicBase);
		this.defaultCmdSuffix = Topics.prepareSuffix(this.defaultCmdSuffix);
		this.defaultValueSuffix = Topics.prepareSuffix(this.defaultValueSuffix);

		Logger.trace("setConfig ended");

	}
	/**
	 * Subscribes to a Topic and adds the topic to the list
	 * @param topic
	 */
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
	
	public void sendMQTT(String topic, MqttMessage message) {
		try {
			mqttClient.publish(topic, message);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return List of subscribed Topics
	 */
	public LinkedList<String> getSubscriptions(){
		return subscribeList;
	}


	
	
}
