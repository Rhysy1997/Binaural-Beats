package com.example.binauralbeats;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private ArrayList<Category> mCatList;
    private CategoryAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CategoryAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public CategoryAdapter(Context context, ArrayList<Category> catList) {
        mContext = context;
        mCatList = catList;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.home_item, parent, false);
        return new CategoryAdapter.CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, final int position) {
        Category currentItem = mCatList.get(position);

        String type = currentItem.getCatType();
        String baseFreq = currentItem.getCatBaseFreq();
        String beatFreq = currentItem.getCatBeatFreq();
        String desc = currentItem.getCatDesc();
        String time = currentItem.getCatTime();

        holder.mTextViewType.setText(type);
        holder.mTextViewBase.setText("Base: " + baseFreq + " Hz");
        holder.mTextViewBeat.setText("Beat: " + beatFreq + " Hz");
        holder.mTextViewDesc.setText(desc);
        holder.mTextViewTime.setText(time + " min");

        if(holder.mTextViewType.getText().equals("Delta")){
            holder.mTextViewType.setTextColor(this.mContext.getColor(R.color.colorDelta));
            holder.mTextViewDesc.setTextColor(this.mContext.getColor(R.color.colorDelta));
            holder.imageViewWaveIcon.setImageDrawable(this.mContext.getDrawable(R.drawable.deltaicon));
           // holder.itemView.setBackground(this.mContext.getDrawable(R.drawable.deltablue));
        }else if(holder.mTextViewType.getText().equals("Theta")){
            holder.mTextViewType.setTextColor(this.mContext.getColor(R.color.colorTheta));
            holder.mTextViewDesc.setTextColor(this.mContext.getColor(R.color.colorTheta));
            holder.imageViewWaveIcon.setImageDrawable(this.mContext.getDrawable(R.drawable.thetaicon));

            // holder.itemView.setBackground(this.mContext.getDrawable(R.drawable.thetagreen));
        }else if(holder.mTextViewType.getText().equals("Alpha")){
            holder.mTextViewType.setTextColor(this.mContext.getColor(R.color.colorAlpha));
            holder.mTextViewDesc.setTextColor(this.mContext.getColor(R.color.colorAlpha));
            holder.imageViewWaveIcon.setImageDrawable(this.mContext.getDrawable(R.drawable.alphaicon));

            // holder.itemView.setBackground(this.mContext.getDrawable(R.drawable.alphayellow));

        }else if(holder.mTextViewType.getText().equals("Beta")){
            holder.mTextViewType.setTextColor(this.mContext.getColor(R.color.colorBeta));
            holder.mTextViewDesc.setTextColor(this.mContext.getColor(R.color.colorBeta));
            holder.imageViewWaveIcon.setImageDrawable(this.mContext.getDrawable(R.drawable.betaicon));

            // holder.itemView.setBackground(this.mContext.getDrawable(R.drawable.betaorange));

        }else{
            holder.mTextViewType.setTextColor(this.mContext.getColor(R.color.colorGamma));
            holder.mTextViewDesc.setTextColor(this.mContext.getColor(R.color.colorGamma));
            holder.imageViewWaveIcon.setImageDrawable(this.mContext.getDrawable(R.drawable.gammaicon));

            // holder.itemView.setBackground(this.mContext.getDrawable(R.drawable.gammared));


        }
    }

    @Override
    public int getItemCount() {
        return mCatList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextViewType;
        public TextView mTextViewBase;
        public TextView mTextViewBeat;
        public TextView mTextViewDesc;
        public TextView mTextViewTime;
        public ImageView imageViewWaveIcon;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewType = itemView.findViewById(R.id.text_view_type);
            mTextViewBase = itemView.findViewById(R.id.text_view_cat_base);
            mTextViewBeat = itemView.findViewById(R.id.text_view_cat_beat);
            mTextViewDesc = itemView.findViewById(R.id.textView_cat_desc);
            mTextViewTime = itemView.findViewById(R.id.textView_cat_time);
            imageViewWaveIcon = itemView.findViewById(R.id.imageView_waveIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }
}
