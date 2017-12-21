package com.example.wpx.framework.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wpx.framework.R;
import com.example.wpx.framework.adapter.TestLvAtAdapter;
import com.example.wpx.framework.config.FileConfig;
import com.example.wpx.framework.http.Observer.GetOrPostListener;
import com.example.wpx.framework.http.RetrofitClient;
import com.example.wpx.framework.http.model.TestModel;
import com.example.wpx.framework.ui.base.BaseActivity;
import com.example.wpx.framework.ui.presenter.TestAtPresenter;
import com.example.wpx.framework.ui.view.ITestAtView;
import com.example.wpx.framework.util.LogUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h3>description</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class TestActivity extends BaseActivity<ITestAtView, TestAtPresenter> implements OnRefreshListener, OnLoadmoreListener, AdapterView.OnItemClickListener {

    private RefreshLayout mRefreshLayout;
    private ClassicsHeader mClassicsHeader;
    private ClassicsFooter mClassicsFooter;
    private ListView mListView;
    private TestLvAtAdapter mTestLvAtAdapter;
    List<String> list = new ArrayList<>();
    private int pageIndex;
    private int pageSize = 15;

    @Override
    protected TestAtPresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_test;
    }

    @Override
    protected void addFilters() {

    }

    @Override
    protected void onReceive(Context context, Intent intent) {

    }

    @Override
    protected void findView() {
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mClassicsHeader = (ClassicsHeader) mRefreshLayout.getRefreshHeader();
        mClassicsFooter = (ClassicsFooter) mRefreshLayout.getRefreshFooter();
        mListView = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadmoreListener(this);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void initIntentData() {

    }

    @Override
    protected void initData() {
        mTestLvAtAdapter = new TestLvAtAdapter(this, list);
        mListView.setAdapter(mTestLvAtAdapter);
        onRefreshData();
    }

    //下拉刷新监听
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        LogUtil.e("下拉刷新监听");
        onRefreshData();
    }

    //上拉加载更多
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        LogUtil.e("上拉加载更多");
        onLoadmoreData();
    }

    /**
     * 下拉刷新数据
     */
    private void onRefreshData() {
        pageIndex = 1;
        list.clear();
        for (int i = 0; i < pageIndex * pageSize; i++) {
            if (i == 0) {
                list.add("测试get请求");
            } else if (i == 1) {
                list.add("测试post请求");
            } else if (i == 2) {
                list.add("测试文件下载");
            } else {
                list.add("第" + (i + 1) + "条数据");
            }
        }
        mTestLvAtAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh();
    }

    /**
     * 上拉加载更多数据
     */
    private void onLoadmoreData() {
        for (int i = pageIndex * pageSize; i < (pageIndex + 1) * pageSize; i++) {
            list.add("第" + (i + 1) + "条数据");
        }
        mTestLvAtAdapter.notifyDataSetChanged();
        mRefreshLayout.finishLoadmore();
        pageIndex += 1;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        //get表单请求测试
        if (position == 0) {
            String methodName = "";
            Map<String, String> pargrams = new HashMap();
            pargrams.put("pageSize", "5");
            pargrams.put("curPage", "1");
            RetrofitClient.getInstance().get(methodName, pargrams, this, true, TestModel.class, new GetOrPostListener<TestModel>() {
                @Override
                public void onSuccess(TestModel testModel) {
                    LogUtil.e(testModel.toString());
                }
            });
        }
        //post表单请求测试
        else if (position == 1) {
            String methodName = "";
            Map<String, String> pargrams = new HashMap();
            pargrams.put("pageSize", "5");
            pargrams.put("curPage", "1");
            RetrofitClient.getInstance().post(methodName, pargrams, this, true, TestModel.class, new GetOrPostListener<TestModel>() {
                @Override
                public void onSuccess(TestModel testModel) {
                    LogUtil.e(testModel.toString());
                }
            });
        }
        //文件下载
        else if (position == 2) {
            String aplUrl = "http://121.37.17.177:27024/Apk/YGT_V1.2.apk";
            RetrofitClient.getInstance().downLoadFile(aplUrl, FileConfig.PATH_UPDATE_APK, new DownLoadListener() {
                @Override
                public void onProgress(long position, long fileSize) {
                    LogUtil.e("position=" + position + ",fileSize=" + fileSize);
                }

                @Override
                public void onDownLoadOver(File file) {
                    LogUtil.e("onDownLoadOver file.exists()="+file.exists());
                }
            });
        }
    }


}
