package ch.comstock.hivecontroller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.pmw.tinylog.Logger;

import com.google.common.collect.Collections2;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import ch.comstock.hivecontroller.engine.Engine;
import ch.comstock.hivecontroller.engine.Message;
import ch.comstock.hivecontroller.mqtt.MQTTclient;
/***
 * 
 * @author MajorTwip
 * @version 0.0.1
 * 
 * Mainclass for the HiveController
 *
 */
public class HiveController {
	private Config conf;
	LinkedBlockingQueue<Message> msgFromMQTT;
	LinkedBlockingQueue<Message> msgForMQTT;

	/***
	 * main method. Entrypoint for the hole application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Logger.info("Starting");
		Logger.trace("Instanciating Hivecontroller");
		try {
			new HiveController();	
		}catch(ConfigException e) {
			Logger.error(e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Constructor for the main Class
	 * instanciates 2 BlockingQueues to pass messages from/to the MQTT-Handler
	 * 
	 * @throws ConfigException
	 */
	private HiveController() throws ConfigException{
		msgFromMQTT = new LinkedBlockingQueue<Message>();
		msgForMQTT = new LinkedBlockingQueue<Message>();
		loadConf();
		initMqtt();
		initEngine();
	}
	
	private void initMqtt() throws ConfigException{
		try {
			new MQTTclient(conf, msgFromMQTT, msgForMQTT);
		}catch(MqttException e) {
			Logger.error(e.getMessage());
		}
	}
	
	/**
	 * Starts the Engine, the part that coordinates the modules
	 */
	private void initEngine() {
		Thread engine = new Thread(new Engine(msgFromMQTT,msgForMQTT, conf));
		engine.start();
	}
	
	
	/**
	 * loads the config file.
	 * Uses the class com.typesafe.config
	 * 
	 * ToDo: implement config checks
	 */
	private void loadConf() {
		Logger.info("Load Config");
		this.conf = ConfigFactory.load();
		//implement checks
		Logger.trace("Config loaded");
	}

}
