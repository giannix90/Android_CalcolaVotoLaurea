package com.example.gianni.calcolavotolaurea;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VotoLaureaActivity extends AppCompatActivity {

    Voti mVoti;
    DBHelper mydb;
    List<materia> listaM;
    NumberPicker ed1;
    EditText ed2;
    NumberPicker ed3;
    ListView listOfVotes;
    TextView header;

    Context context;
    List<String> tasks;
    ArrayAdapter<String> adapter;
    private static final String TAG="VotoLaureaActivity";
    private View dialogView;
    private LayoutInflater inflator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        context = this;
        tasks = new ArrayList<String>();



        mydb = new DBHelper(this);
        listaM=mydb.getAllVotes();
        Log.d(TAG,"Ho "+listaM.size()+" voti nel database");

        setContentView(R.layout.activity_voto_laurea);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        header=(TextView) findViewById(R.id.textView);


        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,tasks);
        listOfVotes=(ListView) findViewById(R.id.listView);
        assert listOfVotes != null;
        listOfVotes.setAdapter(adapter);

        if(listaM.size()>0) {

            materia tmp=new materia();
            //Fetch all votes from database
            for(int i=0;i<listaM.size();i++) {

                tmp=listaM.get(i);
                tasks.add("Materia: " + tmp.nome_materia+" Voto: "+tmp.voto+" CFU: "+tmp.CFU);
                Log.d(TAG, "Materia: " + tmp.nome_materia+" Voto: "+tmp.voto+" CFU: "+tmp.CFU);
            }

            // this method will refresh your listview manually
            adapter.notifyDataSetChanged();
        }

        mVoti=new Voti(listaM);

        header.setText("Voto previsto: "+mVoti.getVotoLaurea(3,3));

        ed1=(NumberPicker) findViewById(R.id.numberPicker3); //Voto
        ed2=(EditText) findViewById(R.id.editText2); //Materia
        ed3=(NumberPicker) findViewById(R.id.numberPicker4);   //CFU

/*=====Reset the field on click=======*/

        ed1.setMinValue(1);

        ed1.setMaxValue(33);

        ed3.setMinValue(1);

        ed3.setMaxValue(18);

        ed2.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v,boolean hasFocus){

                if(hasFocus) {

                    ed2.setText("");

                }
            }
        });



/*==========Add a new vote=============*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                Log.d(TAG,"Ho premuto il tasto add");



                    new AlertDialog.Builder(context)


                        //I inflate the box of custom title
                        .setTitle("Are you sure?")
                        .setMessage("Insert vote in a database?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with insert
                                mydb.insertVoti(ed2.getText().toString(),String.valueOf( ed1.getValue()), String.valueOf(ed3.getValue()));

                                //I add the new materia to the list
                                tasks.add("Materia: " + ed2.getText().toString()+" Voto: "+String.valueOf( ed1.getValue())+" CFU: "+String.valueOf(ed3.getValue()));
                                adapter.notifyDataSetChanged();
                                mVoti.addVoto(ed2.getText().toString(),ed1.getValue(),ed3.getValue());
                                listaM.add(listaM.size(),new materia(){
                                    String nome_materia=ed2.getText().toString();
                                    int CFU=ed1.getValue();
                                    int voto=ed3.getValue();
                                    }
                                );
                                header.setText("Voto previsto: "+mVoti.getVotoLaurea(3,3));

                                ed2.setText("");
                                ed1.setValue(1);
                                ed3.setValue(1);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })


                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_voto_laurea, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id== R.id.action_clear){
            //I want delete the last vote inserted
            mydb.deleteLastVoteFromDB(listaM);
            listaM.remove(listaM.size()-1);
                tasks.remove(listaM.size()-1); //remove the last element from the list
                adapter.notifyDataSetChanged();
        }
        if (id== R.id.action_delete){
            //I want clear all database
            mydb.deleteDB(listaM);
            tasks.clear();
            adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }
}
