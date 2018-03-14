package ch.comstock.hivecontroller.channels;

public class GPIOchannel extends Channel{
	private GPIOchannelDirection direction;
	private int gpionr;
	private boolean value;

	public GPIOchannel(String name,String valueTopic, String cmdTopic,int gpio) {
		super(name,valueTopic,cmdTopic);
	}
	
	public GPIOchannel(String name, String valueTopic, String cmdTopic, int gpio, boolean value) {
		super(name,valueTopic,cmdTopic);
		this.value = value;
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
