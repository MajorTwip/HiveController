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
	LinkedList<Message> msgForMQTT;

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
		msgForMQTT = new LinkedList<Message>();
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
	
	private void initEngine() {
		Thread engine = new Thread(new Engine(msgFromMQTT,msgForMQTT));
		engine.start();
	}
	
	private void loadConf() {
		Logger.info("Load Config");
		this.conf = ConfigFactory.load();
		//implement checks
		Logger.trace("Config loaded");
	}

}
