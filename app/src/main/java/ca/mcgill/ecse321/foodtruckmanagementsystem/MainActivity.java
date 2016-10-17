package ca.mcgill.ecse321.foodtruckmanagementsystem;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ca.mcgill.ecse321.team7.foodtruckmanagementsystem.controller.InvalidInputException;
import ca.mcgill.ecse321.team7.foodtruckmanagementsystem.controller.MenuController;
import ca.mcgill.ecse321.team7.foodtruckmanagementsystem.controller.MenuControllerAdapter;
import ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.FoodTruckManagementSystem;
import ca.mcgill.ecse321.team7.foodtruckmanagementsystem.persistence.PersistenceFoodTruckManager;


public class MainActivity extends AppCompatActivity {

    private HashMap<Integer, ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.Menu> menus;
    private HashMap<Integer, ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.MenuItem> items;
    String error = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PersistenceFoodTruckManager.loadEventRegistrationModel();

        final Spinner menuSpinner = (Spinner) findViewById(R.id.menuspinner);
        menuSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                populateItemSpinner();
                populateGridView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                populateItemSpinner();
                populateGridView();
            }

        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void refreshData() {
        populateMenuSpinner();
        TextView newMenuNameTV = (TextView) findViewById(R.id.newmenu_name);
        newMenuNameTV.setText("");
        Spinner menuSpinner = (Spinner) findViewById(R.id.menuspinner);
        menuSpinner.setSelection(0);
        TextView editMenuNameTV = (TextView) findViewById(R.id.editmenu_name);
        editMenuNameTV.setText("");
        TextView newItemNameTV = (TextView) findViewById(R.id.newitem_name);
        newItemNameTV.setText("");
        TextView newItemPriceTV = (TextView) findViewById(R.id.newitem_price);
        newItemPriceTV.setText("");
        Spinner itemSpinner = (Spinner) findViewById(R.id.itemspinner);
        itemSpinner.setSelection(0);

    }


    public void populateMenuSpinner() {
        FoodTruckManagementSystem ftms = FoodTruckManagementSystem.getInstance();
        Spinner spinner = (Spinner) findViewById(R.id.menuspinner);
        ArrayAdapter<CharSequence> menuAdapter = new
                ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        menuAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        this.menus = new HashMap<Integer, ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.Menu>();
        int i = 0;
        try {
            for (Iterator<ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.Menu> menus = ftms.getFoodList().iterator();
                 menus.hasNext(); i++) {
                ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.Menu m = menus.next();
                menuAdapter.add(m.getName());
                this.menus.put(i, m);
            }
            spinner.setAdapter(menuAdapter);
        } catch (Exception e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }
    }

    public void populateItemSpinner() {
        FoodTruckManagementSystem ftms = FoodTruckManagementSystem.getInstance();
        Spinner itemSpinner = (Spinner) findViewById(R.id.itemspinner);
        Spinner menuSpinner = (Spinner) findViewById(R.id.menuspinner);
        ArrayAdapter<CharSequence> itemAdapter = new
                ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        itemAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        this.items = new HashMap<Integer, ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.MenuItem>();
        int i = 0;
        try {
            ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.Menu m = this.menus.get(menuSpinner.getSelectedItemPosition());
            for (Iterator<ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.MenuItem> items = m.getMenuItems().iterator();
                 items.hasNext(); i++) {
                ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.MenuItem item = items.next();
                itemAdapter.add(item.getName());
                this.items.put(i, item);
            }
            itemSpinner.setAdapter(itemAdapter);
        } catch (Exception e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }
    }

    public void populateGridView(){
        Spinner menuSpinner = (Spinner) findViewById(R.id.menuspinner);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.Menu m = this.menus.get(menuSpinner.getSelectedItemPosition());
        MenuController mc = new MenuControllerAdapter();
        final ArrayList<String> menuItemList = new ArrayList<String>();
        try{
            for (int i = 0; i < this.items.size(); i++) {
                menuItemList.add(this.items.get(i).getName());
                menuItemList.add(String.valueOf(this.items.get(i).getPrice()));
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,menuItemList);
            gridview.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }


    }

    public void addMenu(View v) {
        TextView tv = (TextView) findViewById(R.id.newmenu_name);
        MenuController mc = new MenuControllerAdapter();

        try {
            mc.createMenu(tv.getText().toString());
        } catch (InvalidInputException e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }

        refreshData();
    }

    public void editMenu(View v) {
        TextView tv = (TextView) findViewById(R.id.editmenu_name);
        Spinner menuSpinner = (Spinner) findViewById(R.id.menuspinner);

        MenuController mc = new MenuControllerAdapter();

        try {
            ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.Menu m = this.menus.get(menuSpinner.getSelectedItemPosition());
            mc.changeMenuName(m, tv.getText().toString());
        } catch (InvalidInputException e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }

        refreshData();
    }

    public void removeMenu(View v) {
        Spinner menuSpinner = (Spinner) findViewById(R.id.menuspinner);
        MenuController mc = new MenuControllerAdapter();

        try {
            ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.Menu m = this.menus.get(menuSpinner.getSelectedItemPosition());
            mc.deleteMenu(m);
        } catch (InvalidInputException e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }

        refreshData();
    }

    public void addItem(View v) {
        TextView tvname = (TextView) findViewById(R.id.newitem_name);
        TextView tvprice = (TextView) findViewById(R.id.newitem_price);
        Spinner menuSpinner = (Spinner) findViewById(R.id.menuspinner);

        MenuController mc = new MenuControllerAdapter();

        try {
            ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.Menu m = this.menus.get(menuSpinner.getSelectedItemPosition());
            String name = tvname.getText().toString();
            Integer price = (int) (100 * Double.parseDouble(tvprice.getText().toString()));
            mc.createMenuItem(m, name, price);
        } catch (InvalidInputException e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }
        refreshData();
    }

    public void removeItem(View v) {
        Spinner menuSpinner = (Spinner) findViewById(R.id.menuspinner);
        Spinner itemSpinner = (Spinner) findViewById(R.id.itemspinner);
        MenuController mc = new MenuControllerAdapter();

        try {
            ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.Menu m = this.menus.get(menuSpinner.getSelectedItemPosition());
            ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.MenuItem mi = this.items.get(itemSpinner.getSelectedItemPosition());
            mc.deleteMenuItem(m, mi);
        } catch (InvalidInputException e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }

        refreshData();
    }

    public void updateItem(View v) {
        TextView tvname = (TextView) findViewById(R.id.edititem_name);
        TextView tvprice = (TextView) findViewById(R.id.edititem_price);
        Spinner menuSpinner = (Spinner) findViewById(R.id.menuspinner);
        Spinner itemSpinner = (Spinner) findViewById(R.id.itemspinner);
        MenuController mc = new MenuControllerAdapter();

        try {
            ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.Menu m = this.menus.get(menuSpinner.getSelectedItemPosition());
            ca.mcgill.ecse321.team7.foodtruckmanagementsystem.model.MenuItem mi = this.items.get(itemSpinner.getSelectedItemPosition());
            String name = tvname.getText().toString();
            String stringFigure = tvprice.getText().toString();
            if (m == null || mi == null) {
                displayExceptionMessage("Please select a menu and an item to edit.");
            }
            if (stringFigure.length() > 0) {
                Integer price = (int) (100 * Double.parseDouble(tvprice.getText().toString()));
                mc.changeMenuItemPrice(m, mi, price);
            }
            if (name.length() > 0) {
                mc.changeMenuItemName(m, mi, name);
            }
            if (stringFigure.length() <= 0 && name.length() <= 0) {
                displayExceptionMessage("Please enter a new name or price for the edited item");
            }

        } catch (InvalidInputException e) {
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }

        refreshData();
    }



    public void displayExceptionMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
