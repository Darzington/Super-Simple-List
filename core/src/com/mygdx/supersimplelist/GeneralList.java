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

public abstract class GeneralList<T> extends Actor {

	private String listName;
	protected ArrayList<T> list;

	private transient final int fieldPadding = 20, entryPadding = 30;
	private transient Table table, wholeTable, header;
	protected transient Skin skin;
	private transient TextButton tempHolding, latestOver;

	public GeneralList() {
		this("New List", new ArrayList<T>());
	}

	public GeneralList(String listName, ArrayList<T> list) {
		this.listName = listName;
		this.list = list;
		setUp();
	}

	public void setUp() {
		table = new Table();
		wholeTable = new Table();
		setupTableStyles();
		setUpHeader();

		refreshTable();
	}

	private void setUpHeader() {
		header = new Table(skin);

		float buttonSidePadding = (MasterList.headerHeight - MasterList.buttonSize) / 2.0f;

		if (showButtons()) {
			header.add(makeBackButton()).center().padLeft(buttonSidePadding).width(MasterList.buttonSize)
					.height(MasterList.buttonSize);
		}

		header.add(new Label(listName, UISkin.skin, "title")).center().expandX();

		if (showButtons()) {
			header.add(makeSettingsButton()).center().padRight(buttonSidePadding).width(MasterList.buttonSize)
					.height(MasterList.buttonSize);
		}

		header.setBackground("list");
		header.setBounds(0, Gdx.graphics.getHeight() - MasterList.headerHeight, Gdx.graphics.getWidth(),
				MasterList.headerHeight);
	}

	private void setupTableStyles() {
		table.defaults().center().width(Gdx.graphics.getWidth() - entryPadding);
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

	protected void removeFromList(T removedEntry) {
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

	// TODO make a "moveToTopOfChecked" function?

	public void addToStage(Stage stage) {
		stage.addActor(this);
		stage.addActor(header);
		stage.addActor(wholeTable);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
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

	private void tryToAddIntoListBefore(T entry, TextButton latestOver, float x, float y) {
		if (latestOver == null) {
			addToListAtIndex(entry, list.size());
		} else {
			String addBeforeThis = latestOver.getText().toString();
			int desiredIndex = getEntryIndex(addBeforeThis);
			if (desiredIndex >= 0) {
				addToListAtIndex(entry, desiredIndex);
			} else {
				addToListFirst(entry);
			}
		}
	}

	protected int getEntryIndex(String entry) {
		for (int i = 0; i < list.size(); i++) {
			if (isThisEntry(list.get(i), entry)) {
				return i;
			}
		}
		return -1;
	}

	protected abstract void onClickBackButton();

	protected abstract void onClickSettingsButton();

	protected abstract TextButton makeSpecificItemButton(T entry);

	protected abstract T createNewEntry(String entry);

	protected abstract boolean isThisEntry(T value, String entry);

	protected abstract String getNewEntryGenericName();

	protected abstract void doTapBehavior(TextButton button, T entry);

	protected void doLongTapBehavior(TextButton button, T entry) {
	}

	protected abstract boolean showButtons();

	protected TextButton makeItemButton(T entry) {
		final TextButton itemButton = makeSpecificItemButton(entry);
		itemButton.addListener(new ActorGestureListener() {

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				doLongTapBehavior(itemButton, entry);
				return false;
			}

			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				doTapBehavior(itemButton, entry);
			}

			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				if (tempHolding == null || !tempHolding.equals(itemButton)) {
					tempHolding = itemButton;
					itemButton.getParent().removeActor(itemButton, false);
					event.getStage().addActor(itemButton);
					itemButton.setTouchable(Touchable.disabled);
				}
				float holdingY = Gdx.graphics.getHeight() - Gdx.input.getY() - itemButton.getHeight() / 2f;
				itemButton.setPosition(entryPadding / 2.0f, holdingY);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (tempHolding != null && tempHolding.equals(itemButton)) {
					removeFromList(entry);
					tryToAddIntoListBefore(entry, latestOver, x, y);
					tempHolding.remove();
					tempHolding = null;
				}
			}

		});
		itemButton.addListener(new ClickListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				if (tempHolding != null && (latestOver == null || latestOver != itemButton)) {
					if (latestOver != null) {
						latestOver.padTop(0);
					}
					itemButton.padTop(tempHolding.getHeight());
					table.invalidateHierarchy();
					wholeTable.invalidateHierarchy();
				}
				latestOver = itemButton;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				itemButton.padTop(0);
				table.invalidateHierarchy();
				wholeTable.invalidateHierarchy();
				latestOver = null;
			}

		});
		return itemButton;
	}

	private TextField makeNewItemField() {
		TextField newField = new TextField("", UISkin.skin);
		newField.setMessageText(" + Add New " + getNewEntryGenericName());
		newField.addListener(new InputListener() {

			@Override
			public boolean keyTyped(InputEvent event, char character) {
				if (character == '\n' || character == '\r') {
					addToListFirst(createNewEntry(newField.getText()));
					return true;
				}
				return false;
			}
		});

		return newField;
	}

	private Button makeBackButton() {
		Button backButton = new TextButton("<", UISkin.skin);
		backButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				onClickBackButton();
			}
		});

		return backButton;
	}

	private Button makeSettingsButton() {
		Button settingsButton = new TextButton("x", UISkin.skin);
		settingsButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				onClickSettingsButton();
				refreshTable();
			}
		});

		return settingsButton;
	}

	private void refreshTable() {
		table.clear();
		Iterator<T> newContents = list.iterator();
		while (newContents.hasNext()) {
			T entry = newContents.next();
			table.add(makeItemButton(entry)).expandX();
			table.row();
		}
		table.setWidth(Gdx.graphics.getWidth());

		wholeTable.clear();

		Table scrollTable = new Table();
		scrollTable.add(makeNewItemField()).width(Gdx.graphics.getWidth() - 10);
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

		table.invalidateHierarchy();
		wholeTable.invalidateHierarchy();
	}

}
