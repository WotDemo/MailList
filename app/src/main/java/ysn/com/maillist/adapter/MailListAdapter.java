package ysn.com.maillist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ysn.com.maillist.R;
import ysn.com.maillist.bean.Mail;

/**
 * @Author yangsanning
 * @ClassName MailListAdapter
 * @Description 一句话概括作用
 * @Date 2019/2/18
 * @History 2019/2/18 author: description:
 */
public class MailListAdapter extends RecyclerView.Adapter<MailListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Mail> mailList;
    private Context context;

    public MailListAdapter(Context context, List<Mail> data) {
        inflater = LayoutInflater.from(context);
        mailList = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MailListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_mail, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvTag = view.findViewById(R.id.mail_item_tag);
        viewHolder.tvName = view.findViewById(R.id.mail_item_name);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MailListAdapter.ViewHolder holder, final int position) {
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(mailList.get(position).getLetter());
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

        }

        holder.tvName.setText(this.mailList.get(position).getName());

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, mailList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mailList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTag, tvName;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setNewDatas(List<Mail> list) {
        this.mailList = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return mailList.get(position);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return mailList.get(position).getLetter().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mailList.get(i).getLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}