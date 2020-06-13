package com.mygdx.supersimplelist;

public class StringBoolean {
	private String entry;
	private boolean isChecked;

	public StringBoolean() {
		this("", true);
	}

	public StringBoolean(String entry, boolean isChecked) {
		this.entry = entry;
		this.isChecked = isChecked;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}
