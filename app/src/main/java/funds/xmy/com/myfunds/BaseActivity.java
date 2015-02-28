/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package funds.xmy.com.myfunds;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by xumengyang01 on 2015/2/27.
 */
public abstract class BaseActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
        initEvent();
        initData();
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();
}

