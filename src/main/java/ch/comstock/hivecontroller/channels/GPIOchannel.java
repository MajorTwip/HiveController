package ch.comstock.hivecontroller.channels;

import org.pmw.tinylog.Logger;

public class GPIOchannel extends Channel{
	private GPIOchannelDirection direction;
	private int gpio;
	private boolean value;

	public GPIOchannel(String name,String valueTopic, String cmdTopic, GPIOchannelDirection direction,int gpio) {
		super(name,valueTopic,cmdTopic);
		this.direction = direction;
		this.setGPIO(gpio);
	}
	
	public GPIOchannel(String name, String valueTopic, String cmdTopic, GPIOchannelDirection direction, int gpio, boolean value) {
		super(name,valueTopic,cmdTopic);
		this.direction = direction;
		this.set(value);
		this.setGPIO(gpio);
	}
	
	@Override
	public String toString() {
		String string = super.toString() + "\n" +
						"GPIO Nr: " +gpio+ "\n" +
						"Value: "  + value;
		
		return string;
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

	/**
	 * @return the gpio
	 */
	public int getGPIO() {
		return gpio;
	}

	/**
	 * @param gpio the gpio to set
	 */
	private void setGPIO(int gpio) {
		this.gpio = gpio;
	}
	
}
