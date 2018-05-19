package com.example.levine.gitmakerautcom;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/9 0009.
 * 列表多样item基类
 */

public class BaseEntity<T> implements MultiItemEntity, Serializable {
    //item类型
    public static final int ITEMTYPE1 = 1;
    public static final int ITEMTYPE2 = 2;
    public static final int ITEMTYPE3 = 3;
    public static final int ITEMTYPE4 = 4;
    public static final int ITEMTYPE5 = 5;
    public static final int ITEMTYPE6 = 6;
    public static final int ITEMTYPE7 = 7;
    public static final int ITEMTYPE8 = 8;
    public static final int ITEMTYPE9 = 9;
    public static final int ITEMTYPE10 = 10;
    //item权重
    public static final int SPAN1 = 1;
    private int itemType;
    private int spanSize;
    private T t;
    private boolean isCheck;
    //用于记录共同的id，解决item组的数据获取
    private int id_1 = ID_NULL;
    private int id_2 = ID_NULL;
    private int id_3 = ID_NULL;
    private int id_4 = ID_NULL;
    private int id_5 = ID_NULL;
    public static final int ID_NULL = -100;

    private String name;
    private String id;

    /**
     * @param itemType item类型
     * @param spanSize item占比
     * @param t        数据
     */
    public BaseEntity(int itemType, int spanSize, T t) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.t = t;
    }

    public BaseEntity() {

    }

    public BaseEntity(int itemType) {
        this.itemType = itemType;
    }

    public BaseEntity(int itemType, T t) {
        this.itemType = itemType;
        this.t = t;
    }

    public void setData(T t) {
        this.t = t;
    }

    public int getId_1() {
        return id_1;
    }

    public void setId_1(int id_1) {
        this.id_1 = id_1;
    }

    public int getId_2() {
        return id_2;
    }

    public void setId_2(int id_2) {
        this.id_2 = id_2;
    }

    public int getId_3() {
        return id_3;
    }

    public void setId_3(int id_3) {
        this.id_3 = id_3;
    }

    public int getId_4() {
        return id_4;
    }

    public void setId_4(int id_4) {
        this.id_4 = id_4;
    }

    public int getId_5() {
        return id_5;
    }

    public void setId_5(int id_5) {
        this.id_5 = id_5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public T getData() {
        return t;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

}
