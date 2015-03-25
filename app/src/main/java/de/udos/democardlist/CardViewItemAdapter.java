package de.udos.democardlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CardViewItemAdapter extends RecyclerView.Adapter<CardViewItemAdapter.ViewHolder> {

    private String[] mData;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;

        public ViewHolder(View v) {

            super(v);
            mTextView = (TextView) v.findViewById(R.id.text_view);
        }
    }

    public CardViewItemAdapter(String[] data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemRootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycler_view_item, parent, false);

        return new ViewHolder(itemRootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mData[position]);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

}
