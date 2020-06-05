package com.mygdx.supersimplelist;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;

public class SimpleList extends Actor{
	
	private String listName;
	private ArrayList<String> list;
	private ArrayList<String> doneList;
	private transient Table table, doneTable, wholeTable, header;
	private transient Skin skin;
	private transient CheckBox tempHolding, latestOver;
	
	public SimpleList() {
		this("New List", new ArrayList<String>(), new ArrayList<String>());
	}

	public SimpleList(String listName, ArrayList<String> list, ArrayList<String> donelist) {
		this.listName = listName;
		this.list = list;
		this.doneList = donelist;
		table = new Table();
		doneTable = new Table();
		wholeTable = new Table();
		setupTableStyles();
		setUpHeader();
		
		testingStart();
		
		refreshBothTables();
	}
	
	private void setUpHeader() {
		header = new Table(skin);

		header.add(makeBackButton()).center().padLeft(5).width(MasterList.buttonSize).height(MasterList.buttonSize);
		header.add(new Label(listName, UISkin.skin)).center().expandX();
		header.add(makeSettingsButton()).center().padRight(5).width(MasterList.buttonSize).height(MasterList.buttonSize);
		
		header.setBackground("list");
		header.setBounds(0, Gdx.graphics.getHeight() - MasterList.headerHeight, Gdx.graphics.getWidth(), MasterList.headerHeight);
	}
	
	private void testingStart() {
		addToListFirst(list, "to do 1");
		addToListFirst(list, "to do 2");
		addToListFirst(list, "to do 3");
		addToListFirst(list, "to do 4");
		addToListFirst(list, "to do 5");
		addToListFirst(list, "to do 6");
		addToListFirst(list, "to do 7");
		addToListFirst(list, "to do 8");
		addToListFirst(list, "to do 9");
		addToListFirst(doneList, "done 1");
		addToListFirst(doneList, "done 2");
		addToListFirst(doneList, "done 3");
		addToListFirst(doneList, "done 4");
	}
	
	private void setupTableStyles() {
		// TODO use VerticalGroups instead of subTables to regulate spacing between items?
		table.defaults().center().width(Gdx.graphics.getWidth()-20);
		doneTable.defaults().center().width(Gdx.graphics.getWidth()-20);
		wholeTable.defaults().center();
		skin = UISkin.skin;
		wholeTable.setSkin(skin);
		wholeTable.setBackground("list");
	}
	
	public void addToListFirst(ArrayList<String> list, String newEntry) {
		list.add(0, newEntry);
		refreshBothTables();
	}
	
	public void addToListAtIndex(ArrayList<String> list, String newEntry, int index) {
		list.add(index, newEntry);
		refreshBothTables();
	}
	
	public void removeFromList(ArrayList<String> list, ArrayList<String> listToSendTo, String removedEntry) {
		list.remove(removedEntry);
		if (listToSendTo != null) {
			addToListFirst(listToSendTo, removedEntry);
		}
		refreshBothTables();
	}
	
	private void refreshBothTables() {
		refreshTable(table, list, doneList, false);
		refreshTable(doneTable, doneList, list, true);
		if (!doneTable.isVisible()) {
			doneTable.clear();
		}
		
		wholeTable.clear();
		
		Table scrollTable = new Table();		
		scrollTable.add(makeNewItemField()).width(Gdx.graphics.getWidth()-10);
		scrollTable.row();
		scrollTable.add(table);
		scrollTable.row();
		scrollTable.add(makeListHeaderButton("Hidden items")).padTop(4);
		scrollTable.row();
		scrollTable.add(doneTable);	
		
		ScrollPane listScroll = new ScrollPane(scrollTable, skin, "no-bg");
		listScroll.setScrollingDisabled(true, false);
		listScroll.setWidth(Gdx.graphics.getWidth());
		listScroll.setHeight(Gdx.graphics.getHeight() - MasterList.headerHeight);
		wholeTable.add(listScroll);
		
		wholeTable.setWidth(Gdx.graphics.getWidth());
		wholeTable.setHeight(Gdx.graphics.getHeight() - MasterList.headerHeight);
		wholeTable.align(Align.top);
	}
	
