package com.example.homeloan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "AddUserDetails";
    private static final int DB_VERSION = 5;
    private static final String F_NAME = "f_name";
    private static final String L_NAME = "l_name";
    private static final String CONTACT = "contact";
    private static final String PROJECTNAME = "project_name";
    private static final String FLATDETAILS = "flat_details";
    private static final String PROPERTYCOST = "property_cost";
    private static final String LOANREQUIREMENT = "loan_requirement";
    private static final String STATE = "state";
    private static final String CITY = "city";
    private static final String ADD_USER = "User";
    private static final String ADD_IMAGES = "Add_Images";
    private static final String ID_COL = "id";
    private static final String IMAGES_PATH = "path";
    Context context;
    public DataBaseHelper( Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }
   private static String addUserQuery = "CREATE TABLE " +ADD_USER  + " ("
            + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + F_NAME + " TEXT,"
            + L_NAME + " TEXT,"
            + CONTACT + " TEXT,"
            + PROJECTNAME + " TEXT,"
            + FLATDETAILS + " TEXT,"
            + PROPERTYCOST + " TEXT,"
            + STATE + " TEXT,"
            + CITY + " TEXT,"
            + LOANREQUIREMENT + " TEXT)";
   private static String addImagesQuery = "CREATE TABLE " +ADD_IMAGES  + " ("
            + ID_COL + " TEXT, "
            + IMAGES_PATH + " BLOB)";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(addUserQuery);
        sqLiteDatabase.execSQL(addImagesQuery);
        Log.d("table","hhh");
    }
    public boolean addUser(String fName, String lName, String contact, String projectName, String flatDetails, String propertyCost, String loadRequirement, String state, String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(F_NAME,fName);
        values.put(L_NAME,lName);
        values.put(CONTACT,contact);
        values.put(PROJECTNAME,projectName);
        values.put(FLATDETAILS,flatDetails);
        values.put(PROPERTYCOST,propertyCost);
        values.put(LOANREQUIREMENT,loadRequirement);
        values.put(STATE,state);
        values.put(CITY,city);
        long result= db.insert(ADD_USER,null,values);
        db.close();

        return result != -1;

    }
    public Boolean getPhoneNumberList(String phoneNumber){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor;
        Boolean numberExist=false;
        cursor = db.rawQuery("SELECT "+CONTACT +" FROM " + ADD_USER , null);

        ArrayList phoneNumberList = new ArrayList<>();
        // moving our cursor to first position.
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    phoneNumberList.add(cursor.getString(0));
                } while (cursor.moveToNext());
                // moving our cursor to next.
            }
        }
        assert cursor != null;
        cursor.close();
        for (int i=0;i<phoneNumberList.size();i++){
            if(phoneNumberList.get(i).equals(phoneNumber)){
                numberExist=true;
            }
        }
        return numberExist;
    }
    public ArrayList<User> getListOfUser(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + ADD_USER , null);
        ArrayList<User> arrayList = new ArrayList<>();
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    arrayList.add(new User(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9)));

                } while (cursor.moveToNext());

            }

        }
        assert cursor != null;
        cursor.close();

        return arrayList;

    }
    public ArrayList<GraphList> fetchStateCount(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT state,COUNT(*) as \"count\" FROM " + ADD_USER +" GROUP BY state" , null);
        ArrayList<GraphList> arrayList=new ArrayList<>();
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    arrayList.add(new GraphList(cursor.getString(0),
                            cursor.getString(1)));
                } while (cursor.moveToNext());

            }

        }
        assert cursor != null;
        cursor.close();
        return arrayList;
        }

    public ArrayList<User> getIndividualUserDetails(String id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + ADD_USER +" WHERE id='" + id + "'", null);
        ArrayList<User> arrayList = new ArrayList<>();
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    arrayList.add(new User(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9)));

                } while (cursor.moveToNext());

            }

        }
        assert cursor != null;
        cursor.close();

        return arrayList;

    }

    public ArrayList<byte[]> getImageList(String id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + ADD_IMAGES +" WHERE id='" + id + "'", null);
        ArrayList<byte[]> arrayList = new ArrayList<>();
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    arrayList.add(cursor.getBlob(1));

                } while (cursor.moveToNext());

            }

        }
        assert cursor != null;
        cursor.close();

        return arrayList;

    }

    public void deleteUser(String idVal){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(ADD_USER,"id=?",new String[]{idVal});
        db.close();

    }
    public Boolean addImages(String filePath){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + ADD_USER, null);
        String id = null;
        if(cursor.moveToLast()){
            id = cursor.getString(0);

        }
        assert cursor != null;
        cursor.close();

        Bitmap bitmap= BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        ContentValues contentValues=new ContentValues();
        contentValues.put(ID_COL,id);
        contentValues.put(IMAGES_PATH,bytes);
        long result= db.insert(ADD_IMAGES,null,contentValues);
        db.close();

        return result != -1;

        }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
