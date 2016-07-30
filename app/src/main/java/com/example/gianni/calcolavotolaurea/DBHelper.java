package com.example.gianni.calcolavotolaurea;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by gianni on 16/07/16.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG="DBHelper";

    public static final String DATABASE_NAME = "Voti.db";
    public static final String VOTI_TABLE_NAME = "Voti";
    public static final String VOTI_COLUMN_ID = "nome_materia";
    public static final String VOTI_COLUMN_VOTO = "voto";
    public static final String VOTI_COLUMN_CFU = "cfu";
    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // I create te structure of database
        db.execSQL(
                "create table Voti " +
                        "(nome_materia text primary key,voto text,cfu text);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Voti");
        onCreate(db);
    }

    public void deleteDB(List<materia> list){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(VOTI_TABLE_NAME,null,null);
    }

    public void deleteLastVoteFromDB(List<materia> list){

            Log.e(TAG,"Rimuovo :"+list.get(list.size()-1).nome_materia+" Esito: "+deleteContact(list.get(list.size()-1).nome_materia));
    }


    public boolean insertVoti  (String name_materia, String voto, String cfu)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome_materia", name_materia);
        contentValues.put("voto", voto);
        contentValues.put("cfu", cfu);
        db.insert("Voti", null, contentValues);

        Log.d(TAG,"Inserisco il voto nel database: "+name_materia+" "+voto+" "+cfu);
        return true;
    }

    public Cursor getData(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,VOTI_TABLE_NAME);
        return numRows;
    }

    public boolean updateVoto (String id, String nome_materia, String voto, String cfu)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome_materia", nome_materia);
        contentValues.put("voto", voto);
        contentValues.put("cfu", cfu);
        db.update("Voti", contentValues, "id = ? ", new String[] { id } );
        return true;
    }

    public Boolean deleteContact (String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();



        try {
            return db.delete(VOTI_TABLE_NAME,
                    "nome_materia='" + id+"'",
                    null) > 0;
        }catch(android.database.sqlite.SQLiteException e){

                //Probably i have a Database Miss
            Log.e(TAG,"Error: "+e.toString());

        }finally {
            return false;
        }

    }

    public ArrayList<materia> getAllVotes()
    {
        ArrayList<materia> array_list = new ArrayList<materia>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Voti", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            materia m=new materia();

           // array_list.add(res.getString(res.getColumnIndex(VOTI_COLUMN_ID)));
            m.nome_materia=res.getString(res.getColumnIndex(VOTI_COLUMN_ID));

            m.voto=Integer.parseInt(res.getString(res.getColumnIndex(VOTI_COLUMN_VOTO)));
            m.CFU=Integer.parseInt(res.getString(res.getColumnIndex(VOTI_COLUMN_CFU)));

            array_list.add(m);
            Log.d(TAG,"Estraggo dal database: "+m.nome_materia+" "+m.voto+" "+m.CFU);

            res.moveToNext();
        }
        return array_list;
    }
}