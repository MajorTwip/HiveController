package ch.comstock.hivecontroller.mqtt;

public enum MsgType {
	SET, GET, IN, OUT, SUB;
	
	public static MsgType getTypeFromPayload(String payload) {
		MsgType msgType = MsgType.IN;
		if(payload.trim().startsWith("SET")) {
			msgType = MsgType.SET;
		}
		else if(payload.trim().startsWith("GET")) {
			msgType = MsgType.GET;
		}
		return msgType;
	}
	
	public static String stripCmd(String payload) {
		String pl = payload.trim();
		if(pl.startsWith("GET")||pl.startsWith("SET")) {
			pl = pl.substring(3).trim();
		}
		return pl;
	}
	
}
