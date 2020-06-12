package com.mygdx.supersimplelist;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SimpleList extends GeneralList<SimpleList.StringBoolean>{
	
	public SimpleList() {
		this("New List", new ArrayList<StringBoolean>());
	}

	public SimpleList(String listName, ArrayList<StringBoolean> list) {
		super(listName, list);
		testingStart();
	}
	
	private void testingStart() {
		addToListFirst(new StringBoolean("to do 1", true));
		addToListFirst(new StringBoolean("to do 2", true));
		addToListFirst(new StringBoolean("to do 3", true));
		addToListFirst(new StringBoolean("to do 4", true));
		addToListFirst(new StringBoolean("to do 5", true));
		addToListFirst(new StringBoolean("to do 6", true));
	}
	
	@Override
	protected TextButton makeSpecificItemButton(StringBoolean entry) {
		final CheckBox checkBox = new CheckBox(entry.getEntry(), skin);
		checkBox.left();
		checkBox.setChecked(entry.isChecked());
		return checkBox;
	}
	
	@Override
	protected Button makeBackButton() {
		Button backButton = new TextButton("<", UISkin.skin);
		backButton.addListener(new ClickListener() {
			
		    @Override
			public void clicked (InputEvent event, float x, float y) {
		    	// TODO what is back
			}
		});
		
		return backButton;
	}

	@Override
	protected Button makeSettingsButton() {
		Button settingsButton = new TextButton("?", UISkin.skin);
		settingsButton.addListener(new ClickListener() {
			
		    @Override
			public void clicked (InputEvent event, float x, float y) {
		    	// TODO what is settings
			}
		});
		
		return settingsButton;
	}

	@Override
	protected StringBoolean createNewEntry(String entry) {
		return new StringBoolean(entry, false);
	}

	@Override
	protected String getNewEntryGenericName() {
		return "Item";
	}

	@Override
	protected void doTapBehavior(TextButton button, StringBoolean sb) {
		boolean checked = button.isChecked();
		sb.setChecked(checked);
		
		if (!checked) {
			moveToTop(sb);
		} else {
			moveToBottom(sb);
		}
	}

	@Override
	protected boolean isThisEntry(StringBoolean sb, String entry) {
		return sb.getEntry().equals(entry);
	}
	
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
	
}
