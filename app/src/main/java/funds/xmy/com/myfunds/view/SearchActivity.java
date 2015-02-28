/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package funds.xmy.com.myfunds.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import funds.xmy.com.myfunds.BaseActivity;
import funds.xmy.com.myfunds.R;
import funds.xmy.com.myfunds.controller.FundsController;
import funds.xmy.com.myfunds.db.FundDBManager;
import funds.xmy.com.myfunds.model.FundTradeInfo;

/**
 * Created by xumengyang01 on 2015/2/27.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener,TextView.OnEditorActionListener{

    private EditText mET;
    private TextView mFundNameTV;

    private FundsController mFundsController;
    private FundTradeInfo mInfo;

    public static Intent createSearchActivityIntent(Context ctx){
        Intent intent = new Intent(ctx,SearchActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
    }

    @Override
    protected void initView() {
        this.mET = (EditText)findViewById(R.id.search_et);
        this.mFundNameTV = (TextView)findViewById(R.id.search_fund_name_tv);
    }

    @Override
    protected void initData() {
        this.mFundsController = new FundsController();
    }

    @Override
    protected void initEvent() {
        this.mFundNameTV.setOnClickListener(this);
        this.mET.setOnEditorActionListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_fund_name_tv:
                if(!TextUtils.isEmpty(mFundNameTV.getText().toString())){
                    Intent intent = BuyFundActivity.createBuyFundActivityIntent(SearchActivity.this,mInfo);
                    startActivityForResult(intent, BuyFundActivity.RECORD_FUND_SUCCESS);
                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId){
            case EditorInfo.IME_ACTION_SEARCH:
                String id = mET.getText().toString();
                if(!TextUtils.isEmpty(id)){
                    mFundsController.getFundNameByID(id);
                }
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BuyFundActivity.RECORD_FUND_SUCCESS){
            SearchActivity.this.finish();
        }
    }

    /**
     * 获得基金信息
     * @param info
     */
    public void onEventMainThread(FundTradeInfo info){
        if(info != null){
            this.mInfo = info;
            mFundNameTV.setText(info.name);
        }
    }
}
