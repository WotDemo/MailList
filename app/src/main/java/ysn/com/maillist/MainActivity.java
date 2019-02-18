package ysn.com.maillist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ysn.com.maillist.adapter.MailListAdapter;
import ysn.com.maillist.bean.Mail;
import ysn.com.maillist.utils.PinyinComparator;
import ysn.com.maillist.utils.PinyinUtils;
import ysn.com.maillist.view.SideBar;

public class  MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SideBar sideBar;
    private TextView dialogTextView;
    private MailListAdapter adapter;
    private EditText filterEditText;
    private LinearLayoutManager linearLayoutManager;

    private List<Mail> mailList;

    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        pinyinComparator = new PinyinComparator();

        sideBar = findViewById(R.id.main_activity_side_bar);
        dialogTextView = findViewById(R.id.main_activity_dialog);
        sideBar.setTextView(dialogTextView);

        //设置右侧SideBar触摸监听
        sideBar.setOnLetterChangeListener(new SideBar.OnLetterChangedListener() {

            @Override
            public void onLetter(String letter) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    linearLayoutManager.scrollToPositionWithOffset(position, 0);
                }
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        mailList = setNewDatas(getResources().getStringArray(R.array.kings));

        // 根据a-z进行排序源数据
        Collections.sort(mailList, pinyinComparator);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MailListAdapter(this, mailList);
        recyclerView.setAdapter(adapter);
        //item点击事件
        adapter.setOnItemClickListener(new MailListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, ((Mail) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        filterEditText = findViewById(R.id.main_activity_filter);

        //根据输入框输入值的改变来过滤搜索
        filterEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private List<Mail> setNewDatas(String[] datas) {
        List<Mail> mailList = new ArrayList<>();

        for (String data : datas) {
            Mail mail = new Mail();
            mail.setName(data);
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(data);
            String letter = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (letter.matches("[A-Z]")) {
                mail.setLetter(letter.toUpperCase());
            } else {
                mail.setLetter("#");
            }

            mailList.add(mail);
        }
        return mailList;
    }

    /**
     * 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
     */
    private void filterData(String filterStr) {
        List<Mail> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mailList;
        } else {
            filterDateList.clear();
            for (Mail mail : mailList) {
                String name = mail.getName();
                if (name.contains(filterStr) ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr)
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr)
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr)
                ) {
                    filterDateList.add(mail);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.setNewDatas(filterDateList);
    }
}
