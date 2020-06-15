package com.mygdx.supersimplelist;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.supersimplelist.androidnativekeyboardinputtest.ApplicationBundle;

public class MySuperSimpleList extends ApplicationAdapter {

	private final String saveName = "save.json";
	private Stage stage;
	private MasterList masterList;

	private transient ApplicationBundle applicationBundle;

	public MySuperSimpleList(ApplicationBundle applicationBundle) {
		this.applicationBundle = applicationBundle;
	}

	@Override
	public void create() {
		startUp();
	}

	private void startUp() {
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
		save();
		stage.dispose();
		UISkin.skin.dispose();
	}

	@Override
	public void pause() {
		super.pause();
		save();
		UISkin.skin.dispose();
	}

	@Override
	public void resume() {
		startUp();
		super.resume();
	}

	private void load() {
		FileHandle file = Gdx.files.local(saveName);
		if (file.exists()) {
			Json json = new Json();
			json.setIgnoreUnknownFields(true);
			try {
				masterList = json.fromJson(MasterList.class, file);
				masterList.setListName("Super Simple Lists");
			} catch (Exception e) {
				masterList = new MasterList(applicationBundle);
				masterList.setListName(e.getMessage());
			}
		} else {
			masterList = new MasterList(applicationBundle);
			masterList.setListName("no savefile");
		}

		masterList.setUp();
	}

	private void save() {
		stage.clear();
		Json json = new Json();
		json.setUsePrototypes(false);
		FileHandle file = Gdx.files.local(saveName);
		file.writeString(json.toJson(masterList), false);
	}

}
