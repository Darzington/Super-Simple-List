package com.mygdx.supersimplelist;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MySuperSimpleList extends ApplicationAdapter {

	private final String saveName = "savedLists.json";
	private Stage stage;
	private MasterList masterList;

	@Override
	public void create() {
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		new UISkin();

		load();
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
		save();
	}

	private void load() {
		FileHandle file = Gdx.files.local(saveName);
		if (file.exists()) {
			Json json = new Json();
			json.setIgnoreUnknownFields(true);
			try {
				masterList = json.fromJson(MasterList.class, file);
			} catch (Exception e) {
				masterList = new MasterList();
			}
		} else {
			masterList = new MasterList();
		}

		masterList.setUp();
	}

	private void save() {
		Json json = new Json();
		json.setUsePrototypes(false);
		FileHandle file = Gdx.files.local(saveName);
		file.writeString(json.toJson(masterList), false);
	}

}
