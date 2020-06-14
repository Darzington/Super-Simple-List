package com.mygdx.supersimplelist;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;

public class SimpleList extends GeneralList<StringBoolean> {

	private transient boolean shouldClose;

	public SimpleList() {
		this("New List", new ArrayList<StringBoolean>());
	}

	public SimpleList(String listName) {
		this(listName, new ArrayList<StringBoolean>());
	}

	public SimpleList(String listName, ArrayList<StringBoolean> list) {
		super(listName, list);
		shouldClose = false;
	}

	private void testingStart() {
		addToListFirst(new StringBoolean("to do 1", false));
		addToListFirst(new StringBoolean("to do 2", false));
		addToListFirst(new StringBoolean("to do 3", false));
		addToListFirst(new StringBoolean("to do 4", false));
		addToListFirst(new StringBoolean("to do 5", false));
		addToListFirst(new StringBoolean("to do 6", false));
	}

	public boolean shouldClose() {
		return shouldClose;
	}

	public void resetCloseFlag() {
		shouldClose = false;
	}

	@Override
	protected TextButton makeSpecificItemButton(StringBoolean entry) {
		final CheckBox checkBox = new CheckBox(entry.getEntry(), skin);
		checkBox.left();
		checkBox.setChecked(entry.isChecked());
		checkBox.getImage().setScaling(Scaling.fill);
		checkBox.getImageCell().size(checkBox.getHeight());
		return checkBox;
	}

	@Override
	protected void onClickBackButton() {
		shouldClose = true;
	}

	@Override
	protected void onClickSettingsButton() {
		list.removeIf(sb -> sb.isChecked());
	}

	@Override
	protected boolean showButtons() {
		return true;
	}

	@Override
	protected StringBoolean createNewEntry(String entry) {
		return new StringBoolean(entry, false);
	}

	@Override
	protected String getNewEntryGenericName() {
		return "Item";
	}

	@Override
	protected void doTapBehavior(TextButton button, StringBoolean sb) {
		boolean checked = button.isChecked();
		sb.setChecked(checked);

		if (!checked) {
			moveToTop(sb);
		} else {
			moveToBottom(sb);
		}
	}

	@Override
	protected boolean isThisEntry(StringBoolean sb, String entry) {
		return sb.getEntry().equals(entry);
	}

}
