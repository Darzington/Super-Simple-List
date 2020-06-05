package com.mygdx.supersimplelist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UISkin {

	public static Skin skin;
	
	public UISkin() {
		skin = new Skin(Gdx.files.internal("sgx/skin/sgx-ui.json"));
		//skin.addRegions(new TextureAtlas(Gdx.files.internal("sgx/skin/sgx-ui.atlas")));

		/*
		Color red = skin.getColor("red");
		BitmapFont font = skin.getFont("large");
		TextureRegion region = skin.getRegion("hero");
		NinePatch patch = skin.getPatch("header");
		Sprite sprite = skin.getSprite("footer");
		TiledDrawable tiled = skin.getTiledDrawable("pattern");
		Drawable drawable = skin.getDrawable("enemy");
		*/
	}
}
