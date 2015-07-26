package vn.star.wallpapers;

import android.database.Cursor;
import android.view.Menu;

public interface Util {
	Cursor getCursor();
	Menu getMenu();
	int getNumberOfColumns();
	int getHeightOfGridItem();
	
}
