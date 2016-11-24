package asuper.maathis.maathai;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Grocery extends AppCompatActivity {

    Button button;
    String fetcher="http://192.168.43.184/maathai/item_fetcher.php";
    ListView listView;
    public static final String MyPREFERENCES = "MyPrefs";
    String what;
    List<String> myList = new ArrayList<String>();
    List<String> fetcherlist = new ArrayList<String>();
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //button=(Button)findViewById(R.id.button);
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        listView=(ListView)findViewById(R.id.listview);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick();
            }
        });
        Intent intent =getIntent();
        what=intent.getStringExtra("category");
        getJSON();

        String email= sharedpreferences.getString("what", "null");


        if(email.isEmpty())
        {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("what", what);
            editor.commit();
        }
        Toast.makeText(this, "Activity name:="+email, Toast.LENGTH_SHORT).show();

        final SharedPreferences.Editor editor3 = sp.edit();

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
                final  String itemid =   map.get("name");
                final  String price=map.get("price");

                AlertDialog.Builder build = new AlertDialog.Builder(Grocery.this);
                build.setTitle("Please Confirm");
                build.setMessage("Are you sure you want to add \n"+itemid+"\nto the shopping list");

                build.setPositiveButton("YES", new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Grocery.this, "Saved", Toast.LENGTH_SHORT).show();
                        myList.add(itemid+"\t"+price);
                        editor3.putInt("Status_size", myList.size());

                        String email= sharedpreferences.getString("what", "null");


//                        if(!email.equals(what))
//                        {
//                             Toast.makeText(Grocery.this, "We Have moved to a new activity", Toast.LENGTH_SHORT).show();
//                            for (i = 0; i < myList.size(); i++) {
//                                editor3.remove("Status_" + i);
//                                editor3.putString("Status_" + i, myList.get(i));
//
//                            }
//                            editor3.commit();
//                            Toast.makeText(Grocery.this, "We do not delete existing elements", Toast.LENGTH_SHORT).show();
//                        }else if (email.equals(what))
//                        {
//                            Toast.makeText(Grocery.this, "We are in the same activity", Toast.LENGTH_SHORT).show();
//                            for (i = 0; i < myList.size(); i++)
//                            {
//                               // editor3.remove("Status_" + i);
//                                editor3.putString("Status_" + i, myList.get(i));
//                                //  Toast.makeText(Grocery.this, "Has some contents", Toast.LENGTH_SHORT).show();
//                            }
//                            editor3.commit();
//                        }

                        if(myList.size()<1) {
                            for (i = 0; i < myList.size(); i++) {
                                editor3.remove("Status_" + i);
                                editor3.putString("Status_" + i, myList.get(i));

                            }

                        }

                        else
                        {
                            for (i = myList.size()-1; i < myList.size(); i++) {
                                editor3.remove("Status_" + i);
                                editor3.putString("Status_" + i, myList.get(i));

                            }
                            editor3.commit();

                        }


                    }
                });
                build.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Grocery.this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                build.show();



            }
        });






//        Set<String> set = myScores.getStringSet("key", null);
//
//        //Set the values
//        Set<String> set = new HashSet<String>();
//        set.addAll(myList);
//        scoreEditor.putStringSet("key", set);
//        scoreEditor.commit();









    }

    public void onclick()

    {

        SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(this);



        String email= sharedpreferences.getString("what", "null");


        if(!email.equals(what))
        {
            Toast.makeText(this, "We Have moved to a new activity", Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("what", what);
            editor.commit();



            int size = mSharedPreference1.getInt("Status_size", 0);

            for(int i=0;i<size;i++)
            {
                fetcherlist.add(mSharedPreference1.getString("Status_" + i, null));
            }


            String[] arr = fetcherlist.toArray(new String[fetcherlist.size()]);
            AlertDialog.Builder build = new AlertDialog.Builder(Grocery.this);
            build.setTitle("SHOPPING LIST CONTENTS\nITEM\t\t\t\tPRICE");
            build.setItems(arr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Grocery.this, which, Toast.LENGTH_SHORT).show();
                }
            });
            //build.setMessage("Total Price"+300);
            build.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                }
            });
            build.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(Grocery.this, "Shopping List Saved", Toast.LENGTH_SHORT).show();
                }
            });
            build.show();
        }
        else
        {

            fetcherlist.clear();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("what", what);
            editor.commit();



            int size = mSharedPreference1.getInt("Status_size", 0);

            for(int i=0;i<size;i++)
            {
                fetcherlist.add(mSharedPreference1.getString("Status_" + i, null));
            }


            String[] arr = fetcherlist.toArray(new String[fetcherlist.size()]);
            AlertDialog.Builder build = new AlertDialog.Builder(Grocery.this);
            build.setTitle("SHOPPING LIST CONTENTS\nITEM\t\t\t\tPRICE");
            build.setItems(arr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Grocery.this, which, Toast.LENGTH_SHORT).show();
                }
            });
            //build.setMessage("Total Price"+300);
            build.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                }
            });
            build.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(Grocery.this, "Shopping List Saved", Toast.LENGTH_SHORT).show();
                }
            });
            build.show();
        }





    }



    public void getJSON()
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            SweetAlertDialog pDialog = new SweetAlertDialog(Grocery.this, SweetAlertDialog.PROGRESS_TYPE);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading items");
                pDialog.setCancelable(false);
                pDialog.show();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> parabms = new HashMap<>();
                parabms.put("item_category",what);
                String res = rh.sendPostRequest(fetcher, parabms);
                return res;

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pDialog.dismiss();
                showthem(s);
                //. Toast.makeText(Grocery.this, s, Toast.LENGTH_SHORT).show();

            }


        }
        GetJSON jj =new GetJSON();
        jj.execute();
    }



    private void showthem(String s) {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            String itemID="";
            for (int i = 0; i < result.length(); i++)
            {  JSONObject jo = result.getJSONObject(i);

                String name=jo.getString("name");
                String price=jo.getString("price");

                HashMap<String, String> employees = new HashMap<>();
                employees.put("name", name);
                employees.put("price", price);
                list.add(employees);
            }



        } catch (JSONException e) {

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText(e.toString())
                    .show();

        }

        ListAdapter adapter = new SimpleAdapter(Grocery.this, list, R.layout.itemlayout,
                new String[]{"name", "price"}, new int[]{R.id.name, R.id.price});
        listView.setAdapter(adapter);
    }




}














/*
Loading Array Data from SharedPreferences

public static void loadArray(Context mContext)
{
    SharedPreferences mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(mContext);
    sKey.clear();
    int size = mSharedPreference1.getInt("Status_size", 0);

    for(int i=0;i<size;i++)
    {
     sKey.add(mSharedPreference1.getString("Status_" + i, null));
    }

}
 */



/*


Saving Array in SharedPreferences:

public static boolean saveArray()
{
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor mEdit1 = sp.edit();
     sKey is an array
mEdit1.putInt("Status_size", sKey.size());

        for(int i=0;i<sKey.size();i++)
        {
        mEdit1.remove("Status_" + i);
        mEdit1.putString("Status_" + i, sKey.get(i));
        }

        return mEdit1.commit();
        }


 */


