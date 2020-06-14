package com.mygdx.supersimplelist;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MasterList extends GeneralList<SimpleList> {

	public static transient final float headerHeight = Gdx.graphics.getHeight() / 12f, buttonSize = headerHeight * 0.8f;
	private transient SimpleList currentList;

	public MasterList() {
		this(new ArrayList<SimpleList>());
	}

	public MasterList(ArrayList<SimpleList> allLists) {
		super("Super Simple Lists", allLists);
	}

	public void loadData() {

	}

	public void handleReturnToMasterList() {
		if (currentList != null && currentList.shouldClose()) {
			currentList.resetCloseFlag();
			Stage stage = currentList.getStage();
			stage.clear();
			addToStage(stage);
			currentList = null;
		}
	}

	@Override
	protected void onClickBackButton() {
	}

	@Override
	protected void onClickSettingsButton() {
	}

	@Override
	protected boolean showButtons() {
		return false;
	}

	@Override
	protected TextButton makeSpecificItemButton(SimpleList entry) {
		final TextButton listButton = new TextButton(entry.getListName(), skin);
		listButton.left();
		return listButton;
	}

	@Override
	protected SimpleList createNewEntry(String entry) {
		return new SimpleList(entry);
	}

	@Override
	protected boolean isThisEntry(SimpleList value, String entry) {
		return value.getListName().equals(entry);
	}

	@Override
	protected String getNewEntryGenericName() {
		return "List";
	}

	@Override
	protected void doTapBehavior(TextButton button, SimpleList entry) {
		currentList = list.get(getEntryIndex(entry.getListName()));
		currentList.setUp();
		Stage stage = this.getStage();
		stage.clear();
		currentList.addToStage(stage);
	}

}
