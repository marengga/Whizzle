package com.marengga.whizzle.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
    
    // #region Table Definitions
    
    // Table Names
    private static final String TABLE_LIBRARY = "Library";
    private static final String TABLE_PROFILE = "Profile";
    private static final String TABLE_NEWS = "News";
    private static final String TABLE_USER = "User";
    private static final String TABLE_TEAM = "Team";
    private static final String TABLE_USERTEAM = "UserTeam";
    private static final String TABLE_PIN = "Pin";
    private static final String TABLE_MESSAGE = "Message";
 
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
    private static final String PRF_STATUS = "Status";
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
	private static final String USR_ISFRIEND = "IsFriend";
	
	private static final String TIM_ID = "TeamId";
	private static final String TIM_NAME = "Name";
	private static final String TIM_DESC = "Description";
	private static final String TIM_AVATAR = "Avatar";
	
	private static final String UTM_USER = "UserId";
	private static final String UTM_TEAM = "TeamId";
	
	private static final String PIN_ID = "PinId";
	private static final String PIN_TEAM = "TeamId";
	private static final String PIN_TITLE = "Title";
	private static final String PIN_DESC = "Description";
	private static final String PIN_CREATEDBY = "CreatedBy";
	private static final String PIN_ASSIGNEE = "Assignee";
	private static final String PIN_DUE = "DueDate";
	private static final String PIN_PRIORITY = "Priority";
	private static final String PIN_STATUSCODE = "StatusCode";
	
	private static final String MSG_ID = "MessageId";
	private static final String MSG_SENDER = "Sender";
	private static final String MSG_SENT = "Sent";
	private static final String MSG_REC_USER = "RecipientUser";
	private static final String MSG_REC_GROUP = "RecipientGroup";
	private static final String MSG_MESSAGE = "Message";
	private static final String MSG_EXPIRED = "Expired";
	private static final String MSG_STATUS = "StatusCode";
    
    // Create Table SQL Statements
	private static final String CREATE_LIBRARY = "CREATE TABLE " +
			TABLE_LIBRARY + "(" + LIB_ID + " TEXT PRIMARY KEY, " +
	    	LIB_CATEGORY + " TEXT," +
	    	LIB_TITLE + " TEXT," +
	    	LIB_AUTHOR + " TEXT," +
	    	LIB_DESCRIPTION + " TEXT," +
	    	LIB_COVER + " BLOB" +
		")";
	private static final String CREATE_PROFILE = "CREATE TABLE " +
			TABLE_PROFILE + "(" + PRF_ID + " TEXT PRIMARY KEY, " +
			PRF_NAME + " TEXT, "+
			PRF_DEPT + " TEXT, "+
			PRF_STATUS + " TEXT, "+
		    PRF_AVATAR + " BLOB" +
		")";
	private static final String CREATE_NEWS = "CREATE TABLE " +
			TABLE_NEWS + "(" + NWS_ID + " TEXT PRIMARY KEY, " +
			NWS_AUTHOR + " TEXT, "+
			NWS_PUBLISHED + " NUMERIC, "+
			NWS_CATEGORY + " INT, "+
			NWS_TITLE + " TEXT, "+
			NWS_CONTENT + " TEXT, "+
			NWS_IMG + " TEXT"+
		")";
	private static final String CREATE_USER = "CREATE TABLE " +
			TABLE_USER + "(" + USR_ID + " TEXT PRIMARY KEY, "+
			USR_NAME + " TEXT, "+
			USR_DEPT + " TEXT, "+
			USR_AVATAR + " BLOB, "+
			USR_STATUS + " TEXT, "+
			USR_ISFRIEND + " BOOL "+
		")";
	private static final String CREATE_TEAM = "CREATE TABLE " +
			TABLE_TEAM + "(" + TIM_ID + " TEXT PRIMARY KEY, "+
			TIM_NAME + " TEXT, "+
			TIM_DESC + " TEXT, "+
			TIM_AVATAR + " TEXT "+
		")";
	private static final String CREATE_USERTEAM = "CREATE TABLE " +
			TABLE_USERTEAM + "("+ UTM_TEAM + " TEXT, "+
			UTM_USER + " TEXT, "+
			"PRIMARY KEY (" + UTM_TEAM + "," + UTM_USER + ")"+
		")";
	private static final String CREATE_PIN = "CREATE TABLE " +
			TABLE_PIN + "(" + PIN_ID + " TEXT PRIMARY KEY, "+
			PIN_TEAM + " TEXT, "+
			PIN_TITLE + " TEXT, "+
			PIN_DESC + " TEXT, "+
			PIN_CREATEDBY + " TEXT, "+
			PIN_ASSIGNEE + " TEXT, "+
			PIN_DUE + " NUMERIC, "+
			PIN_PRIORITY + " NUMERIC, "+
			PIN_STATUSCODE + " NUMERIC "+
		")";
	private static final String CREATE_MESSAGE = "CREATE TABLE " +
			TABLE_MESSAGE + "(" + MSG_ID + " TEXT PRIMARY KEY, "+
			MSG_SENDER + " TEXT, "+
			MSG_SENT + " NUMERIC, "+
			MSG_REC_USER + " TEXT, "+
			MSG_REC_GROUP + " TEXT, "+
			MSG_MESSAGE + " TEXT, "+
			MSG_EXPIRED + " NUMERIC, "+
			MSG_STATUS + " NUMERIC "+
		")";
	
	// #endregion
			
	// #region OnCreate and Upgrade
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_LIBRARY);
		db.execSQL(CREATE_PROFILE);
		db.execSQL(CREATE_NEWS);
		db.execSQL(CREATE_USER);
		db.execSQL(CREATE_TEAM);
		db.execSQL(CREATE_USERTEAM);
		db.execSQL(CREATE_PIN);
		db.execSQL(CREATE_MESSAGE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBRARY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERTEAM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        // create new tables
        onCreate(db);
	}
	// #endregion
	
	// #region Library
	
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
	
	// #endregion
	
	// #region Profile
	
	public long createProfile(ProfileModel p) {
		long profileId = -1;
		
		Log.d(TAG, "Inserting Profile");
		
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    values.put(PRF_ID, p.getUserId());
	    values.put(PRF_NAME, p.getFullName());
	    values.put(PRF_DEPT, p.getDepartment());
	    values.put(PRF_STATUS, p.getStatus());
	    values.put(PRF_AVATAR, p.getAvatar());
	    
	    profileId = db.insertWithOnConflict(TABLE_PROFILE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		
		return profileId;
	}

	public ProfileModel getProfileDetail() {
	    SQLiteDatabase db = this.getReadableDatabase();

	    String selectQuery = "SELECT * FROM " + TABLE_PROFILE;

	    Log.d(TAG, "Getting Profile Detail : " + selectQuery);

	    Cursor c = db.rawQuery(selectQuery, null);

	    if (c != null)
	        c.moveToFirst();
	    
	    ProfileModel p = new ProfileModel();
	    p.setAvatar(c.getBlob(c.getColumnIndex(PRF_AVATAR)));
	    p.setDepartment(c.getString(c.getColumnIndex(PRF_DEPT)));
	    p.setFullName(c.getString(c.getColumnIndex(PRF_NAME)));
	    p.setStatus(c.getString(c.getColumnIndex(PRF_STATUS)));
	    p.setUserId(c.getString(c.getColumnIndex(PRF_ID)));

	    return p;
	}
	
	public int updateStatus(String status){
		Log.d(TAG, "Updating Status : " + status);
		SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRF_STATUS, status);
        // updating row
        int r = db.update(TABLE_PROFILE, values, null, null );
        return r;
	}
	
	public int updateAvatar(byte[] avatar) {
		Log.d(TAG, "Updating Avatar : " + String.valueOf(avatar));
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRF_AVATAR, avatar);
        int r = db.update(TABLE_PROFILE, values, null, null );
        return r;
    }
	
	// #endregion

	// #region User
	public long createUser(UserModel u, boolean replace) {
		long library_id = -1;
		
		Log.d(TAG, "Inserting User");
		
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    values.put(USR_AVATAR, u.getAvatar());
	    values.put(USR_DEPT, u.getDepartment());
	    values.put(USR_ID, u.getUserId());
	    values.put(USR_ISFRIEND, u.getIsFriend());
	    values.put(USR_NAME, u.getFullName());
	    values.put(USR_STATUS, u.getStatus());
	    
	    if(replace)
	    	library_id = db.insertWithOnConflict(TABLE_USER, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	    else
	    	library_id = db.insertWithOnConflict(TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		
		return library_id;
	}
	
	public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        
        Log.d(TAG, "Deleting All User from DB");
        
        db.delete(TABLE_USER, null, null);
        db.close();
    }

	public UserModel getUser(String UserId) {
	    SQLiteDatabase db = this.getReadableDatabase();

	    String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE " + USR_ID + " = " + UserId;

	    Log.d(TAG, "Getting User : " + selectQuery);

	    Cursor c = db.rawQuery(selectQuery, null);

	    if (c != null)
	        c.moveToFirst();
	    
	    UserModel u = new UserModel();
	    u.setAvatar(c.getBlob(c.getColumnIndex(USR_AVATAR)));
	    u.setDepartment(c.getString(c.getColumnIndex(USR_DEPT)));
	    u.setFullName(c.getString(c.getColumnIndex(USR_NAME)));
	    u.setIsFriend( c.getInt(c.getColumnIndex(USR_ISFRIEND))!=0 );
	    u.setStatus(c.getString(c.getColumnIndex(USR_STATUS)));
	    u.setUserId(c.getString(c.getColumnIndex(USR_ID)));

	    return u;
	}

	public ArrayList<UserModel> getAllContact() {
	    ArrayList <UserModel> us = new ArrayList <UserModel> ();
	    String selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE "+USR_ISFRIEND+"=1 " +
	    		"ORDER BY "+USR_NAME;

	    Log.d(TAG, "Getting All Contacts : " + selectQuery);

	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);

	    if (c.moveToFirst()) {
	        do {
	        	UserModel u = new UserModel();
	    	    u.setAvatar(c.getBlob(c.getColumnIndex(USR_AVATAR)));
	    	    u.setDepartment(c.getString(c.getColumnIndex(USR_DEPT)));
	    	    u.setFullName(c.getString(c.getColumnIndex(USR_NAME)));
	    	    u.setIsFriend(c.getInt(c.getColumnIndex(USR_ISFRIEND))!=0);
	    	    u.setStatus(c.getString(c.getColumnIndex(USR_STATUS)));
	    	    u.setUserId(c.getString(c.getColumnIndex(USR_ID)));
	            us.add(u);
	        } while (c.moveToNext());
	    }
	    return us;
	}
	
	public ArrayList<UserModel> getTeamMember(String TeamId) {
	    ArrayList <UserModel> us = new ArrayList <UserModel> ();
	    String selectQuery = "SELECT u.* "+
	    		"FROM " + TABLE_USER + " u " +
	    		"INNER JOIN " + TABLE_USERTEAM + " ut "+ 
	    			" ON u." + USR_ID + "=ut." + UTM_USER +  " " +
	    		"WHERE ut." + UTM_TEAM + "='" + TeamId + "' " +
	    		"ORDER BY u." + USR_NAME;

	    Log.d(TAG, "Getting All Team Member : " + selectQuery);

	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);

	    if (c.moveToFirst()) {
	        do {
	        	UserModel u = new UserModel();
	    	    u.setAvatar(c.getBlob(c.getColumnIndex(USR_AVATAR)));
	    	    u.setDepartment(c.getString(c.getColumnIndex(USR_DEPT)));
	    	    u.setFullName(c.getString(c.getColumnIndex(USR_NAME)));
	    	    u.setIsFriend(c.getInt(c.getColumnIndex(USR_ISFRIEND))!=0);
	    	    u.setStatus(c.getString(c.getColumnIndex(USR_STATUS)));
	    	    u.setUserId(c.getString(c.getColumnIndex(USR_ID)));
	            us.add(u);
	        } while (c.moveToNext());
	    }
	    return us;
	}
	// #endregion
	
	// #region UserTeam
	public long createUserTeam(UserTeamModel ut) {
		long library_id = -1;
		
		Log.d(TAG, "Inserting UserTeam");
		
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    values.put(UTM_TEAM, ut.getTeamId());
	    values.put(UTM_USER, ut.getUserId());
	    
	    library_id = db.insertWithOnConflict(TABLE_USERTEAM, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		
		return library_id;
	}
	// #endregion

	// #region Team
	public long createTeam(TeamModel t) {
		long library_id = -1;
		
		Log.d(TAG, "Inserting Team");
		
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    values.put(TIM_AVATAR, t.getAvatar());
	    values.put(TIM_DESC, t.getDescription());
	    values.put(TIM_ID, t.getTeamId());
	    values.put(TIM_NAME, t.getName());
	    
	    library_id = db.insertWithOnConflict(TABLE_TEAM, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		
		return library_id;
	}

	public TeamModel getTeam(String TeamId) {
	    SQLiteDatabase db = this.getReadableDatabase();

	    String selectQuery = "SELECT  * FROM " + TABLE_TEAM + " WHERE " + TIM_ID + " = " + TeamId;

	    Log.d(TAG, "Getting Team : " + selectQuery);

	    Cursor c = db.rawQuery(selectQuery, null);

	    if (c != null)
	        c.moveToFirst();
	    
	    TeamModel t = new TeamModel();
	    t.setAvatar(c.getBlob(c.getColumnIndex(TIM_AVATAR)));
	    t.setDescription(c.getString(c.getColumnIndex(TIM_DESC)));
	    t.setName(c.getString(c.getColumnIndex(TIM_NAME)));
	    t.setTeamId(c.getString(c.getColumnIndex(TIM_ID)));

	    return t;
	}

	public ArrayList<TeamModel> getAllTeam() {
	    ArrayList <TeamModel> us = new ArrayList <TeamModel> ();
	    String selectQuery = "SELECT * FROM " + TABLE_TEAM;

	    Log.d(TAG, "Getting All Team : " + selectQuery);

	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);

	    if (c.moveToFirst()) {
	        do {
	        	TeamModel t = new TeamModel();
	    	    t.setAvatar(c.getBlob(c.getColumnIndex(TIM_AVATAR)));
	    	    t.setDescription(c.getString(c.getColumnIndex(TIM_DESC)));
	    	    t.setName(c.getString(c.getColumnIndex(TIM_NAME)));
	    	    t.setTeamId(c.getString(c.getColumnIndex(TIM_ID)));
	            us.add(t);
	        } while (c.moveToNext());
	    }
	    return us;
	}
	// #endregion
	
	// #region Message
	public long createMessage(MessageModel m) {
		long msdig = -1;
		
		Log.d(TAG, "Inserting Message");
		
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    values.put(MSG_EXPIRED, m.getExpired().toString());
	    values.put(MSG_ID, m.getMessageId());
	    values.put(MSG_MESSAGE, m.getMessage());
	    values.put(MSG_REC_GROUP, m.getRecipientGroup());
	    values.put(MSG_REC_USER, m.getRecipientUser());
	    values.put(MSG_SENDER, m.getSender());
	    values.put(MSG_SENT, m.getSent().toString());
	    values.put(MSG_STATUS, m.getStatusCode());
	    
	    msdig = db.insertWithOnConflict(TABLE_MESSAGE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		
		return msdig;
	}
	// TODO : salah nih methodnya
	public ArrayList<ChatModel> getMessageByUser(String UserId) {
	    ArrayList <ChatModel> us = new ArrayList <ChatModel> ();
	    String selectQuery = "SELECT u." + USR_NAME + ", m." + MSG_MESSAGE + ", m." + MSG_SENT + ", m." + MSG_STATUS + "," +
	    		" CASE WHEN m." + MSG_SENDER + "='" + UserId + "' THEN 1 ELSE 0 END as IsMe" + 
	    		" FROM " + TABLE_MESSAGE + " m " +
	    		" INNER JOIN " + TABLE_USER + " u " +
	    			" ON m." + MSG_SENDER + " = u." + USR_ID + 
	    			" OR m." + MSG_REC_USER + " = u." + USR_ID +
	    		" WHERE " + MSG_SENDER + "='" + UserId + "' OR " + MSG_REC_USER + "='" + UserId + "'";

	    Log.d(TAG, "Getting All Message by User : " + selectQuery);

	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);

	    if (c.moveToFirst()) {
	        do {
	        	ChatModel t = new ChatModel();
	        	t.setIsMe(c.getInt(c.getColumnIndex("IsMe")) == 1 ? true : false);
	        	t.setMessage(c.getString(c.getColumnIndex(MSG_MESSAGE)));
	        	if(t.getIsMe())
	        		t.setUsername("Me");
	        	else
	        		t.setUsername(c.getString(c.getColumnIndex(USR_NAME)));
				t.setSent(c.getString(c.getColumnIndex(MSG_SENT)));
	        	t.setStatusCode(c.getInt(c.getColumnIndex(MSG_STATUS)));
	            us.add(t);
	        } while (c.moveToNext());
	    }
	    return us;
	}
	public ArrayList<ChatModel> getTeamConversation(String TeamId, String UserId) {
	    ArrayList <ChatModel> us = new ArrayList <ChatModel> ();
	    String selectQuery = "SELECT m." + MSG_SENDER + ", u." + USR_NAME + ", m." + MSG_MESSAGE + ", m." + MSG_SENT + ", m." + MSG_STATUS + "," +
	    		" CASE WHEN m." + MSG_SENDER + "='" + UserId + "' THEN 1 ELSE 0 END as IsMe" + 
	    		" FROM " + TABLE_MESSAGE + " m " +
	    		" LEFT JOIN " + TABLE_USER + " u " +
	    			" ON m." + MSG_SENDER + " = u." + USR_ID +
	    		" WHERE " + MSG_REC_GROUP + "='" + TeamId + "'";

	    Log.d(TAG, "Getting Team Conversation : " + selectQuery);

	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	    Log.v("USer ID", UserId);
	    Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));

	    if (c.moveToFirst()) {
	        do {
	        	ChatModel t = new ChatModel();
	        	t.setIsMe(c.getInt(c.getColumnIndex("IsMe")) == 1 ? true : false);
	        	t.setMessage(c.getString(c.getColumnIndex(MSG_MESSAGE)));
	        	if(t.getIsMe())
	        		t.setUsername("- " + c.getString(c.getColumnIndex(MSG_SENDER)) + " -");
	        	else
	        		t.setUsername(c.getString(c.getColumnIndex(USR_NAME)));
				t.setSent(c.getString(c.getColumnIndex(MSG_SENT)));
	        	t.setStatusCode(c.getInt(c.getColumnIndex(MSG_STATUS)));
	            us.add(t);
	        } while (c.moveToNext());
	    }
	    return us;
	}
	
	public ArrayList<ChatModel> getChatList() {
	    ArrayList <ChatModel> us = new ArrayList <ChatModel> ();
	    String selectQuery = "SELECT u." + USR_ID + ", u." + USR_NAME + ", u." + USR_AVATAR +", m." + MSG_MESSAGE + " "+
			"FROM " + TABLE_USER + " u "+
			"INNER JOIN " + TABLE_MESSAGE + " m "+
			"ON m." + MSG_ID + "= "+
			"( "+
				"SELECT " + MSG_ID + " "+ 
				"FROM " + TABLE_MESSAGE + " "+
				"WHERE " + MSG_REC_USER + "= u." + USR_ID + " "+
				"ORDER BY " + MSG_SENT + " DESC "+
				"LIMIT 1" +
			")";

	    Log.d(TAG, "Getting Chat List : " + selectQuery);

	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);

	    if (c.moveToFirst()) {
	        do {
	        	ChatModel t = new ChatModel();
	        	t.setUserId(c.getString(c.getColumnIndex(USR_ID)));
	        	t.setMessage(c.getString(c.getColumnIndex(MSG_MESSAGE)));
	        	t.setUsername(c.getString(c.getColumnIndex(USR_NAME)));
				t.setAvatar(c.getBlob(c.getColumnIndex(USR_AVATAR)));
	            us.add(t);
	        } while (c.moveToNext());
	    }
	    return us;
	}
	// #endregion
	
	// #region Pin
	public long createPin(PinModel t) {
		long library_id = -1;
		
		Log.d(TAG, "Inserting Pin");
		
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    values.put(PIN_ASSIGNEE, t.getAssignee());
	    values.put(PIN_DESC, t.getDescription());
	    values.put(PIN_DUE, t.getDueDate());
	    values.put(PIN_ID, t.getPinId());
	    values.put(PIN_PRIORITY, t.getPriority());
	    values.put(PIN_STATUSCODE, t.getStatusCode());
	    values.put(PIN_TEAM, t.getTeamId());
	    values.put(PIN_TITLE, t.getTitle());
	    values.put(PIN_CREATEDBY, t.getCreatedBy());
	    
	    library_id = db.insertWithOnConflict(TABLE_PIN, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		
		return library_id;
	}

	public PinModel getPin(String PinId) {
	    SQLiteDatabase db = this.getReadableDatabase();

	    String selectQuery = "SELECT  * FROM " + TABLE_PIN + " WHERE " + PIN_ID + " = '" + PinId + "'";

	    Log.d(TAG, "Getting Pin : " + selectQuery);

	    Cursor c = db.rawQuery(selectQuery, null);

	    if (c != null)
	        c.moveToFirst();
	    
	    PinModel t = new PinModel();
	    t.setAssignee(c.getString(c.getColumnIndex(PIN_ASSIGNEE)));
	    t.setDescription(c.getString(c.getColumnIndex(PIN_DESC)));
	    t.setDueDate(c.getString(c.getColumnIndex(PIN_DUE)));
	    t.setPinId(c.getString(c.getColumnIndex(PIN_ID)));
	    t.setPriority(c.getInt(c.getColumnIndex(PIN_PRIORITY)));
	    t.setStatusCode(c.getInt(c.getColumnIndex(PIN_STATUSCODE)));
	    t.setTeamId(c.getString(c.getColumnIndex(PIN_TEAM)));
	    t.setTitle(c.getString(c.getColumnIndex(PIN_TITLE)));
	    t.setCreatedBy(c.getString(c.getColumnIndex(PIN_CREATEDBY)));

	    return t;
	}

	public ArrayList<PinModel> getPinByTeam(String teamId) {
	    ArrayList <PinModel> us = new ArrayList <PinModel> ();
	    String selectQuery = "SELECT * FROM " + TABLE_PIN + " WHERE " + PIN_TEAM + "='" + teamId + "' "+
	    		"ORDER BY " + PIN_DUE;

	    Log.d(TAG, "Getting Pin By Team : " + selectQuery);

	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);

	    if (c.moveToFirst()) {
	        do {
	        	PinModel t = new PinModel();
	    	    t.setAssignee(c.getString(c.getColumnIndex(PIN_ASSIGNEE)));
	    	    t.setDescription(c.getString(c.getColumnIndex(PIN_DESC)));
	    	    t.setDueDate(c.getString(c.getColumnIndex(PIN_DUE)));
	    	    t.setPinId(c.getString(c.getColumnIndex(PIN_ID)));
	    	    t.setPriority(c.getInt(c.getColumnIndex(PIN_PRIORITY)));
	    	    t.setStatusCode(c.getInt(c.getColumnIndex(PIN_STATUSCODE)));
	    	    t.setTeamId(c.getString(c.getColumnIndex(PIN_TEAM)));
	    	    t.setTitle(c.getString(c.getColumnIndex(PIN_TITLE)));
	    	    t.setCreatedBy(c.getString(c.getColumnIndex(PIN_CREATEDBY)));
	            us.add(t);
	        } while (c.moveToNext());
	    }
	    return us;
	}
	// #endregion
	
	public void clearAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        
        Log.d(TAG, "Deleting Everything !");
        
        db.delete(TABLE_LIBRARY, null, null);
        db.delete(TABLE_NEWS, null, null);
        db.delete(TABLE_PIN, null, null);
        db.delete(TABLE_PROFILE, null, null);
        db.delete(TABLE_TEAM, null, null);
        db.delete(TABLE_USER, null, null);
        db.delete(TABLE_USERTEAM, null, null);
        db.delete(TABLE_MESSAGE, null, null);
        db.close();
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