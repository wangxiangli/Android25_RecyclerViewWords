package com.bush.android25_recyclerviewwords.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "db_words.db";
    private final static int VERSION = 1;
    private SQLiteDatabase dbConn = null;

    /**
     * 构造方法，实现数据库操作对象的初始化
     * 当执行构造方法，其实完成了数据库的创建
     * @param context
     */
    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        // 初始化数据库操作对象
        dbConn = getReadableDatabase();
    }

    /**
     * 创建数据库中的
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_words(_id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "word , detail)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tb_newwords(_id INTEGER PRIMARY KEY AUTOINCREMENT " +
                ", word , detail)");
    }

    /**
     * 对于数据库中的表进行更新
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS tb_words");
            db.execSQL("DROP TABLE IF EXISTS tb_newwords");
            onCreate(db);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下为封装的需要sql语句的CRUD方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 查询返回Cursor
     * @param sql
     * @param selectionArgs
     * @return
     */
    public Cursor selectCursor(String sql, String[] selectionArgs) {
        return dbConn.rawQuery(sql, selectionArgs);
    }

    /**
     * 查询条数
     * @param sql 格式如：select id from 表名
     * @param selectionArgs
     * @return
     */
    //
    public int selectCount(String sql, String[] selectionArgs) {
        Cursor cursor = dbConn.rawQuery(sql, selectionArgs);
        int count = cursor.getCount();
        if (cursor != null) {
            cursor.close();
        }
        return count;
    }

    /**
     * 查询返回集合
     * @param sql
     * @param selectionArgs
     * @return
     */
    public List<Map<String, String>> selectList(String sql, String[] selectionArgs) {
        Cursor cursor = dbConn.rawQuery(sql, selectionArgs);
        return cursorToList(cursor);
    }

    /**
     * Cursor转集合
     * @param cursor
     * @return
     */
    public List<Map<String, String>> cursorToList(Cursor cursor) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        while (cursor.moveToNext()) {
            // 对于每一行数据进行操作
            Map<String, String> map = new HashMap<String, String>();
            // 对于每行数据的每列进行操作
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                map.put(cursor.getColumnName(i), cursor.getString(i));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 执行增删改，返回boolean
     * @param sql
     * @param bindArgs
     * @return
     */
    public boolean execData(String sql, Object[] bindArgs) {
        try {
            if (bindArgs == null) {
                dbConn.execSQL(sql);
            } else {
                dbConn.execSQL(sql, bindArgs);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 以下为封装的多参CRUD方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 添加数据，返回添加后该数据在表中的id
     * @param table
     * @param nullColumnHack
     * @param values
     * @return
     */
    public long insertData(String table, String nullColumnHack, ContentValues values) {
        return dbConn.insert(table, nullColumnHack, values);
    }

    /**
     * 执行删除，返回受影响的行数
     * @param table
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int deleteData(String table, String whereClause, String[] whereArgs) {
        return dbConn.delete(table, whereClause, whereArgs);
    }

    /**
     * 执行更新，返回受影响的行数
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public int updateData(String table, ContentValues values, String whereClause, String[]
            whereArgs) {
        return dbConn.update(table, values, whereClause, whereArgs);
    }

    /**
     * 执行查询，返回Cursor
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return
     */
    public Cursor queryData(String table, String[] columns,
                            String selection, String[] selectionArgs, String groupBy,
                            String having, String orderBy, String limit) {
        return dbConn.query(table, columns, selection, selectionArgs, groupBy, having, orderBy,
                limit);
    }

    /**
     * 执行查询，返回集合
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return
     */
    public List<Map<String, String>> queryList(String table, String[] columns,
                            String selection, String[] selectionArgs, String groupBy,
                            String having, String orderBy, String limit) {
        Cursor cursor = dbConn.query(table, columns, selection, selectionArgs, groupBy, having, orderBy,
                limit);
        return cursorToList(cursor);
    }


    /**
     * 关闭数据库连接，便于GC回收对象
     */
    public void destroy() {
        if (dbConn != null) {
            dbConn.close();
        }
    }
}
