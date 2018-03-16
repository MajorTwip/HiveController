package ch.comstock.hivecontroller.channels;

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
	public boolean get() {
		return value;
	}
	
	protected void set(boolean value) {
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
