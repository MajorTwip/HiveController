package ch.comstock.hivecontroller.channels;

public class GPIOchannel extends Channel{
	private GPIOchannelDirection direction;
	private boolean value;

	public GPIOchannel(String valueTopic, String cmdTopic, GPIOchannelDirection direction) {
		super(valueTopic,cmdTopic);
		this.direction = direction;
	}
	
	public GPIOchannel(String valueTopic, String cmdTopic, GPIOchannelDirection direction, boolean value) {
		super(valueTopic,cmdTopic);
		this.direction = direction;
		this.set(value);
	}

	/**
	 * @return the value
	 */
	public boolean get() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void set(boolean value) {
		this.value = value;
	}
	
	public GPIOchannelDirection getDirection() {
		return direction;
	}
	
}
