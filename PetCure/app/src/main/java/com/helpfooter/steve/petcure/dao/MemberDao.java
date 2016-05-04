package com.helpfooter.steve.petcure.dao;

import android.content.Context;

import com.helpfooter.steve.petcure.dataobjects.AbstractObj;
import com.helpfooter.steve.petcure.dataobjects.MemberObj;

/**
 * Created by steve on 2016/5/5.
 */
public class MemberDao  extends AbstractDao {

    public MemberDao(Context ctx) {
        super(ctx, "tb_member");
    }

    @Override
    void gotoCreateTableSql() {
        util.open();
        StringBuffer sql = new StringBuffer();

        sql.append("create table IF NOT EXISTS  tb_member " +
                "(id int,name varchar ,mobile varchar,photo varchar,openid varchar )");
        util.execSQL(sql.toString(), new Object[]{});
    }

    static boolean hascheckcreate = false;

    public void createTable(){
        if(hascheckcreate==false){
            gotoCreateTableSql();
            hascheckcreate=true;
        }
    }


    @Override
    public void insertObj(AbstractObj abobj) {
        util.open();

        MemberObj obj=(MemberObj)abobj;

        StringBuffer sql = new StringBuffer();
        sql.append("insert into tb_member (id,mobile  ,name,photo,openid ) values (?,?,?,?,?)");
        Object[] bindArgs = {obj.getId(),obj.getMobile(),obj.getName(),obj.getPhoto(),obj.getOpenid()};
        util.execSQL(sql.toString(), bindArgs);

        util.close();
    }

    @Override
    public void updateObj(AbstractObj abobj) {
        util.open();

        MemberObj obj=(MemberObj)abobj;

        StringBuffer sql = new StringBuffer();
        sql.append("update tb_member set mobile=?,name=?,photo=?,openid=? where id=? ");
        Object[] bindArgs = {obj.getMobile(),obj.getName(),obj.getPhoto(),obj.getOpenid(),obj.getId()};
        util.execSQL(sql.toString(),bindArgs);

        util.close();
    }

    @Override
    AbstractObj newRealObj() {
        return new MemberObj();
    }


}
