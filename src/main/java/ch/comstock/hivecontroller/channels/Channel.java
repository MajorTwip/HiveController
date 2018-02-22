package ch.comstock.hivecontroller.channels;

public class Channel {
	private String name;
	private String valueTopic;
	private String cmdTopic;
	
	public Channel(String name, String valueTopic, String cmdTopic) {
		this.setName(name);
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
	@Override
	public String toString() {
		String string = 	"Channel '"+name +"'\n" +
							"valueTopic: " +valueTopic +"\n"+
							"cmdTopic: " +cmdTopic;
		
		return string;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
