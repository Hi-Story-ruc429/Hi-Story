package com.history.db;

import com.history.Page;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PageInsert {

    private String tableName = "";
    private PreparedStatement pst = null;
    private DBconnect dbc = null;
    private Connection conn = null;
    public PageInsert(){
        dbc = new DBconnect();
        conn = dbc.getConnection();
        if (conn == null)
            System.out.println("conn is null");
    }


    public int insert(Page page){
        int result = 0 ; //未知异常,-1为主键重复异常,大于0为成功
        String sql = "INSERT INTO history.page " +
                "(id, url, title, content)," +
                " VALUES (?, ?, ?, ?)";
        try{
            pst = conn.prepareStatement(sql);
            pst.setInt(1,page.getId());
            pst.setString(2,page.getUrl());
            pst.setString(3,page.getTitle());
            pst.setString(4,page.getContent());
            result = pst.executeUpdate();

        }catch(Exception e){
            //捕获主键重复异常
            result = e instanceof MySQLIntegrityConstraintViolationException ?  -1 : 0;
            if (result != -1){  //别的异常,要打出
                e.printStackTrace();
            }

        }
        return result;
    }





    @Override
    protected void finalize() throws Throwable {
        if(dbc!=null){
            dbc.close();
        }
        super.finalize();
    }
}
