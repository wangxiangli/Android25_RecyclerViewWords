package com.bush.android25_recyclerviewwords;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bush.android25_recyclerviewwords.adapter.MyRecyclerViewAdapter;
import com.bush.android25_recyclerviewwords.helper.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerviewMain;

    private Context mContext = this;
    private MySQLiteOpenHelper dbHelper;
    private SQLiteDatabase dbConn;
    private MyRecyclerViewAdapter adapter;
    private List<Map<String,String>> totalList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerviewMain= (RecyclerView) findViewById(R.id.recyclerview_main);
        //初始化数据库
        initDB();

        initView();
        //更新数据，全局更新
        reloadRecyclerViewData();
    }

    private void reloadRecyclerViewData() {
        //查询数据，并按反序输出
        String sql="select * from tb_words order by _id desc";
        //查询的数据库结果
        List<Map<String,String>> list=dbHelper.selectList(sql,null);
        adapter.reloadRecyclerView(list,true);
    }

    private void initDB() {
        dbHelper = new MySQLiteOpenHelper(mContext);
        dbConn = dbHelper.getReadableDatabase();
    }

    private void initView() {
        final LinearLayoutManager manager = new LinearLayoutManager(
                mContext, LinearLayoutManager.VERTICAL, false);

        recyclerviewMain.setLayoutManager(manager);
        adapter=new MyRecyclerViewAdapter(totalList,mContext,recyclerviewMain);
        recyclerviewMain.setAdapter(adapter);
        adapter.setListener(new MyRecyclerViewAdapter.OnItemClickedListener() {
            @Override
            public void onItemclicked(final int position) {
                AlertDialog.Builder builder=new AlertDialog.Builder(mContext)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("确认删除吗？")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                long id=adapter.getItemId(position);
//                                dbConn.delete("tb_words","_id=?",new String[]{id+""});
//                                reloadRecyclerViewData();
                                adapter.removeItem(position);
                            }
                        });
                        builder.show();
            }

            //当长按是，返回的是false表示，长按的结束后还会触发单击事件，返回true不会再触发单击事件
            @Override
            public boolean onItemLongClicked(final int position) {
                AlertDialog.Builder builder=new AlertDialog.Builder(mContext)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("确认修改吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues values=new ContentValues();
                                values.put("word", "time");
                                values.put("detail", "时间");
                                long id=adapter.getItemId(position);
                                Log.i("------", "onClick: " + id);
                                dbConn.update("tb_words", values, "_id=?", new String[]{id + ""});

                            }
                        });
                builder.show();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setTitle("添加数据")
                        .setIcon(R.mipmap.ic_launcher);
                View view = getLayoutInflater().inflate(R.layout.alert_view, null);

                TextInputLayout textInputWord= (TextInputLayout) view.findViewById(R.id.textInput_word);

                TextInputLayout textInputDetail= (TextInputLayout) view.findViewById(R.id.textInput_detail);

                final EditText editText_word=textInputWord.getEditText();
                final EditText editText_detail=textInputDetail.getEditText();
                builder.setView(view);

                builder.setNegativeButton("取消", null);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String word =editText_word.getText()+"";
                        String detail =editText_detail.getText()+"";

                        ContentValues values=new ContentValues();
                        values.put("word",word);
                        values.put("detail", detail);
                        long id= dbConn.insert("tb_words",null,values);

                        if (id>0){
                            Toast.makeText(mContext,"insert ok",Toast.LENGTH_SHORT).show();
                            // 更新全部数据
                           // reloadRecyclerViewData();

                            //局部更新，将所有更新的数据填充到0的位置，其他的 顺序后延
                            Map<String ,String > map=new HashMap<String, String>();
                            map.put("_id",id+"");
                            map.put("word",word);
                            map.put("detail",detail);
                            adapter.addItem(0,map);
                        }else {
                            Toast.makeText(mContext,"insert wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
