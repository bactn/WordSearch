package vn.star.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "bookmarksManager";

	// Contacts table name
	private static final String TABLE_BOOKMARKS = "bookmarks";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PH_NO = "phone_number";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_BOOKMARKS_TABLE = "CREATE TABLE " + TABLE_BOOKMARKS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_PH_NO + " TEXT" + ")";
		db.execSQL(CREATE_BOOKMARKS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARKS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	public// Adding new contact
	void addBookmark(Bookmark bookmark) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, bookmark.getName()); // Contact Name
		// values.put(KEY_PH_NO, bookmark.getPhoneNumber()); // Contact Phone

		// Inserting Row
		db.insert(TABLE_BOOKMARKS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	Bookmark getBookmark(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_BOOKMARKS, new String[] { KEY_ID,
				KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Bookmark bookmark = new Bookmark(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return contact
		return bookmark;
	}

	// Getting All Contacts
	public List<Bookmark> getAllBookmarks() {
		List<Bookmark> bookmarkList = new ArrayList<Bookmark>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_BOOKMARKS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Bookmark bookmark = new Bookmark();
				bookmark.setID(Integer.parseInt(cursor.getString(0)));
				bookmark.setName(cursor.getString(1));
				bookmark.setPhoneNumber(cursor.getString(2));
				// Adding contact to list
				bookmarkList.add(bookmark);
			} while (cursor.moveToNext());
		}

		// return contact list
		return bookmarkList;
	}

	// Updating single contact
	public int updateBookmark(Bookmark bookmark) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, bookmark.getName());
		values.put(KEY_PH_NO, bookmark.getPhoneNumber());

		// updating row
		return db.update(TABLE_BOOKMARKS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(bookmark.getID()) });
	}

	// Deleting single contact
	public void deleteBookmark(Bookmark bookmark) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BOOKMARKS, KEY_ID + " = ?",
				new String[] { String.valueOf(bookmark.getID()) });
		Log.e("delete", "delete" + bookmark.getID());
		db.close();
	}

	// Getting contacts Count
	public int getBookmarksCount() {
		String countQuery = "SELECT  * FROM " + TABLE_BOOKMARKS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
//		cursor.close();

		// return count
		return cursor.getCount();
	}

	public void deleteAllBookmark(Bookmark bookmark) {

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from " + TABLE_BOOKMARKS);
		db.close();
	}

}