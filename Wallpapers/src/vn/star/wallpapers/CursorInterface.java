package vn.star.wallpapers;

import android.database.Cursor;

public interface CursorInterface {
	Cursor getCursor();
	int getNumberOfColumns();
	int getHeightOfGrid();
}
