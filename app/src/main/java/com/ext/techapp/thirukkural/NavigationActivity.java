package com.ext.techapp.thirukkural;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ext.techapp.thirukkural.notification.NotificationDetailActivity;
import com.ext.techapp.thirukkural.preference.SettingsActivity;
import com.ext.techapp.thirukkural.xml.CoupletsXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.StringTokenizer;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ItemListFragment.OnListFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener, SearchView.OnSuggestionListener, SearchView.OnQueryTextListener, FavoriteFragment.OnListFragmentInteractionListener {


    NavigationView navigationView;


    private SimpleCursorAdapter mAdapter;
    SearchView searchView;

    MenuItem searchItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        boolean hasTitle = getIntent().hasExtra(ItemListFragment.NAV_ITEM_ID);
        if (hasTitle) {
                int itemId = getIntent().getIntExtra(ItemListFragment.NAV_ITEM_ID, 0);
                this.onNavigationItemSelected(navigationView.getMenu().findItem(itemId).setChecked(true));
        } else {
            AboutFragment about = new AboutFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.item_list_fragment_layout, about).commit();
        }

        final String[] from = new String[]{"searchCode"};
        final int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this,
                R.layout.simple_suggest_list_item_1,
                null,
                from,
                to,
                CursorAdapter.IGNORE_ITEM_VIEW_TYPE);

    }


    public int getResourceId(String pVariableName, String pResourcename, String pPackageName) {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
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
        getMenuInflater().inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat
                .getActionView(searchItem);
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setIconifiedByDefault(true);
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(this.getComponentName());
        searchView.setSearchableInfo(searchableInfo);

        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(this);
        searchView.setOnQueryTextListener(this);
        return super.onPrepareOptionsMenu(menu);
    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        final MatrixCursor cursor = new MatrixCursor(new String[]{BaseColumns._ID, "searchCode"});
        // mAdapter.getFilter().filter(query.toString());

        int queryInt = Integer.parseInt(query);
        if (queryInt != 0 && queryInt <= 133) {
            cursor.addRow(new Object[]{0, "அதிகாரம்:" + queryInt});
        }
        if (queryInt != 0 && queryInt <= 1330) {
            cursor.addRow(new Object[]{0, "குறள்:" + queryInt});
        }


        mAdapter.changeCursor(cursor);
        mAdapter.notifyDataSetChanged();
        //  searchView.setSuggestionsAdapter(mAdapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.search:
                // onSearchRequested();
                break;
        }
        // if (id == R.id.action_settings) {
        //scheduleNotification();
        //startActivity(new Intent(this,SettingsActivity.class));
        //   return true;
        // }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        setTitle(item.getTitle());
        switch (id) {

            case R.id.thiruvalluvar:

                AboutFragment about = new AboutFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(ItemListFragment.NAV_ITEM_ID, id);
                about.setArguments(bundle);
                navigationView.getMenu().findItem(id).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.item_list_fragment_layout, about).commit();
                break;

            case R.id.saved_favorites:
                navigationView.getMenu().findItem(id).setChecked(true);
                FavoriteFragment favoriteFragment = new FavoriteFragment();
                bundle = new Bundle();
                bundle.putInt(ItemListFragment.NAV_ITEM_ID, id);
                favoriteFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.item_list_fragment_layout, favoriteFragment).commit();
                break;
            case R.id.today_couplet:
                navigationView.getMenu().findItem(id).setChecked(true);
                Intent intent = new Intent(this, NotificationDetailActivity.class);
                startActivity(intent);
                break;
            default:

                ItemListFragment listFragment = new ItemListFragment();
                bundle = new Bundle();
                bundle.putInt(ItemListFragment.NAV_ITEM_ID, id);

                StringTokenizer tokens = new StringTokenizer(item.getTitle().toString(), ".");
                String chapter_code = tokens.nextToken();
                bundle.putString(ItemListFragment.NAV_CHAPTER, chapter_code);

                bundle.putString(ItemListFragment.NAV_ITEM_TITLE, item.getTitle().toString());
                listFragment.setArguments(bundle);

                int chap_code = Integer.valueOf(chapter_code);
                navigationView.getMenu().findItem(id).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.item_list_fragment_layout, listFragment).commit();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onFragmentInteraction(int id, CoupletsXMLParser.Couplet couplet) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(ItemListFragment.NAV_ITEM_ID, id);
        intent.putExtra("selected_couplet", couplet);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        //s = ":"+s;
        if (s.length() > 0) {
            populateAdapter(s);
        }
        return false;

    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        // Your code here
        Cursor c = (Cursor) mAdapter.getItem(position);
        String selected = c.getString(1);
        TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
        splitter.setString(selected);
        splitter.next();
        String query = splitter.next();
        int query_code = Integer.valueOf(query);
        if (selected.contains("அதிகாரம்")) {
            this.onNavigationItemSelected(navigationView.getMenu().findItem(getResourceId("chapter_"+query_code,"id",getPackageName())).setChecked(true));
            searchItem.collapseActionView();
        }
        if (selected.contains("குறள்")) {
            int chapter_code;
            int resource_id = 0;

            if (query_code % 10 == 0) {
                chapter_code = query_code / 10;
            } else {
                chapter_code = query_code / 10 + 1;
            }

            resource_id = this.getResources().getIdentifier("chapter_" + chapter_code, "raw", this.getPackageName());
            InputStream in = this.getResources().openRawResource(resource_id);
            CoupletsXMLParser.Couplet couplet = getCouplet(in, query_code);

            Intent intent = new Intent(this, ItemDetailActivity.class);
            // intent.putExtra("couplet_number", query_code);
            intent.putExtra(ItemListFragment.NAV_ITEM_ID, getResourceId("chapter_"+chapter_code,"id",getPackageName()));
            intent.putExtra("selected_couplet", couplet);
            startActivity(intent);

        }

        return true;
    }

    @Override
    public void onFavoriteFragmentInteraction(int id, CoupletsXMLParser.Couplet couplet) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(ItemListFragment.NAV_ITEM_ID, id);
        intent.putExtra("selected_couplet", couplet);
        startActivity(intent);
    }

    public CoupletsXMLParser.Couplet getCouplet(InputStream in, int query_code) {
        CoupletsXMLParser.Couplet couplet = null;
        try {
            Map<Integer, CoupletsXMLParser.Couplet> coupletsMap = new CoupletsXMLParser().coupletsList(in);
            CoupletsXMLParser.Couplet[] couplet_list_to_show = new CoupletsXMLParser.Couplet[10];
            int i = 0;
            for (Object key : coupletsMap.keySet()) {
                // int i = (int)key;
                CoupletsXMLParser.Couplet kural = coupletsMap.get(key);
                couplet_list_to_show[i] = kural;
                i++;
            }

            int kural = query_code % 10;
            if (kural > 0) {
                kural = kural - 1;
            }
            couplet = couplet_list_to_show[kural];
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return couplet;
    }


}
