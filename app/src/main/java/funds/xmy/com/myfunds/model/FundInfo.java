/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package funds.xmy.com.myfunds.model;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by xumengyang01 on 2015/2/28.
 */
public class FundInfo implements Serializable{

    @DatabaseField (id = true)
    public int fund_id;

    @DatabaseField
    public String name;

    @DatabaseField
    public float total_cost;

    @DatabaseField
    public float total_portion;

    @DatabaseField
    public float total_profit;

    @DatabaseField
    public float cur_rate;

    @DatabaseField
    public float last_rate;

    @DatabaseField
    public String last_date;
}
