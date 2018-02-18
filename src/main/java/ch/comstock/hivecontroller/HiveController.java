package ch.comstock.hivecontroller;

import java.util.LinkedList;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.pmw.tinylog.Logger;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import ch.comstock.hivecontroller.engine.Engine;
import ch.comstock.hivecontroller.mqtt.MQTTclient;
import ch.comstock.hivecontroller.mqtt.Message;
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
	LinkedList<Message> msgFromMQTT;
	MQTTclient mqttClient;

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
	
	
	private HiveController() throws ConfigException{
		msgFromMQTT = new LinkedList<Message>();
		loadConf();
		this.mqttClient = initMqtt();
		initEngine(mqttClient);
	}
	
	private MQTTclient initMqtt() throws ConfigException{
		MQTTclient client = null;
		try {
			client = new MQTTclient(conf, msgFromMQTT);
		}catch(MqttException e) {
			Logger.error(e.getMessage());
		}
		return client;
	}
	
	private void initEngine(MQTTclient mqttclient) {
		Thread engine = new Thread(new Engine(msgFromMQTT));
		engine.start();
	}
	
	private void loadConf() {
		Logger.info("Load Config");
		this.conf = ConfigFactory.load();
		//implement checks
		Logger.trace("Config loaded");
	}

}
