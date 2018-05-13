package com.xzmc.zzzt.privateprotect.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xzmc.zzzt.privateprotect.Utils.Utils;
import com.xzmc.zzzt.privateprotect.bean.CategoryModel;
import com.xzmc.zzzt.privateprotect.bean.PostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 17/5/8.
 */

public class DBHelper extends SQLiteOpenHelper{
    private static SQLiteDatabase sqLiteDatabase;
    private static final int DB_VER = 1;
    private final String id_key = "id_key";
    private static DBHelper currentDBHelper;

    public DBHelper(Context context, String name, int version) {
        super(context, name, null,version);
        openSqLiteDatabase();
    }

    public synchronized static DBHelper getCurrentUserInstance(Context context) {
        String name = "chat_" + Utils.getID() + ".db3";
        currentDBHelper = new DBHelper(context, name, DB_VER);
        return currentDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String category = "create table " + Category.tableName + "(" + id_key
                + " Integer primary key," + Category.id + " text,"
                + Category.title + " text," + Category.description + " text)";
        db.execSQL(category);
        String post = "create table " + Post.tableName + "(" + id_key
                + " Integer primary key," + Post.id + " text," + Post.title
                + " text," + Post.content + " text," + Post.imageurl + " text,"
                + Post.source + " text," + Post.time + " text,"
                + Post.comment_count + " text," + Post.view_count + " text,"
                + Post.collection_count + " text," + Post.channel_id + " text,"
                + Post.collection + " text)";
        db.execSQL(post);
        String FRIEND_TABLE_SQL = "CREATE TABLE IF NOT EXISTS `friends` ("
                + "`id` INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "`friendid` VARCHAR(63) UNIQUE NOT NULL, "
                + "`object` BLOB NOT NULL)";
        db.execSQL(FRIEND_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String createTable = "create table " + Post.tableName + "(" + id_key
                + " Integer primary key," + Post.id + " text," + Post.title
                + " text," + Post.content + " text," + Post.imageurl + " text,"
                + Post.source + " text," + Post.time + " text,"
                + Post.comment_count + " text," + Post.view_count + " text,"
                + Post.collection_count + " text," + Post.channel_id + " text,"
                + Post.collection + " text)";
        db.execSQL(createTable);
        String category = "create table " + Category.tableName + "(" + id_key
                + " Integer primary key," + Category.id + " text,"
                + Category.title + " text," + Category.description + " text)";
        db.execSQL(category);
        FriendsTable.getInstance().createTable(db);

    }

    /**
     * 初始化数据库
     */
    public SQLiteDatabase openSqLiteDatabase() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = getWritableDatabase();
        }
        return sqLiteDatabase;
    }
    public void closeSqLiteDatabase() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }

    public void releaseSqliteDatabase() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
        sqLiteDatabase = null;
    }

    /**
     * 插入分类列表
     *
     * @param categories
     */
    public void insertCategories(JSONArray categories) {
        sqLiteDatabase.delete(Category.tableName, null, null);
        for (int j = 0; j < categories.length() && categories != null; j++) {
            ContentValues contentValues = new ContentValues();
            try {
                JSONObject categoryObj = categories.getJSONObject(j);
                String description = categoryObj.getString("description");
                String id = categoryObj.getString("categoryid");
                String title = categoryObj.getString("categoryName");
                contentValues.put(Category.description, description);
                contentValues.put(Category.id, id);
                contentValues.put(Category.title, title);

                sqLiteDatabase.insert(Category.tableName, null, contentValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public int insertPosts(JSONArray posts, String channel_id) {
        // sqLiteDatabase.delete(Post.tableName, null, null);
        int insertCount = 0;
        try {
            for (int i = 0; i < posts.length(); i++) {
                JSONObject obj = posts.getJSONObject(i);

                String id = obj.getString("newsID");
                String source = obj.getString("source");
                String title = obj.getString("title");
                String content = obj.getString("description");
                String imgurl = obj.getString("imgUrl");
                String time = obj.getString("time");
                String comment_count = obj.getString("commentCount");
                String view_count = obj.getString("viewCount");
                String collection_count = obj.getString("collectionCount");
                String iscollection = obj.getString("collection");

                ContentValues contentValues = new ContentValues();
                contentValues.put(Post.collection_count, collection_count);
                contentValues.put(Post.view_count, view_count);
                contentValues.put(Post.comment_count, comment_count);
                contentValues.put(Post.content, content);
                contentValues.put(Post.source, source);
                contentValues.put(Post.time, time);
                contentValues.put(Post.id, id);
                contentValues.put(Post.title, title);
                contentValues.put(Post.imageurl, imgurl);
                contentValues.put(Post.channel_id, channel_id);
                contentValues.put(Post.collection, iscollection);

                Cursor cursor = sqLiteDatabase
                        .query(Post.tableName, null, Post.id + " =? ",
                                new String[] { id }, null, null, null);
                if (cursor != null && cursor.getCount() == 1) {
                    PostModel model = new PostModel();
                    setModelValue(model, cursor, 0);
                    sqLiteDatabase.update(Post.tableName, contentValues,
                            Post.id + "=?", new String[] { id });
                } else {
                    sqLiteDatabase.insert(Post.tableName, null, contentValues);
                    insertCount++;
                }
                if (cursor != null && cursor.isClosed() == false) {
                    cursor.close();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return insertCount;
    }

    public List<PostModel> queryPostsByCategoryId(String categoryId,
                                                  String limit) {
        List<PostModel> list = new ArrayList<PostModel>();

        Cursor postCursor = sqLiteDatabase.query(Post.tableName, null,
                Post.channel_id + " =? ", new String[] { categoryId }, null,
                null, null, limit);

        for (int inner = 0; postCursor != null && inner < postCursor.getCount(); inner++) {
            PostModel model = new PostModel();
            setModelValue(model, postCursor, inner);
            list.add(model);
        }
        if (postCursor != null && postCursor.isClosed() == false) {
            postCursor.close();
        }
        if (postCursor != null && postCursor.isClosed() == false) {
            postCursor.close();
        }
        return list;
    }

    public void setModelValue(PostModel model, Cursor cursor, int cursorPosition) {
        cursor.moveToPosition(cursorPosition);
        int column = 1;
        model.setId(cursor.getString(column++));
        model.setTitle(cursor.getString(column++));
        model.setContent(cursor.getString(column++));
        model.setImageurl(cursor.getString(column++));
        model.setSource(cursor.getString(column++));
        model.setTime(cursor.getString(column++));
        model.setComment_count(cursor.getString(column++));
        model.setView_count(cursor.getString(column++));
        model.setCollection_count(cursor.getString(column++));
        model.setChannel_id(cursor.getString(column++));
        model.setCollection(cursor.getString(column++));
    }

    public List<CategoryModel> queryAllCategory() {
        List<CategoryModel> list = new ArrayList<CategoryModel>();
        Cursor cursor = sqLiteDatabase.query(Category.tableName, null, null,
                null, null, null, null);
        for (int i = 0; cursor != null && i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setDescription(cursor.getString(3));
            categoryModel.setId(cursor.getString(1));
            categoryModel.setTitle(cursor.getString(2));
            list.add(categoryModel);
        }
        cursor.close();
        return list;
    }
}
