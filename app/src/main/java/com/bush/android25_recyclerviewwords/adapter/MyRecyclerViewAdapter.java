package com.bush.android25_recyclerviewwords.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bush.android25_recyclerviewwords.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 嘉华盛世 on 2016-07-29.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Map<String, String>> list;
    private LayoutInflater inflater;
    private Context mContext;
    private OnItemClickedListener listener;
    private RecyclerView recyclerView;
    public interface OnItemClickedListener{
        void onItemclicked(int position);
        boolean onItemLongClicked(int position);
    }

    public void setListener(OnItemClickedListener listener) {
        this.listener = listener;
    }

    public MyRecyclerViewAdapter(List<Map<String, String>> list, Context context,RecyclerView recyclerView) {
        this.list = list;
        mContext = context;
        this.recyclerView=recyclerView;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_recyclerview_main, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).tvMainId.setText(list.get(position).get("_id"));
        ((MyViewHolder)holder).tvMainDetail.setText(list.get(position).get("detail"));
        ((MyViewHolder)holder).tvMainWord.setText(list.get(position).get("word"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        long id=Long.parseLong(list.get(position).get("_id"));
        return id;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMainId;

        TextView tvMainWord;

        TextView tvMainDetail;
        public MyViewHolder(final View itemView) {
            super(itemView);
            tvMainId= (TextView) itemView.findViewById(R.id.tv_main_id);
            tvMainWord= (TextView) itemView.findViewById(R.id.tv_main_word);
            tvMainDetail= (TextView) itemView.findViewById(R.id.tv_main_detail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=recyclerView.getChildAdapterPosition(itemView);
                    listener.onItemclicked(position);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position=recyclerView.getChildAdapterPosition(itemView);
                     return  listener.onItemLongClicked(position);

                }
            });
        }
    }
    // 完整的更新数据
    public  void reloadRecyclerView(List<Map<String ,String >> _list,boolean isClean){
       if (isClean){
           list.clear();
       }
        list.addAll(_list);
        notifyDataSetChanged();
    }
    //局部更新数据，添加一条数据

    public void addItem(int position ,Map<String,String > map){
        list.add(map);
        notifyItemInserted(position);
    }
    //局部更新数据，添加多条数据
    public void addItems(List<Map<String, String>> _list, int positionStart) {
        list.addAll(_list);
        int itemCount = list.size();
        notifyItemRangeInserted(positionStart, itemCount);
    }
    //局部更新数据，删除一条数据
    public void removeItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }
    //局部更新数据，删除多条数据
    public void removeItems(int position,List<Map<String,String>> _list){
        list.remove(_list);
        int numCount=_list.size();
        notifyItemRangeRemoved(position, numCount);
    }

    //局部更新数据，更新一条数据
    public void updateItem(int position,Map<String,String> map){
        list.remove(position);
        list.add(map);
        notifyItemChanged(position);
    }

}
