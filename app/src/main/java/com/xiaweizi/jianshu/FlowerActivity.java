package com.xiaweizi.jianshu;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlowerActivity extends AppCompatActivity {
    private static final String TAG = "FlowerActivity::";
    private static final String FLOWER_BASE_URL = "https://www.huabaike.com/";

    @BindView(R.id.rv_flower)
    RecyclerView rvFlower;
    @BindView(R.id.srl_flower)
    SwipeRefreshLayout srlFlower;

    @BindView(R.id.activity_flower)
    RelativeLayout activityFlower;

    private List<FlowersBean> mBeans;
    private FlowerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_flower);
        ButterKnife.bind(this);
        mBeans = new ArrayList<>();
        mAdapter = new FlowerAdapter(FlowerActivity.this);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        rvFlower.setLayoutManager(new GridLayoutManager(FlowerActivity.this, 2));
        rvFlower.setAdapter(mAdapter);
        flowerData();
        srlFlower.setColorSchemeColors(Color.RED, Color.YELLOW);
        srlFlower.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flowerData();
            }
        });
    }

    private void flowerData() {
        srlFlower.setRefreshing(true);
        mBeans.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(FLOWER_BASE_URL + "hhdq/").timeout(10000).get();
                    Elements imageList = document.select("div.zhiwuImg");
                    Elements li = imageList.select("li");
                    for (Element element : li) {
                        FlowersBean bean = new FlowersBean();
                        bean.imgUrl = FLOWER_BASE_URL + element.select("img").attr("src");
                        bean.title = element.select("img").attr("title");
                        mBeans.add(bean);
                        Log.i(TAG, "data: " + bean.toString());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setNewData(mBeans);
                            srlFlower.setRefreshing(false);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "run: ", e);
                }
            }
        }).start();
    }
}
