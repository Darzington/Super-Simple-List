package com.mygdx.supersimplelist;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MySuperSimpleList extends ApplicationAdapter {

	private Stage stage;
	private MasterList masterList;

	@Override
	public void create() {
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		new UISkin();

		ArrayList<SimpleList> allLists = new ArrayList<>();
		allLists.add(new SimpleList());
		masterList = new MasterList(allLists);
		masterList.addToStage(stage);

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		masterList.handleReturnToMasterList();
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
