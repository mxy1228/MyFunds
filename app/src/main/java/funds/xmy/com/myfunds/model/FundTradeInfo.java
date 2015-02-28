/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package funds.xmy.com.myfunds.model;

import java.io.Serializable;
import com.j256.ormlite.field.DatabaseField;
/**
 * Created by xumengyang01 on 2015/2/27.
 */
public class FundTradeInfo implements Serializable{

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public int fund_id;

    @DatabaseField
    public String name;

    @DatabaseField
    public String drate;

    @DatabaseField
    public String date;

    @DatabaseField
    public String portion;

    @DatabaseField
    public String cost;
}
