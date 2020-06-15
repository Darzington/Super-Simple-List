package com.mygdx.supersimplelist.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.supersimplelist.MySuperSimpleList;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		int multiplier = 5;
		config.width = 90 * multiplier;
		config.height = 160 * multiplier;

		new LwjglApplication(new MySuperSimpleList(null), config);
	}
}
