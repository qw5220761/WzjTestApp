package com.example.drop.wzjtestapp.database.db;

import com.example.drop.wzjtestapp.database.bean.BaseResult;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by liang_xs on 2017/11/28.
 */
@Entity
public class SearchSave extends BaseResult {
    @Id(autoincrement = true)
    private Long _id;//主键  自增长

    private String name;

    @Generated(hash = 1018316693)
    public SearchSave(Long _id, String name) {
        this._id = _id;
        this.name = name;
    }

    @Generated(hash = 456883572)
    public SearchSave() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
