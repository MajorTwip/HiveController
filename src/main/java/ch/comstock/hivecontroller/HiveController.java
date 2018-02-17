package ch.comstock.hivecontroller;

import java.util.LinkedList;

import org.eclipse.paho.client.mqttv3.MqttException;

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

	/***
	 * main method. Entrypoint for the hole application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting");
		System.out.println("Instanciating Hivecontroller");
		try {
			new HiveController();	
		}catch(ConfigException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	
	private HiveController() throws ConfigException{
		msgFromMQTT = new LinkedList<Message>();
		loadConf();
		initMqtt();
		initEngine();
	}
	
	private void initMqtt() throws ConfigException{
		try {
			new MQTTclient(conf, true, msgFromMQTT);
		}catch(MqttException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void initEngine() {
		Thread engine = new Thread(new Engine(msgFromMQTT));
		engine.start();
	}
	
	private void loadConf() {
		System.out.println("Load Config");
		this.conf = ConfigFactory.load();
		//implement checks
		System.out.println("Config loaded");
	}

}
