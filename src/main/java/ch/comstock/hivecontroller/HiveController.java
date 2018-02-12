package ch.comstock.hivecontroller;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

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

	/***
	 * main method. Entrypoint for the hole application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting");
		System.out.println("Instanciating Hivecontroller");
		try {
			HiveController hivecontroller = new HiveController();	
		}catch(ConfigException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	
	private HiveController() throws ConfigException{
		loadConf();
		initMqtt();
	}
	
	private void initMqtt() throws ConfigException{
		try {
			MQTTclient client = new MQTTclient(conf, true);
		}catch(MqttException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void loadConf() {
		System.out.println("Load Config");
		this.conf = ConfigFactory.load();
		//implement checks
		System.out.println("Config loaded");
	}

}
