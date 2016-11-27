package asuper.maathis.maathai;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listView;
    String uID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Select Item category, to start adding items to the shopping list", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent=getIntent();
        uID=intent.getStringExtra("email");
        Toast.makeText(this, "Welcome "+uID, Toast.LENGTH_SHORT).show();
        listView=(ListView)findViewById(R.id.listview);
        ListView list;
        String[] web = {
                "Grocery","Utensils","Furniture","Foods","Electronics"


        } ;
        Integer[] imageId = {
                R.drawable.flower1,
                R.drawable.glass,
                R.drawable.furniturefinal,
                R.drawable.foodfinal,
                R.drawable.eeefinal

        };
        CustomList adapter= new CustomList(MainActivity.this,web,imageId);
        list=(ListView)findViewById(R.id.listview);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {



                if (position == 0) {
                    Intent intent = new Intent(MainActivity.this, Grocery.class);
                    intent.putExtra("category", "grocery");
                    intent.putExtra("uDI",uID);
                    startActivity(intent);
                    //Toast.makeText(MainActivity.this, "GROCERY", Toast.LENGTH_SHORT).show();
                }

                else if (position == 1) {
                    Intent intent = new Intent(MainActivity.this, Grocery.class);
                    intent.putExtra("category", "utensils");
                    intent.putExtra("uDI",uID);
                    startActivity(intent);
                    // Toast.makeText(MainActivity.this, "UTENSILS", Toast.LENGTH_SHORT).show();
                }
                else if (position == 2) {
                    Intent intent = new Intent(MainActivity.this, Grocery.class);
                    intent.putExtra("category", "furniture");
                    intent.putExtra("uDI",uID);
                    startActivity(intent);
                }

                else if (position == 3) {
                    Intent intent = new Intent(MainActivity.this, Grocery.class);
                    intent.putExtra("category", "foods");
                    intent.putExtra("uDI",uID);
                    startActivity(intent);
                    // Toast.makeText(MainActivity.this, "FOOD", Toast.LENGTH_SHORT).show();
                }
                else if (position == 4) {
                    Intent intent = new Intent(MainActivity.this, Grocery.class);
                    intent.putExtra("category", "electronics");
                    intent.putExtra("uDI",uID);
                    startActivity(intent);
                    //  Toast.makeText(MainActivity.this, "electronics", Toast.LENGTH_SHORT).show();
                }


            }
        });




    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(MainActivity.this,Utensils.class);
            intent.putExtra("uID",uID);
            startActivity(intent);
        }

        if (id == R.id.offers) {
            Intent intent = new Intent(MainActivity.this, Grocery.class);
            intent.putExtra("category", "offers");
            intent.putExtra("uDI",uID);
            startActivity(intent);
        }

        else if (id == R.id.nav_send) {
            Intent intent = new Intent(MainActivity.this, Events.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
