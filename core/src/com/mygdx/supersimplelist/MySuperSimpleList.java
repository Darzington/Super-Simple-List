package com.mygdx.supersimplelist;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MySuperSimpleList extends ApplicationAdapter {
	
	private Stage stage;
	
	@Override
	public void create () {   
		stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));    
		
		new UISkin();
		
		ArrayList<SimpleList> allLists = new ArrayList<>();
		allLists.add(new SimpleList());
		MasterList masterList = new MasterList(allLists);
		masterList.addToStage(stage);
		
        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(stage);
        Gdx.input.setInputProcessor(im);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
	    stage.act();
	    stage.draw();
	}
	
	@Override 
	public void dispose() {
		stage.dispose();
	}
	
	
}
