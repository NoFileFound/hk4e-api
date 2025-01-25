package org.httpsrv.database.entity;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;

@Entity(value = "goods", useDiscriminator = false)
public class Goods {
    @Id private String goods_id;
    private String goods_name;
    @Getter private String goods_owner;
    private String goods_name_i18n_key;
    private String goods_desc;
    private String goods_desc_i18n_key;
    private String goods_type; /// Normal, MonthlyCard
    private String goods_unit;
    private final String goods_icon = "https://sdk-webstatic.mihoyo.com/sdk-payment-upload/2022/09/07/0f362595da2e37a7a8fde1bb120656d2_594155779359709441.png";
    private String currency;
    private String price;
    private final String symbol = "￥";
    private String tier_id;
    private Object bonus_desc;
    private Object once_bonus_desc;
    private boolean available;
    private String tips_desc;
    private String tips_i18n_key;
    private String battle_pass_limit;
}