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
    private static final String TAG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "WhizzleDB";
 
    // Table Names
    private static final String TABLE_LIBRARY = "Library";
    private static final String TABLE_PROFILE = "Profile";
    private static final String TABLE_NEWS = "News";
    private static final String TABLE_USER = "User";
    private static final String TABLE_TEAM = "Team";
    private static final String TABLE_PIN = "Pin";
 
    // Library Table Column Names
    private static final String LIB_ID = "LibraryId";
    private static final String LIB_CATEGORY = "Category";
    private static final String LIB_TITLE = "Title";
    private static final String LIB_AUTHOR = "Author";
    private static final String LIB_DESCRIPTION = "Description";
    private static final String LIB_COVER = "CoverImage";
    
    private static final String PRF_ID = "UserId";
    private static final String PRF_NAME = "FullName";
    private static final String PRF_DEPT = "Department";
    private static final String PRF_AVATAR = "Avatar";
    
	private static final String NWS_ID = "NewsId";
	private static final String NWS_AUTHOR = "Author";
	private static final String NWS_PUBLISHED = "PublishedOn";
	private static final String NWS_CATEGORY = "Category";
	private static final String NWS_TITLE = "Title";
	private static final String NWS_CONTENT = "Content";
	private static final String NWS_IMG = "ImageUrl";
	
	private static final String USR_ID = "UserId";
	private static final String USR_NAME = "FullName";
	private static final String USR_DEPT = "Department";
	private static final String USR_AVATAR = "Avatar";
	private static final String USR_STATUS = "Status";
	private static final String USR_TEAMID = "TeamId";
	private static final String USR_ISFRIEND = "IsFriend";
	
	private static final String TIM_ID = "TeamId";
	private static final String TIM_NAME = "Name";
	private static final String TIM_DESC = "Description";
	private static final String TIM_AVATAR = "Avatar";
	
	private static final String PIN_ID = "PinId";
	private static final String PIN_TEAM = "TeamId";
	private static final String PIN_TITLE = "Title";
	private static final String PIN_DESC = "Description";
	private static final String PIN_ASSIGNEE = "Assignee";
	private static final String PIN_DUE = "DueDate";
	private static final String PIN_PRIORITY = "Priority";
	private static final String PIN_STATUSCODE = "StatusCode";
    
    
    // Create Table SQL Statements
	private static final String CREATE_LIBRARY = "CREATE TABLE "
	    + TABLE_LIBRARY + "(" + LIB_ID + " TEXT PRIMARY KEY, " +
	    	LIB_CATEGORY + " TEXT," +
	    	LIB_TITLE + " TEXT," +
	    	LIB_AUTHOR + " TEXT," +
	    	LIB_DESCRIPTION + " TEXT," +
	    	LIB_COVER + " BLOB" +
		")";
	private static final String CREATE_PROFILE = "CREATE TABLE "
		    + TABLE_PROFILE + "(" + PRF_ID + " TEXT PRIMARY KEY, " +
				PRF_NAME + " TEXT, "+
				PRF_DEPT + " TEXT, "+
		    	PRF_AVATAR + " BLOB" +
			")";
	private static final String CREATE_NEWS = "CREATE TABLE "
		    + TABLE_NEWS + "(" + NWS_ID + " TEXT PRIMARY KEY, " +
			    NWS_AUTHOR + " TEXT, "+
			    NWS_PUBLISHED + " TEXT, "+
			    NWS_CATEGORY + " INT, "+
			    NWS_TITLE + " TEXT, "+
			    NWS_CONTENT + " TEXT, "+
			    NWS_IMG + " TEXT"+
			")";
	private static final String CREATE_USER = "CREATE TABLE "
		    + TABLE_USER + "(" + USR_ID + " TEXT PRIMARY KEY, "+
				USR_NAME + " TEXT, "+
				USR_DEPT + " TEXT, "+
				USR_AVATAR + " BLOB, "+
				USR_STATUS + " TEXT, "+
				USR_TEAMID + " TEXT, "+
				USR_ISFRIEND + " BOOL, "+
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
		
		Log.d(TAG, "Inserting Library");
		
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    values.put(LIB_ID, lib.getLibraryId());
	    values.put(LIB_CATEGORY, lib.getCategory());
	    values.put(LIB_TITLE, lib.getTitle());
	    values.put(LIB_AUTHOR, lib.getAuthor());
	    values.put(LIB_DESCRIPTION, lib.getDescription());
	    values.put(LIB_COVER, lib.getCover());
	    
	    //library_id = db.insert(TABLE_LIBRARY, null, values);
	    
	    library_id = db.insertWithOnConflict(TABLE_LIBRARY, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		
		return library_id;
	}

	public LibraryModel getLibrary(String LibraryId) {
	    SQLiteDatabase db = this.getReadableDatabase();

	    String selectQuery = "SELECT  * FROM " + TABLE_LIBRARY + " WHERE " + LIB_ID + " = " + LibraryId;

	    Log.d(TAG, "Getting Library : " + selectQuery);

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

	    Log.d(TAG, "Getting All Libraries : " + selectQuery);

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