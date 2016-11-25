package asuper.maathis.maathai;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Utensils extends AppCompatActivity {
ListView listView;
    String getter="http://192.168.43.184/maathai/item_fetcher2.php";
    String content;
    String price;
    String uID;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utensils);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listView=(ListView)findViewById(R.id.listview);
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String[] arr = {content,"Total Price \t"+price};
                AlertDialog.Builder build = new AlertDialog.Builder(Utensils.this);
                build.setTitle("SHOPPING LIST CONTENTS\nITEM\t\t\t\tPRICE");
                build.setItems(arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(Grocery.this, which, Toast.LENGTH_SHORT).show();
                    }
                });
                build.show();


            }});
        getJSON();

        Intent intent=getIntent();
         uID=intent.getStringExtra("uID");
    }

    public void getJSON()
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

            SweetAlertDialog pDialog = new SweetAlertDialog(Utensils.this, SweetAlertDialog.PROGRESS_TYPE);

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
                HashMap<String, String> employees = new HashMap<>();
                employees.put("owner_id", uID);
                String res=rh.sendPostRequest(getter,employees);

                return res;

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pDialog.dismiss();
                showthem(s);
                Toast.makeText(Utensils.this, s, Toast.LENGTH_SHORT).show();

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
            String succes="0";
            for (int i = 0; i < result.length(); i++)
            {  JSONObject jo = result.getJSONObject(i);


                succes=jo.getString("success");
                if (succes.equals("1"))
                {
                    String dateexe=jo.getString("date");
                    content=jo.getString("content");
                    price=jo.getString("price");
                    String trxID=jo.getString("trxID");

                    HashMap<String, String> employees = new HashMap<>();
                    employees.put("date", dateexe);
                    employees.put("price", price);
                    employees.put("id", trxID);
                    list.add(employees);
                }
                else
                {

                }





            }

            if(!succes.equals("1"))
            {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("You have not registered any Shopping list yet")
                        .setTitleText("Whooops!")
                        .show();
            }





        } catch (JSONException e) {

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText(e.toString())
                    .show();

        }

            ListAdapter adapter = new SimpleAdapter(Utensils.this, list, R.layout.shoppinglists,
                    new String[]{"date", "price","id"}, new int[]{R.id.date, R.id.price,R.id.id});
            listView.setAdapter(adapter);
        }

}
