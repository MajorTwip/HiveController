package ch.comstock.hivecontroller.channels;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.RaspiPin;

public class GPIOchannelIn extends GPIOchannel{
	GpioPinDigitalInput gpio;
	
	public GPIOchannelIn(String name,String valueTopic, String cmdTopic,int gpio, GpioController gpioctrl) {
		super(cmdTopic, cmdTopic, cmdTopic, gpio, false, GPIOchannelDirection.OUT);
		initGpio(gpioctrl);
	}
	
	
	public GPIOchannelIn(Channel chan, int gpionr, GpioController gpioctrl) {
		super(chan,gpionr,false,GPIOchannelDirection.IN);
		initGpio(gpioctrl);
	}
	
	private void initGpio(GpioController gpioctrl) {
		try {
			gpio = gpioctrl.provisionDigitalInputPin(RaspiPin.getPinByAddress(getGPIO()),getName());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean get() {
		boolean value = gpio.isHigh();
		super.set(value);
		return value;
	}
	
	public void addListener(GPIOStateChangeHandler callback) {
		
	}
	
	
}
