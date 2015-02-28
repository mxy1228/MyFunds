/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package funds.xmy.com.myfunds.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import de.greenrobot.event.EventBus;
import funds.xmy.com.myfunds.BaseActivity;
import funds.xmy.com.myfunds.R;
import funds.xmy.com.myfunds.controller.FundsController;
import funds.xmy.com.myfunds.db.FundDBManager;
import funds.xmy.com.myfunds.model.FundTradeInfo;
import funds.xmy.com.myfunds.model.UpdateFundsListEvent;

/**
 * Created by xumengyang01 on 2015/2/27.
 */
public class BuyFundActivity extends BaseActivity implements View.OnClickListener{

    private static final String FUND_INFO = "fund_info";
    public static final int RECORD_FUND_SUCCESS = 1;

    private FundTradeInfo mInfo;
    private MyTextWatcher mTextWatcher;
    private FundsController mFundsController;

    private TextView mNameTV;
    private EditText mDateET;
    private EditText mDrateET;
    private EditText mTotalCostET;
    private EditText mPortionET;//份额
    private EditText mChargeET;//手续费
    private DatePickerDialog mDateDialog;
    private Button mSaveBtn;
    private ProgressDialog mWatingDialog;

    public static Intent createBuyFundActivityIntent(Context ctx,FundTradeInfo info){
        Intent intent = new Intent(ctx,BuyFundActivity.class);
        intent.putExtra(FUND_INFO,info);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_fund_activity);
    }

    @Override
    protected void initView() {
        Calendar c = Calendar.getInstance();
        this.mNameTV = (TextView)findViewById(R.id.buy_fund_name_tv);
        this.mDateET = (EditText)findViewById(R.id.buy_fund_date_et);
        this.mDrateET = (EditText)findViewById(R.id.buy_fund_drate_et);
        this.mTotalCostET = (EditText)findViewById(R.id.buy_fund_cost_et);
        this.mPortionET = (EditText)findViewById(R.id.buy_fund_portion_et);
        this.mChargeET = (EditText)findViewById(R.id.buy_fund_charge_et);
        this.mDateDialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDateET.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
            }
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
        this.mSaveBtn = (Button)findViewById(R.id.buy_fund_save_btn);
        this.mWatingDialog = new ProgressDialog(this);
    }

    @Override
    protected void initData() {
        this.mInfo = (FundTradeInfo)getIntent().getSerializableExtra(FUND_INFO);
        if(mInfo != null){
            this.mNameTV.setText(mInfo.name);
        }
        this.mTextWatcher = new MyTextWatcher();
        this.mFundsController = new FundsController();
    }

    @Override
    protected void initEvent() {
        this.mDateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mDateDialog.show();
                }
            }
        });
        this.mSaveBtn.setOnClickListener(this);
        this.mTotalCostET.addTextChangedListener(new MyTextWatcher());
        this.mDrateET.addTextChangedListener(new MyTextWatcher());
        this.mChargeET.addTextChangedListener(new MyTextWatcher());
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
           case R.id.buy_fund_save_btn:
                if(!TextUtils.isEmpty(mPortionET.getText().toString())){
                    FundTradeInfo info = new FundTradeInfo();
                    info.fund_id = mInfo.fund_id;
                    info.name = mInfo.name;
                    info.date = mDateET.getText().toString();
                    info.drate = mDrateET.getText().toString();
                    info.portion = mPortionET.getText().toString();
                    info.cost = mTotalCostET.getText().toString();
                    FundDBManager.getManager(BuyFundActivity.this).saveFundInfo(info);
                    mWatingDialog.show();
                }
               break;
       }
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mPortionET.setText(mFundsController.caculatePortion(mTotalCostET.getText().toString()
                    ,mDrateET.getText().toString()
                    ,mChargeET.getText().toString()));
        }


    }

    public void onEventMainThread(UpdateFundsListEvent e){
        if(mWatingDialog.isShowing()){
            mWatingDialog.dismiss();
        }
        setResult(RECORD_FUND_SUCCESS);
        BuyFundActivity.this.finish();
    }
}
