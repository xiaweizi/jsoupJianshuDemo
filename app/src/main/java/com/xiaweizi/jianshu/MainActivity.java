package com.xiaweizi.jianshu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "result----->";
    @BindView(R.id.rv_jianshu)
    RecyclerView rvJianshu;
    @BindView(R.id.srl_jianshu)
    SwipeRefreshLayout srlJianshu;

    @BindView(R.id.activity_main)
    RelativeLayout activityMain;

    private Document document;

    private List<JianshuBean> mBeans;

    private JianshuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mBeans = new ArrayList<>();
        mAdapter = new JianshuAdapter(MainActivity.this);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        rvJianshu.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rvJianshu.setAdapter(mAdapter);
        rvJianshu.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.tv_title:
                    case R.id.tv_content:
                    case R.id.iv_primary:
                        Intent title = new Intent(MainActivity.this, ShowActivity.class);
                        title.putExtra("link", ((JianshuBean)adapter.getItem(position)).getTitleLink());
                        startActivity(title);
                        break;
                    case R.id.iv_avatar:
                    case R.id.tv_author:
                        Intent author = new Intent(MainActivity.this, ShowActivity.class);
                        author.putExtra("link", ((JianshuBean)adapter.getItem(position)).getAuthorLink());
                        startActivity(author);
                        break;
                    case R.id.tv_collectTag:
                        Intent collect = new Intent(MainActivity.this, ShowActivity.class);
                        collect.putExtra("link", ((JianshuBean)adapter.getItem(position)).getCollectionTagLink());
                        startActivity(collect);
                        break;
                    default:
                        break;
                }
            }
        });

        jsoupData();

        srlJianshu.setColorSchemeColors(Color.RED, Color.YELLOW);
        srlJianshu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jsoupData();
            }
        });


    }

    private void jsoupData() {
        srlJianshu.setRefreshing(true);
        mBeans.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    document = Jsoup.connect("http://www.jianshu.com/")
                            .timeout(10000)
                            .get();
                    Elements noteList = document.select("ul.note-list");
                    Elements li = noteList.select("li");
                    for (Element element : li) {
                        JianshuBean bean = new JianshuBean();
                        bean.setAuthorName(element.select("div.name").text()); // 作者姓名
                        bean.setAuthorLink(element.select("a.blue-link").attr("abs:href")); // 作者首页链接
                        bean.setTime(timeChange(element.select("span.time").attr("data-shared-at")));   // 发表时间
                        bean.setPrimaryImg(element.select("img").attr("src"));  // 主图
                        bean.setAvatarImg(element.select("a.avatar").select("img").attr("src")); // 头像

                        bean.setTitle(element.select("a.title").text());    // 标题
                        bean.setTitleLink(element.select("a.title").attr("abs:href")); // 标题链接

                        bean.setContent(element.select("p.abstract").text());       // 内容
                        bean.setCollectionTagLink(element.select("a.collection-tag").attr("abs:href")); // 专题链接

                        String[] text = element.select("div.meta").text().split(" ");
//                        str.matches("[0-9]+");
                        if (text[0].matches("[0-9]+")) {
                            bean.setReadNum(text[0]);
                            bean.setTalkNum(text[1]);
                            bean.setLikeNum(text[2]);
                        } else {
                            bean.setCollectionTag(text[0]);
                            bean.setReadNum(text[1]);
                            bean.setTalkNum(text[2]);
                            bean.setLikeNum(text[3]);

                        }
                        mBeans.add(bean);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setNewData(mBeans);
                            srlJianshu.setRefreshing(false);
                        }
                    });
                    Log.i(TAG, "run: " + mBeans.get(0).toString());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "run: " + e.getMessage());
                }

            }
        }).start();
    }

    private String timeChange(String time){
        String[] ts = time.split("T");
        String[] split = ts[1].split("\\+");
        return ts[0] + "    " +  split[0];
    }


}
