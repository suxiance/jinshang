package com.order.cc.cmis.form;

import org.apache.struts.validator.ValidatorForm;

public class CmisServerForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	
	private String contact_id ;
	private String call_id ; 
	private String text ;
	private String input_keys ;
	private boolean interrupt ;
	private boolean session_start ;
	public String getContact_id() {
		return contact_id;
	}
	public void setContact_id(String contact_id) {
		this.contact_id = contact_id;
	}
	public String getCall_id() {
		return call_id;
	}
	public void setCall_id(String call_id) {
		this.call_id = call_id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getInput_keys() {
		return input_keys;
	}
	public void setInput_keys(String input_keys) {
		this.input_keys = input_keys;
	}
	public boolean isInterrupt() {
		return interrupt;
	}
	public void setInterrupt(boolean interrupt) {
		this.interrupt = interrupt;
	}
	public boolean isSession_start() {
		return session_start;
	}
	public void setSession_start(boolean session_start) {
		this.session_start = session_start;
	}
	@Override
	public String toString() {
		return "CmisServerForm [contact_id=" + contact_id + ", call_id="
				+ call_id + ", text=" + text + ", input_keys=" + input_keys
				+ ", interrupt=" + interrupt + ", session_start="
				+ session_start + "]";
	}
    
	
}