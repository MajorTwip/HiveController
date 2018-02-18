package ch.comstock.hivecontroller.utils;

import com.typesafe.config.Config;

public abstract class Topics {
	public static String getValTopic(Config conf, String name) {
		String topic;
		topic = conf.getString("mqtt.topicBase");
		if(topic.charAt(topic.length()-1)!='/') {
			topic = topic + "/";
		}
		topic = topic + name;
		topic = topic + conf.getString("mqtt.defaultValueSuffix");
		return topic;
	}
	
	
	public static String getCmdTopic(Config conf, String name) {
		String topic;
		topic = conf.getString("mqtt.topicBase");
		if(topic.charAt(topic.length()-1)!='/') {
			topic = topic + "/";
		}
		topic = topic + name;
		topic = topic + conf.getString("mqtt.defaultCmdSuffix");
		return topic;
	}
	
}