	private void refreshTable(Table toRefresh, ArrayList<String> currentList, ArrayList<String> listToSendTo, boolean isChecked) {
		toRefresh.clear();
		
		Iterator<String> newContents = currentList.iterator();
		while(newContents.hasNext()) {
			String entry = newContents.next();
			toRefresh.add(makeItemButton(entry, currentList, listToSendTo, isChecked)).expandX();
			toRefresh.row();
		}
		
		toRefresh.setWidth(Gdx.graphics.getWidth());
	}
	
	private TextButton makeItemButton(String entry, ArrayList<String> currentList, ArrayList<String> listToSendTo, boolean isChecked) {
		final CheckBox cb = new CheckBox(entry, skin);
		cb.left();
		cb.setChecked(isChecked);
		cb.addListener(new ActorGestureListener() {
			
			public boolean longPress (Actor actor, float x, float y) {
				removeFromList(currentList, null, entry);
				return false;
			}
			
			@Override
			public void tap (InputEvent event, float x, float y, int count, int button) {
				removeFromList(currentList, listToSendTo, entry);
			}
			
			@Override
			public void pan (InputEvent event, float x, float y, float deltaX, float deltaY) {
				if (tempHolding == null || !tempHolding.equals(cb)) {
					tempHolding = cb;
					cb.getParent().removeActor(cb, false);
					event.getStage().addActor((Actor)cb);
					cb.setTouchable(Touchable.disabled);
				}
				float holdingY = Gdx.graphics.getHeight() - Gdx.input.getY() - cb.getHeight()/2f;
				cb.setPosition(10, holdingY);
			}
			
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if (tempHolding != null && tempHolding.equals(cb)) {
					removeFromList(currentList, null, entry);
					if (!tryToAddIntoListBefore(entry, latestOver.getText().toString(), list)) {
						if (!tryToAddIntoListBefore(entry, latestOver.getText().toString(), doneList)) {
							System.out.println("fail fast");
							addToListFirst(currentList, entry);
						}
					}
					tempHolding.remove();
					tempHolding = null;
				}
			}
			
		});
		cb.addListener(new ClickListener() {
		
			@Override
			public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				if (tempHolding != null && latestOver != null && latestOver != cb)
				{
					latestOver.padTop(0);					
					cb.padTop(cb.getHeight());
					table.invalidateHierarchy();
					doneTable.invalidateHierarchy();
					wholeTable.invalidateHierarchy();
				}
				latestOver = cb;
			}
			
		});
		return cb;
	}
	
	private TextButton makeListHeaderButton(String text) {
		final TextButton butt = new TextButton(text, skin, "emphasis-colored");
		butt.addListener(new InputListener(){
		    @Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		    	boolean isDoneTableVisible = doneTable.isVisible();
		    	doneTable.setVisible(!isDoneTableVisible);
		    	if (isDoneTableVisible) {
		    		doneTable.clear();
		    	} else {
		    		refreshBothTables();
		    	}
				return true;
		    }
		});
		return butt;
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
	
	public boolean tryToAddIntoListBefore(String entry, String addBeforeThis, ArrayList<String> addToThisList) {
		int desiredIndex = addToThisList.indexOf(addBeforeThis);
		if (desiredIndex >= 0) {
			if (desiredIndex == addToThisList.size()-1) {
				addToListAtIndex(addToThisList, entry, addToThisList.size());
			}
			else {
				addToListAtIndex(addToThisList, entry, desiredIndex);
			}
			return true;
		}
		return false;
	}
	
	private Button makeBackButton() {
		Button backButton = new TextButton("<", UISkin.skin);
		backButton.addListener(new ClickListener() {
			
		    @Override
			public void clicked (InputEvent event, float x, float y) {
		    	// TODO what is back
			}
		});
		
		return backButton;
	}
	
	private Button makeSettingsButton() {
		Button settingsButton = new TextButton("?", UISkin.skin);
		settingsButton.addListener(new ClickListener() {
			
		    @Override
			public void clicked (InputEvent event, float x, float y) {
		    	// TODO what is settings
			}
		});
		
		return settingsButton;
	}
	
	private TextField makeNewItemField() {
		TextField newField = new TextField("", UISkin.skin);
    	newField.setMessageText("+ Add New Item");
		newField.addListener(new InputListener() {
		
		    @Override
			public boolean keyTyped (InputEvent event, char character) {
		    	if (character == '\n' || character == '\r') {
		    		addToListFirst(list, newField.getText());
		    		return true;
		    	}
				return false;
			}
		});
		
		return newField;
	}
	
}
