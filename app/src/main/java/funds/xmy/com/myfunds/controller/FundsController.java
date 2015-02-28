/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package funds.xmy.com.myfunds.controller;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import funds.xmy.com.myfunds.MyApplication;
import funds.xmy.com.myfunds.db.FundDBManager;
import funds.xmy.com.myfunds.model.FundInfo;
import funds.xmy.com.myfunds.model.FundTradeInfo;
import funds.xmy.com.myfunds.model.UpdateFundsListEvent;

/**
 * Created by xumengyang01 on 2015/2/27.
 */
public class FundsController {

    public FundsController(){
        EventBus.getDefault().register(this);
    }

    /**
     * 抓取最新的基金净值
     * @param info
     */
    public void onEventBackgroundThread(FundInfo info){
        FundTradeInfo newInfo = requestInfoById(info.fund_id+"");
        info.last_rate = Float.valueOf(newInfo.drate);
        info.last_date = newInfo.date;
        info.total_profit = info.last_rate * info.total_portion - info.total_cost;
        FundDBManager.getManager(MyApplication.mAppCtx).saveOrUpdateFundInfo(info);
        EventBus.getDefault().post(new UpdateFundsListEvent());
    }

    /**
     * 根据基金代号搜索基金名称
     * @param id
     */
    public void getFundNameByID(final String id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                FundTradeInfo info = requestInfoById(id);
                EventBus.getDefault().post(info);
            }
        }){
        }.start();
    }

    /**
     * 根据购买金额、净值和手续费计算份额，这三个参数任何一个为空则返回空字符串，其中手续费可以为0
     * @param totalCostStr
     * @param rateStr
     * @param chargeStr
     * @return
     */
    public String caculatePortion(String totalCostStr,String rateStr,String chargeStr){
        Float cost;
        Float rate;
        Float charge;
        if(TextUtils.isEmpty(totalCostStr)){
            return "";
        }
        cost = Float.parseFloat(totalCostStr);
        if(TextUtils.isEmpty(rateStr)){
            return "";
        }
        rate = Float.parseFloat(rateStr);
        if(TextUtils.isEmpty(chargeStr)){
            charge = Float.parseFloat("0");
        }else{
            charge = Float.parseFloat(chargeStr) / 100;
        }
        float portion = cost * (1 - charge) / rate;
        return String.valueOf(portion);
    }


    /**
     * 将基金信息保存到DB
     * @param info
     */
    public void saveFundInfoIntoDB(Context ctx,FundTradeInfo info){
        FundDBManager.getManager(ctx).saveFundInfo(info);
    }

    private FundTradeInfo requestInfoById(String id){
        Document doc = null;
        try {
            doc = Jsoup.connect(getUrlByID(id)).timeout(60000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseFundInfo(doc);
    }

    private String getUrlByID(String id){
        return "http://www.howbuy.com/fund/"+id+"/index.htm?source=aladdin";
    }

    /**
     * 解析html获取FundInfo信息
     * @param doc
     * @return
     */
    private FundTradeInfo parseFundInfo(Document doc){
        FundTradeInfo info = new FundTradeInfo();
        String titleStr = doc.select("div.title").toString();
        if(!TextUtils.isEmpty(titleStr)){
            Document titleDoc = Jsoup.parse(titleStr);
            Elements titleEs = titleDoc.getElementsByTag("h1");
            if(titleEs != null){
                for(Element e : titleEs){
                    String nameandid = e.getElementsByTag("h1").text();
                    String rex = "[()]+";
                    String[] values = nameandid.split(rex);
                    if(values != null && values.length == 2){
                        info.name = values[0];
                        info.fund_id = Integer.valueOf(values[1]);
                    }else{
                        info.name = "";
                        info.fund_id = 0;
                    }
                    Log.d("html", "name=" + info.name);
                }
            }
        }
        String drateStr = doc.select("div.shouyi-b.shouyi-l.b1").toString();
        if(!TextUtils.isEmpty(drateStr)){
            Document drateDoc = Jsoup.parse(drateStr);
            Elements drateEs = drateDoc.getElementsByTag("div");
            if(drateEs != null){
                info.drate = drateEs.get(0).getElementsByClass("dRate").text();
                Log.d("html","value = "+info.drate);
                String tmpDate = drateEs.get(0).getElementsByClass("b-0").text();
                Log.d("html","date = "+tmpDate);
                info.date = tmpDate.replaceAll("^.*\\[", "").replaceAll("].*", "");
            }
        }
        return info;
    }

}
