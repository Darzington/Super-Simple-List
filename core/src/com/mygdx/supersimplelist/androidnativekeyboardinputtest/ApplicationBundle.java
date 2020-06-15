package com.mygdx.supersimplelist.androidnativekeyboardinputtest;

import com.mygdx.supersimplelist.androidnativekeyboardinputtest.interfaces.android.AndroidTextInputInterface;
import com.mygdx.supersimplelist.androidnativekeyboardinputtest.interfaces.android.VisibleView;

/**
 * Container class to bridge between native and core.
 * 
 * @Author: calanti.games@gmail.com
 */

public class ApplicationBundle {

	private final VisibleView visibleView;
	private final AndroidTextInputInterface androidTextInputInterface;

	public ApplicationBundle(VisibleView visibleView, AndroidTextInputInterface androidTextInputInterface) {
		this.visibleView = visibleView;
		this.androidTextInputInterface = androidTextInputInterface;
	}

	public AndroidTextInputInterface getAndroidTextInputInterface() {
		return androidTextInputInterface;
	}

	public VisibleView getVisibleView() {
		return visibleView;
	}
}
