package ch.comstock.hivecontroller.utils;

import org.pmw.tinylog.Logger;

public abstract class Conversions {
	public static boolean str2bool(String str) {
		String val = str.split(" ")[0].trim();
		if(val.equalsIgnoreCase("true")
				|| val.equalsIgnoreCase("on")
				|| val.equals("1"))
		{
			return true;
		}
		if(val.equalsIgnoreCase("false")
				|| val.equalsIgnoreCase("off")
				|| val.equals("0"))
		{
			return false;
		}
		Logger.warn("Could not convert {} to Boolean, returned false",str);
		return false;
	}
}
