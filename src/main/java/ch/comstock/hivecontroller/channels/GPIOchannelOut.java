package ch.comstock.hivecontroller.channels;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

import ch.comstock.hivecontroller.utils.Conversions;

public class GPIOchannelOut extends GPIOchannel{
	GpioPinDigitalOutput gpio;
	
	public GPIOchannelOut(String name,String valueTopic, String cmdTopic,int gpionr, GpioController gpioctrl) {
		super(cmdTopic, cmdTopic, cmdTopic, gpionr, false, GPIOchannelDirection.OUT);
		initGpio(gpioctrl);
	}
	
	public GPIOchannelOut(String name,String valueTopic, String cmdTopic,int gpionr, boolean value, GpioController gpioctrl) {
		super(cmdTopic, cmdTopic, cmdTopic, gpionr, value, GPIOchannelDirection.OUT);
		initGpio(gpioctrl);
	}
	
	public GPIOchannelOut(Channel chan, int gpionr, boolean value, GpioController gpioctrl) {
		super(chan,gpionr,value,GPIOchannelDirection.OUT);
		initGpio(gpioctrl);
	}
	
	private void initGpio(GpioController gpioctrl) {
		try {
			gpio = gpioctrl.provisionDigitalOutputPin(RaspiPin.getPinByAddress(getGPIO()),getName());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void set(String value) {
		gpio.setState(Conversions.str2bool(value));
		super.set(value);
	}
}
