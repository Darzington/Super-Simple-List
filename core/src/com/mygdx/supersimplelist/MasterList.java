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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class MasterList extends Actor {
	
	public static final float headerHeight = Gdx.graphics.getHeight()/10f, buttonSize = headerHeight*0.6f;
	private ArrayList<SimpleList> allLists;
	
	public MasterList() {
		allLists = new ArrayList<SimpleList>();
		

	}

	public MasterList(ArrayList<SimpleList> allLists) {
		this();
		this.allLists = allLists;
	}
	
	public void loadData() {
		
	}
	
	public void addToStage(Stage stage) {
		stage.addActor(this);
		allLists.stream().forEach(list -> list.addToStage(stage));
		
	}
	
	public void draw (Batch batch, float parentAlpha) {
		/*
		header.debug();
		ShapeRenderer sr = new ShapeRenderer();
		sr.setAutoShapeType(true);
		sr.begin();
		header.drawDebug(sr);
		sr.end();
		*/
		allLists.get(0).draw(batch, parentAlpha);
	}
	

}
