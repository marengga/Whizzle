package com.marengga.whizzle.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
    private static final String LOG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "WhizzleDB";
 
    // Table Names
    private static final String TABLE_LIBRARY = "Library";
 
    // Library Table Column Names
    private static final String LIB_ID = "LibraryId";
    private static final String LIB_CATEGORY = "Category";
    private static final String LIB_TITLE = "Title";
    private static final String LIB_AUTHOR = "Author";
    private static final String LIB_DESCRIPTION = "Description";
    private static final String LIB_COVER = "CoverImage";
    
    // Create Table SQL Statements
	private static final String CREATE_LIBRARY = "CREATE TABLE "
	    + TABLE_LIBRARY + "(" + LIB_ID + " TEXT PRIMARY KEY," +
	    	LIB_CATEGORY + " TEXT," +
	    	LIB_TITLE + " TEXT," +
	    	LIB_AUTHOR + " TEXT," +
	    	LIB_DESCRIPTION + " TEXT," +
	    	LIB_COVER + " BLOB" +
		")";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_LIBRARY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBRARY);
 
        // create new tables
        onCreate(db);
	}
	
	// ------------------------ "Library" table methods ----------------//
	
	public long createLibrary(LibraryModel lib) {
		long library_id = -1;
		
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    values.put(LIB_ID, lib.getLibraryId());
	    values.put(LIB_CATEGORY, lib.getCategory());
	    values.put(LIB_TITLE, lib.getTitle());
	    values.put(LIB_AUTHOR, lib.getAuthor());
	    values.put(LIB_DESCRIPTION, lib.getDescription());
	    values.put(LIB_COVER, lib.getCover());

	    library_id = db.insert(TABLE_LIBRARY, null, values);
		
		return library_id;
	}

	public LibraryModel getLibrary(String LibraryId) {
	    SQLiteDatabase db = this.getReadableDatabase();

	    String selectQuery = "SELECT  * FROM " + TABLE_LIBRARY + " WHERE " + LIB_ID + " = " + LibraryId;

	    Log.e(LOG, selectQuery);

	    Cursor c = db.rawQuery(selectQuery, null);

	    if (c != null)
	        c.moveToFirst();

	    LibraryModel lib = new LibraryModel();
	    lib.setAuthor(c.getString(c.getColumnIndex(LIB_AUTHOR)));
	    lib.setCategory(c.getInt(c.getColumnIndex(LIB_CATEGORY)));
	    lib.setDescription(c.getString(c.getColumnIndex(LIB_DESCRIPTION)));
	    lib.setLibraryId(c.getString(c.getColumnIndex(LIB_ID)));
	    lib.setTitle(c.getString(c.getColumnIndex(LIB_TITLE)));
	    lib.setCover(c.getBlob(c.getColumnIndex(LIB_COVER)));

	    return lib;
	}

	public ArrayList<LibraryModel> getAllLibrary() {
	    ArrayList <LibraryModel> libs = new ArrayList < LibraryModel > ();
	    String selectQuery = "SELECT * FROM " + TABLE_LIBRARY;

	    Log.e(LOG, selectQuery);

	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);

	    if (c.moveToFirst()) {
	        do {
	        	LibraryModel lib = new LibraryModel();
	        	lib.setAuthor(c.getString(c.getColumnIndex(LIB_AUTHOR)));
	    	    lib.setCategory(c.getInt(c.getColumnIndex(LIB_CATEGORY)));
	    	    lib.setDescription(c.getString(c.getColumnIndex(LIB_DESCRIPTION)));
	    	    lib.setLibraryId(c.getString(c.getColumnIndex(LIB_ID)));
	    	    lib.setTitle(c.getString(c.getColumnIndex(LIB_TITLE)));
	    	    lib.setCover(c.getBlob(c.getColumnIndex(LIB_COVER)));

	            libs.add(lib);
	        } while (c.moveToNext());
	    }

	    return libs;
	}
	
	public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
	

	// ------------------------ SINGLETON INSTANTIATION APPROACH ----------------//
	
	private static DatabaseHelper sInstance;

	public static synchronized DatabaseHelper getInstance(Context context) {
		// Use the application context, which will ensure that you 
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context.getApplicationContext());
		}
		return sInstance;
	}

	/**
	* Constructor should be private to prevent direct instantiation.
	* make call to static method "getInstance()" instead.
	*/
	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
}