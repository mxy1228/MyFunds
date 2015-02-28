/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package funds.xmy.com.myfunds.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import funds.xmy.com.myfunds.Config;
import funds.xmy.com.myfunds.model.FundInfo;
import funds.xmy.com.myfunds.model.FundTradeInfo;

/**
 * Created by xumengyang01 on 2015/2/28.
 */
public class FundHelper extends OrmLiteSqliteOpenHelper{

    private Dao<FundInfo,Integer> mFundDao;
    private Dao<FundTradeInfo,Integer> mTradeDao;

    public FundHelper(Context ctx){
        super(ctx,Config.DBNAME,null,Config.DB_VERSION);
    }

    public FundHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, Config.DBNAME, factory, Config.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,FundInfo.class);
            TableUtils.createTable(connectionSource,FundTradeInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {

    }


    public Dao<FundInfo,Integer> getFundDao() throws SQLException{
        if(mFundDao == null){
            mFundDao = getDao(FundInfo.class);
        }
        return mFundDao;
    }

    public Dao<FundTradeInfo,Integer> getTradeDao() throws SQLException{
        if(mTradeDao == null){
            mTradeDao = getDao(FundTradeInfo.class);
        }
        return mTradeDao;
    }
}
