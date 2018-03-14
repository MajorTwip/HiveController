package ch.comstock.hivecontroller.engine;

import ch.comstock.hivecontroller.mqtt.MsgType;

public class Message {
	private MsgType msgType;
	private String msgTarget;
	private String msgValue;

	
	public Message(MsgType msgType, String msgTarget, String msgValue) {
		this.setType(msgType);
		this.setTarget(msgTarget);
		this.setValue(msgValue);
	}
	
	public Message(MsgType msgType, String msgTarget) {
		this.setType(msgType);
		this.setTarget(msgTarget);
		this.setValue(null);
	}
	

	/**
	 * @return the msgTarget
	 */
	public String getTarget() {
		return msgTarget;
	}

	/**
	 * @param msgTarget the msgTarget to set
	 */
	public void setTarget(String msgTarget) {
		this.msgTarget = msgTarget;
	}

	/**
	 * @return the msgValue
	 */
	public String getValue() {
		return msgValue;
	}

	/**
	 * @param msgValue the msgValue to set
	 */
	public void setValue(String msgValue) {
		this.msgValue = msgValue;
	}

	/**
	 * @return the msgType
	 */
	public MsgType getType() {
		return msgType;
	}

	/**
	 * @param msgType the msgType to set
	 */
	public void setType(MsgType msgType) {
		this.msgType = msgType;
	}
	
	
	
}
