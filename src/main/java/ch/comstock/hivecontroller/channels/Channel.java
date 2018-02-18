package ch.comstock.hivecontroller.channels;

public class Channel {
	private String valueTopic;
	private String cmdTopic;
	
	public Channel(String valueTopic, String cmdTopic) {
		this.valueTopic = valueTopic;
		this.cmdTopic = cmdTopic;
	}
	
	/**
	 * @return the valueTopic
	 */
	public String getValueTopic() {
		return valueTopic;
	}
	/**
	 * @param valueTopic the valueTopic to set
	 */
	public void setValueTopic(String valueTopic) {
		this.valueTopic = valueTopic;
	}
	/**
	 * @return the cmdTopic
	 */
	public String getCmdTopic() {
		return cmdTopic;
	}
	/**
	 * @param cmdTopic the cmdTopic to set
	 */
	public void setCmdTopic(String cmdTopic) {
		this.cmdTopic = cmdTopic;
	}

}
