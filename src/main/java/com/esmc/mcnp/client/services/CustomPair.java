package com.esmc.mcnp.client.services;

public class CustomPair {
	private Integer key;
	private String value;

	public CustomPair() {
	}

	public CustomPair(Integer key, String value) {
		this.key = key;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
