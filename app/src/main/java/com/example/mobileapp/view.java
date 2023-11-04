package com.example.mobileapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class view extends AppCompatActivity {

    ListView lst1;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        SQLiteDatabase db = openOrCreateDatabase("SliteDb",Context.MODE_PRIVATE,null);
        lst1 = findViewById(R.id.lst1);
        final Cursor c = db.rawQuery("select * from records",null);
        int id = c.getColumnIndex("id");
        int name = c.getColumnIndex("name");
        int location = c.getColumnIndex("location");
        int length = c.getColumnIndex("lenght");
        int level = c.getColumnIndex("level");
        int discription = c.getColumnIndex("discription");

        titles.clear();
        arrayAdapter = new ArrayAdapter(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,titles);
        lst1.setAdapter(arrayAdapter);
        final  ArrayList<UserEdit> stud = new ArrayList<UserEdit>();
        if(c.moveToFirst())
        {
            do{
                UserEdit stu = new UserEdit();
                stu.id = c.getString(id);
                stu.name = c.getString(name);
                stu.location = c.getString(location);
                stu.length = c.getString(length);
                stu.level = c.getString(level);
                stu.discription = c.getString(length);
                stud.add(stu);
                titles.add(c.getString(id) + " \t " + c.getString(name) + " \t "  + c.getString(location) + " \t "  + c.getString(length)+ c.getString(level)+ c.getString(discription) );
            } while(c.moveToNext());
            arrayAdapter.notifyDataSetChanged();
            lst1.invalidateViews();
        }
        lst1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String aa = titles.get(position).toString();
                UserEdit stu = stud.get(position);
                Intent i = new Intent(getApplicationContext(),edit.class);
                i.putExtra("id",stu.id);
                i.putExtra("name",stu.name);
                i.putExtra("location",stu.location);
                i.putExtra("length",stu.length);
                i.putExtra("length",stu.level);
                i.putExtra("length",stu.discription);
                startActivity(i);
            }
        });
    }
}