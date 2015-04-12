package de.udos.democardlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CardViewFragment())
                    .commit();
        }
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


    public static class CardViewFragment extends Fragment {

        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private LinearLayoutManager mLayoutManager;
        private RecyclerView.ItemAnimator mItemAnimator;

        public CardViewFragment() {
            // leerer Konstruktor per Definition
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mItemAnimator = new DefaultItemAnimator();

            mAdapter = new CardViewItemAdapter(getActivity(), new Rating[] {
                    new Rating(0.67f, "Eins"),
                    new Rating(0.89f, "Zwei")});

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(mItemAnimator);
            mRecyclerView.setAdapter(mAdapter);

            return rootView;
        }
    }
}
