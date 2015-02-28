/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package funds.xmy.com.myfunds.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;
import funds.xmy.com.myfunds.BaseActivity;
import funds.xmy.com.myfunds.R;
import funds.xmy.com.myfunds.db.FundDBManager;
import funds.xmy.com.myfunds.model.FundInfo;
import funds.xmy.com.myfunds.model.UpdateFundsListEvent;


public class MainActivity extends BaseActivity {

    private ListView mLV;
    private MyFundsAdapter mAdapter;
    private TextView mTotalProfitTV;
    private TextView mTotalCostTV;

    private List<FundInfo> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        this.mLV = (ListView)findViewById(R.id.main_lv);
        this.mTotalCostTV = (TextView)findViewById(R.id.main_total_cost_tv);
        this.mTotalProfitTV = (TextView)findViewById(R.id.main_total_profit_tv);
    }

    @Override
    protected void initData() {
        this.mData = FundDBManager.getManager(this).queryAllFundInfo();
        this.mLV.setEmptyView(LayoutInflater.from(this).inflate(R.layout.lv_empty_view,null));
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mData = FundDBManager.getManager(this).queryAllFundInfo();
        if(mAdapter == null){
            this.mAdapter = new MyFundsAdapter(this,mData);
            this.mLV.setAdapter(mAdapter);
        }else{
            mAdapter.updateData(mData);
        }
        float totalCost = 0;
        float totalProfit = 0;
        for(FundInfo info : mData){
            totalCost += info.total_cost;
            totalProfit += info.total_profit;
        }
        mTotalProfitTV.setText(getString(R.string.total_profit,totalProfit+""));
        mTotalCostTV.setText(getString(R.string.total_buy,totalCost+""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_buy) {
            Intent intent = SearchActivity.createSearchActivityIntent(this);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
