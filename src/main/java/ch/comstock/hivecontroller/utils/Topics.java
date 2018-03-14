package ch.comstock.hivecontroller.utils;

import com.typesafe.config.Config;
/**
 * Abstract methods to prepare/modify Topic-Strings
 * @author MajorTwip
 *
 */
public abstract class Topics {
	/**
	 * Prepares the Valuetopic if no specific is defined
	 * @param conf The global Config
	 * @param name The Channelname
	 * @return Valuetopic
	 */
	public static String getValTopic(Config conf, String name) {
		String topic;
		topic = preparePrefix(conf.getString("mqtt.topicBase"));
		topic = topic + name;
		topic = topic + prepareSuffix(conf.getString("mqtt.defaultValueSuffix"));
		return topic;
	}
	
	/**
	 * Prepares the Commandtopic if no specific is defined
	 * @param conf The global Config
	 * @param name The Channelname
	 * @return Commandtopic
	 */
	public static String getCmdTopic(Config conf, String name) {
		String topic;
		topic = preparePrefix(conf.getString("mqtt.topicBase"));
		topic = topic + name;
		topic = topic + prepareSuffix(conf.getString("mqtt.defaultCmdSuffix"));
		return topic;
	}
	/**
	 * Preposes a "/" if not allready there
	 * 
	 * @param suffix Suffix to check
	 * @return corrected Suffix
	 */
	public static String prepareSuffix(String suffix) {
		String newSuffix;
		suffix = suffix.trim();
		if(suffix.charAt(0)!='/') {
			newSuffix =  "/" + suffix;
		}else {
			newSuffix = suffix;
		}
		return newSuffix;
	}
	/**
	 * Adds a trailing "/" if not already there
	 * 
	 * @param prefix Prefix to check
	 * @return corrected Prefix
	 */
	public static String preparePrefix(String prefix) {
		String newPrefix;
		prefix = prefix.trim();
		if(prefix.charAt(prefix.length()-1)!='/') {
			newPrefix =  prefix + "/";
		}else {
			newPrefix = prefix;
		}
		return newPrefix;
	}
	
}
