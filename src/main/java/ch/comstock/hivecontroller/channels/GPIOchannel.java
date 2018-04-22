package ch.comstock.hivecontroller.channels;

import ch.comstock.hivecontroller.utils.Conversions;

public class GPIOchannel extends Channel{
	private GPIOchannelDirection direction;
	private int gpionr;
	private boolean value;

	public GPIOchannel(String name,String valueTopic, String cmdTopic,int gpionr, GPIOchannelDirection dir) {
		super(name,valueTopic,cmdTopic,"gpio");
		this.gpionr = gpionr;
		this.value = false;
		this.direction = dir;
	}
	
	public GPIOchannel(String name, String valueTopic, String cmdTopic, int gpionr, boolean value, GPIOchannelDirection dir) {
		super(name,valueTopic,cmdTopic,"gpio");
		this.value = value;
		this.gpionr = gpionr;
		this.direction = dir;
	}
	
	public GPIOchannel(Channel chan, int gpionr, boolean value, GPIOchannelDirection dir) {
		super(chan);
		this.gpionr = gpionr;
		this.value = value;	
		this.direction = dir;
	}
	
	@Override
	public String toString() {
		String string = super.toString() + "\n" +
						"GPIO Nr: " +gpionr+ "\n" +
						"Value: "  + value;
		
		return string;
	}

	/**
	 * @return the value
	 */
	public String get() {
		return Boolean.toString(value);
	}
	
	public void set(String value) {
		this.value=Conversions.str2bool(value);
	}
	
	public void set(Boolean value) {
		this.value=value;
	}

	
	public GPIOchannelDirection getDirection() {
		return direction;
	}

	/**
	 * @return the gpio
	 */
	public int getGPIO() {
		return gpionr;
	}	
}
