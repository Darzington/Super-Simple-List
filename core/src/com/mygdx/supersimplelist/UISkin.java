package com.mygdx.supersimplelist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UISkin {

	public static Skin skin;

	public UISkin() {
		skin = new Skin(Gdx.files.internal("sgx/skin/sgx-ui.json"));
	}
}
