package com.xzmc.zzzt.privateprotect.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xzmc.zzzt.privateprotect.Utils.ParcelableUtil;
import com.xzmc.zzzt.privateprotect.base.App;
import com.xzmc.zzzt.privateprotect.bean.User;


public class FriendsTable {
	private static final String FRIENDID = "friendid";
	private static final String FRIEND_TABLE_SQL = "CREATE TABLE IF NOT EXISTS `friends` ("
			+ "`id` INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "`friendid` VARCHAR(63) UNIQUE NOT NULL, "
			+ "`object` BLOB NOT NULL)";
	public static final String FRIEND_ID = "friendid";
	public static final String OBJECT = "object";

	private static final String FRIEND_TABLE = "friends";
	public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS `friends`";

	private static FriendsTable roomsTable;
	private DBHelper dbHelper;

	private FriendsTable() {
		dbHelper = DBHelper.getCurrentUserInstance(App.ctx);
	}

	public synchronized static FriendsTable getInstance() {
		if (roomsTable == null) {
			roomsTable = new FriendsTable();
		}
		return roomsTable;
	}

	void createTable(SQLiteDatabase db) {
		db.execSQL(FRIEND_TABLE_SQL);
	}

	void dropTable(SQLiteDatabase db) {
		db.execSQL(DROP_TABLE_SQL);
	}

	public List<User> selectFriends() {
		List<User> friends = new ArrayList<User>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM friends", null);
		while (c.moveToNext()) {
			User friend = createFriendByCursor(c);
			friends.add(friend);
		}
		c.close();
		return friends;
	}

	static User createFriendByCursor(Cursor c) {
		byte[] msgBytes = c.getBlob(c.getColumnIndex(OBJECT));
		if (msgBytes != null) {
			User user = (User) ParcelableUtil.unmarshall(msgBytes,
					User.CREATOR);
			return user;
		} else {
			return null;
		}
	}

	public void insertFriends(List<User> friends) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (User friend : friends) {
				ContentValues cv = new ContentValues();
				cv.put(FRIEND_ID, friend.getID());
				cv.put(OBJECT, marshallMsg(friend));
				db.insert(FRIEND_TABLE, null, cv);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public byte[] marshallMsg(User user) {
		byte[] msgBytes = ParcelableUtil.marshall(user);
		if (msgBytes == null) {
			throw new NullPointerException("msg bytes is null");
		}
		return msgBytes;
	}

	public void deleteFriend(String friendid) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(FRIEND_TABLE, "friendid=?", new String[] { friendid });
	}

	public void deleteAllFriend(List<User> friends) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		for (User friend : friends) {
			db.delete(FRIEND_TABLE, "friendid=?",
					new String[] { friend.getID() });
		}

	}

	void close() {
		roomsTable = null;
	}
}
