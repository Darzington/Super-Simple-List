package com.mygdx.supersimplelist;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class MasterList extends GeneralList<SimpleList> {
	
	public static transient final float headerHeight = Gdx.graphics.getHeight()/10f, buttonSize = headerHeight*0.6f;
	private transient int currentListIndex;
	
	public MasterList() {
		this(new ArrayList<SimpleList>());
	}

	public MasterList(ArrayList<SimpleList> allLists) {
		super("Super Simple Lists", allLists);
		currentListIndex = -1;
	}
	
	public void loadData() {
		
	}
	
	public void addToStage(Stage stage) {
		list.stream().forEach(simpleList -> simpleList.addToStage(stage));
		super.addToStage(stage);
	}
	
	public void draw (Batch batch, float parentAlpha) {
		batch.begin();
		if (currentListIndex == -1) {
			super.draw(batch, parentAlpha);
		} else {
			list.get(currentListIndex).draw(batch, parentAlpha);
		}
		batch.end();
	}

	@Override
	protected void onClickBackButton() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onClickSettingsButton() {
		// TODO Auto-generated method stub
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
		currentListIndex = getEntryIndex(entry.getListName());
		this.setTouchable(Touchable.disabled);
	}
	
}
