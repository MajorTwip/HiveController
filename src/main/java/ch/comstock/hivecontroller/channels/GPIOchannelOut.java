package ch.comstock.hivecontroller.channels;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

public class GPIOchannelOut extends GPIOchannel{
	GpioPinDigitalOutput gpio;
	
	public GPIOchannelOut(String name,String valueTopic, String cmdTopic,int gpio, GpioController gpioctrl) {
		super(cmdTopic, cmdTopic, cmdTopic, gpio, false);
		initGpio(gpioctrl);
	}
	
	public GPIOchannelOut(String name,String valueTopic, String cmdTopic,int gpio, boolean value, GpioController gpioctrl) {
		super(cmdTopic, cmdTopic, cmdTopic, gpio, value);
		initGpio(gpioctrl);
	}
	
	private void initGpio(GpioController gpioctrl) {
		try {
			gpio = gpioctrl.provisionDigitalOutputPin(RaspiPin.getPinByAddress(getGPIO()),getName());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void set(boolean value) {
		gpio.setState(value);
		super.set(value);
	}
}
