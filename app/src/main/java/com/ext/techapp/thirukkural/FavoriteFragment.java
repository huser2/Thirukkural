package com.ext.techapp.thirukkural;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.ext.techapp.thirukkural.adapter.CustomStringAdapter;
import com.ext.techapp.thirukkural.db.Favorite;
import com.ext.techapp.thirukkural.db.FavoritesDataSource;
import com.ext.techapp.thirukkural.xml.CoupletsXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FavoriteFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    private CoupletsXMLParser.Couplet[] couplet_list_to_show;
    public static final String NAV_ITEM_ID = "nav_item_id";

    FavoritesDataSource dataSource;

    public static String IS_FAVORITE_FRAGMENT="IS_FAVORITE_FRAGMENT";
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavoriteFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FavoriteFragment newInstance(int columnCount) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        dataSource = new FavoritesDataSource(getActivity());
        dataSource.open();


        List<Favorite> favList= dataSource.getAllFavorites();
        String[] coupletsDisplayArray = new String[favList.size()];

        ArrayList<CoupletsXMLParser.Couplet> couplets = new ArrayList<CoupletsXMLParser.Couplet>();
        for (int j=0;j<favList.size();j++) {
            Favorite favorite = favList.get(j);
            String chapter = Long.toString(favorite.getCOLUMN_CHAPTER_ID());
            int chapter_id = getResources().getIdentifier("chapter_" + chapter, "raw", getActivity().getPackageName());

            //String chapter = getArguments().getString(NAV_CHAPTER);

           // int id = getResources().getIdentifier("chapter_" + chapter_id, "raw", getActivity().getPackageName());
            InputStream in = getResources().openRawResource(chapter_id);

            try {
                Map<Integer, CoupletsXMLParser.Couplet> coupletsMap = new CoupletsXMLParser().coupletsList(in);
                couplet_list_to_show = new CoupletsXMLParser.Couplet[10];
               // int i = 0;
                for (Object key : coupletsMap.keySet()) {
                    CoupletsXMLParser.Couplet couplet = coupletsMap.get(key);
                    // int i = (int)key;
                    String couplet_code = Long.toString(favorite.getCOLUMN_COUPLET_ID());
                    if (couplet.getCoupletNumber().equals(couplet_code)){
                        couplets.add(couplet);
                    }

                   // couplet_list_to_show[i] = couplet;
                   // coupletsDisplayArray[i] = couplet.getFirstLineTamil() + "\n " + couplet.getSecondLineTamil();
                 //   i++;
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //List<String> list = Arrays.asList(coupletsDisplayArray);
        // TODO: Change Adapter to display your content
        //  mAdapter = new ArrayAdapter<String>(getActivity(),
        //         R.layout.simple_list_item_1, android.R.id.text1, list);
        couplet_list_to_show = new CoupletsXMLParser.Couplet[couplets.size()];
        couplet_list_to_show = couplets.toArray(couplet_list_to_show);
        mAdapter = new CustomStringAdapter(getActivity(), R.layout.simple_list_item_1, android.R.id.text1, R.id.coupletNumber, couplet_list_to_show);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }



    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        dataSource.close();
    }

    @Override
    public void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        dataSource.close();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        dataSource.close();
        super.onDestroy();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFavoriteFragmentInteraction(getArguments().getInt(ItemListFragment.NAV_ITEM_ID),couplet_list_to_show[position]);
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFavoriteFragmentInteraction(int id,CoupletsXMLParser.Couplet couplet);
    }
}
