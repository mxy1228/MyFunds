/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package funds.xmy.com.myfunds.db;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import de.greenrobot.event.EventBus;
import funds.xmy.com.myfunds.model.FundInfo;
import funds.xmy.com.myfunds.model.FundTradeInfo;

/**
 * Created by xumengyang01 on 2015/2/27.
 */
public class FundDBManager {

    private FundHelper mFundHelper;

    private static FundDBManager mManager;

    public static FundDBManager getManager(Context ctx){
        if(mManager == null){
            synchronized (FundDBManager.class){
                if(mManager == null){
                    mManager = new FundDBManager(ctx);
                }
            }
        }
        return mManager;
    }

    private FundDBManager(Context ctx){
        this.mFundHelper = new FundHelper(ctx);
    }

    /**
     * 保存FundInfo信息到DB
     * @param info
     */
    public void saveFundInfo(FundTradeInfo info){
        try {
            this.mFundHelper.getTradeDao().create(info);
            List<FundTradeInfo> infos = this.mFundHelper.getTradeDao().queryBuilder().where().eq("fund_id", info.fund_id).query();
            if(infos != null){
                FundInfo fundInfo = new FundInfo();
                for(FundTradeInfo fi : infos){
                    fundInfo.total_cost += Float.valueOf(fi.cost);
                    fundInfo.total_portion += Float.valueOf(fi.portion);
                }
                fundInfo.cur_rate = fundInfo.total_cost / fundInfo.total_portion;
                fundInfo.fund_id = info.fund_id;
                fundInfo.name = info.name;
                saveOrUpdateFundInfo(fundInfo);
                //发送通知同步该基金的净值
                EventBus.getDefault().post(fundInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveOrUpdateFundInfo(FundInfo info){
        try {
            this.mFundHelper.getFundDao().createOrUpdate(info);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<FundInfo> queryAllFundInfo(){
        try {
            return this.mFundHelper.getFundDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
