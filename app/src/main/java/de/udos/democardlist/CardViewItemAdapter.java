package de.udos.democardlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class CardViewItemAdapter extends RecyclerView.Adapter<CardViewItemAdapter.ViewHolder> {

    private Context mContext;
    private Rating[] mData;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mCardTitleView;
        private final Indicator mIndicatorView;


        public ViewHolder(View v) {

            super(v);
            mIndicatorView = (Indicator) v.findViewById(R.id.indicator_view);
            mCardTitleView = (TextView) v.findViewById(R.id.tv_card_title);
        }
    }

    public CardViewItemAdapter(Context context, Rating[] data) {

        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemRootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_item, parent, false);

        return new ViewHolder(itemRootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Rating item = mData[position];

        holder.mCardTitleView.setText(item.getTitle());

        holder.mIndicatorView.setLabelText(Math.round(item.getValue() * 100) + "%");
        holder.mIndicatorView.setValue(item.getValue());

        Animation anim = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
        anim.setStartOffset(200 * position);
        holder.itemView.startAnimation(anim);

    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

}
