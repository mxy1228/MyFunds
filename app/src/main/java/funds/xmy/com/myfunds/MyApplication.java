/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package funds.xmy.com.myfunds;

import android.app.Application;
import android.content.Context;

/**
 * Created by xumengyang01 on 2015/2/28.
 */
public class MyApplication extends Application{

    public static Context mAppCtx;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mAppCtx = getApplicationContext();
    }


}
