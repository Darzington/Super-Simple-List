package com.mygdx.supersimplelist;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.supersimplelist.SimpleList.StringBoolean;

public abstract class GeneralList <T> extends Actor {

	private String listName;
	private ArrayList<T> list;
	
	private transient Table table, wholeTable, header;
	protected transient Skin skin;
	private transient TextButton tempHolding, latestOver;
	
	public GeneralList() {
		this("New List", new ArrayList<T>());
	}
	
	public GeneralList(String listName, ArrayList<T> list) {
		this.listName = listName;
		this.list = list;
		table = new Table();
		wholeTable = new Table();
		setupTableStyles();
		setUpHeader();
		
		refreshTable();
	}
	
	private void setUpHeader() {
		header = new Table(skin);

		header.add(makeBackButton()).center().padLeft(5).width(MasterList.buttonSize).height(MasterList.buttonSize);
		header.add(new Label(listName, UISkin.skin)).center().expandX();
		header.add(makeSettingsButton()).center().padRight(5).width(MasterList.buttonSize).height(MasterList.buttonSize);
		
		header.setBackground("list");
		header.setBounds(0, Gdx.graphics.getHeight() - MasterList.headerHeight, Gdx.graphics.getWidth(), MasterList.headerHeight);
	}
	
	private void setupTableStyles() {
		table.defaults().center().width(Gdx.graphics.getWidth()-20);
		wholeTable.defaults().center();
		skin = UISkin.skin;
		wholeTable.setSkin(skin);
		wholeTable.setBackground("list");
	}
	
	protected void addToListFirst(T newEntry) {
		list.add(0, newEntry);
		refreshTable();
	}
	
	private void addToListAtIndex(T newEntry, int index) {
		list.add(index, newEntry);
		refreshTable();
	}
	
	private void removeFromList(T removedEntry) {
		list.remove(removedEntry);
		refreshTable();
	}
	
	protected void moveToTop(T entry) {
		list.remove(entry);
		list.add(0, entry);
		refreshTable();
	}
	
	protected void moveToBottom(T entry) {
		list.remove(entry);
		list.add(entry);
		refreshTable();
	}
	
	public void addToStage(Stage stage) {
		stage.addActor(header);
		stage.addActor(wholeTable);
	}
	
	public void draw (Batch batch, float parentAlpha) {
		header.draw(batch, parentAlpha);
		wholeTable.draw(batch, parentAlpha);
		if (tempHolding != null) {
			tempHolding.draw(batch, parentAlpha);
		}
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	private boolean tryToAddIntoListBefore(T entry, String addBeforeThis) {
		int desiredIndex = getEntryIndex(addBeforeThis);
		if (desiredIndex >= 0) {
			if (desiredIndex == list.size()-1) {
				addToListAtIndex(entry, list.size());
			}
			else {
				addToListAtIndex(entry, desiredIndex);
			}
			return true;
		}
		return false;
	}
	
	private int getEntryIndex(String entry) {
		for (int i = 0; i < list.size(); i++) {
			if (isThisEntry(list.get(i), entry)) {
				return i;
			}
		}
		return -1;
	}

	protected abstract Button makeBackButton();
	protected abstract Button makeSettingsButton();
	protected abstract TextButton makeSpecificItemButton(T entry);
	protected abstract T createNewEntry(String entry);
	protected abstract boolean isThisEntry(T value, String entry);
	protected abstract String getNewEntryGenericName();
	protected abstract void doTapBehavior(TextButton button, T entry);
	
	protected TextButton makeItemButton(T entry) {
		final TextButton itemButton = makeSpecificItemButton(entry);
		itemButton.addListener(new ActorGestureListener() {
			
			public boolean longPress (Actor actor, float x, float y) {
				removeFromList(entry);
				return false;
			}
			
			@Override
			public void tap (InputEvent event, float x, float y, int count, int button) {
				doTapBehavior(itemButton, entry);
			}
			
			@Override
			public void pan (InputEvent event, float x, float y, float deltaX, float deltaY) {
				if (tempHolding == null || !tempHolding.equals(itemButton)) {
					tempHolding = itemButton;
					itemButton.getParent().removeActor(itemButton, false);
					event.getStage().addActor((Actor)itemButton);
					itemButton.setTouchable(Touchable.disabled);
				}
				float holdingY = Gdx.graphics.getHeight() - Gdx.input.getY() - itemButton.getHeight()/2f;
				itemButton.setPosition(10, holdingY);
			}
			
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if (tempHolding != null && tempHolding.equals(itemButton)) {
					removeFromList(entry);
					if (!tryToAddIntoListBefore(entry, latestOver.getText().toString())) {
						System.out.println("fail fast");
						addToListFirst(entry);
					}
					tempHolding.remove();
					tempHolding = null;
				}
			}
			
		});
		itemButton.addListener(new ClickListener() {
		
			@Override
			public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				if (tempHolding != null && latestOver != null && latestOver != itemButton)
				{
					latestOver.padTop(0);					
					itemButton.padTop(itemButton.getHeight());
					table.invalidateHierarchy();
					wholeTable.invalidateHierarchy();
				}
				latestOver = itemButton;
			}
			
		});
		return itemButton;
	}
	
	private TextField makeNewItemField() {
		TextField newField = new TextField("", UISkin.skin);
    	newField.setMessageText("+ Add New " + getNewEntryGenericName());
		newField.addListener(new InputListener() {
		
		    @Override
			public boolean keyTyped (InputEvent event, char character) {
		    	if (character == '\n' || character == '\r') {
		    		addToListFirst(createNewEntry(newField.getText()));
		    		return true;
		    	}
				return false;
			}
		});
		
		return newField;
	}
	
	private void refreshTable() {
		table.clear();		
		Iterator<T> newContents = list.iterator();
		while(newContents.hasNext()) {
			T entry = newContents.next();
			table.add(makeItemButton(entry)).expandX();
			table.row();
		}		
		table.setWidth(Gdx.graphics.getWidth());
		
		wholeTable.clear();
		
		Table scrollTable = new Table();		
		scrollTable.add(makeNewItemField()).width(Gdx.graphics.getWidth()-10);
		scrollTable.row();
		scrollTable.add(table);
		scrollTable.row();
		
		ScrollPane listScroll = new ScrollPane(scrollTable, skin, "no-bg");
		listScroll.setScrollingDisabled(true, false);
		listScroll.setWidth(Gdx.graphics.getWidth());
		listScroll.setHeight(Gdx.graphics.getHeight() - MasterList.headerHeight);
		wholeTable.add(listScroll);
		
		wholeTable.setWidth(Gdx.graphics.getWidth());
		wholeTable.setHeight(Gdx.graphics.getHeight() - MasterList.headerHeight);
		wholeTable.align(Align.top);
	}
	
}
