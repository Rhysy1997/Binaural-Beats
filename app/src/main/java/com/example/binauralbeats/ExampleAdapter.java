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


public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<Track> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public ExampleAdapter(Context context, ArrayList<Track> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, final int position) {
        Track currentItem = mExampleList.get(position);

        String title = currentItem.getTrackTitle();
        String baseFreq = currentItem.getBaseFreq();
        String beatFreq = currentItem.getBeatFreq();
        String waveType = currentItem.getWaveType();

        holder.mTextViewTitle.setText(title);
        holder.mTextViewBase.setText("Base: " + baseFreq + " Hz");
        holder.mTextViewBeat.setText("Beat: " + beatFreq + " Hz");
        holder.mTextViewWaveType.setText(waveType);

        if(holder.mTextViewWaveType.getText().equals("Delta")){
            holder.mTextViewWaveType.setTextColor(this.mContext.getColor(R.color.colorDelta));
            holder.mImageViewWaveIconLibrary.setImageDrawable(this.mContext.getDrawable(R.drawable.deltaicon));
            holder.mImageViewWaveLine.setImageDrawable(this.mContext.getDrawable(R.drawable.deltablue1));
        }else if(holder.mTextViewWaveType.getText().equals("Theta")){
            holder.mTextViewWaveType.setTextColor(this.mContext.getColor(R.color.colorTheta));
            holder.mImageViewWaveIconLibrary.setImageDrawable(this.mContext.getDrawable(R.drawable.thetaicon));
            holder.mImageViewWaveLine.setImageDrawable(this.mContext.getDrawable(R.drawable.thetagreen1));

        }else if(holder.mTextViewWaveType.getText().equals("Alpha")){
            holder.mTextViewWaveType.setTextColor(this.mContext.getColor(R.color.colorAlpha));
            holder.mImageViewWaveIconLibrary.setImageDrawable(this.mContext.getDrawable(R.drawable.alphaicon));
            holder.mImageViewWaveLine.setImageDrawable(this.mContext.getDrawable(R.drawable.alphayellow1));

        }else if(holder.mTextViewWaveType.getText().equals("Beta")){
            holder.mTextViewWaveType.setTextColor(this.mContext.getColor(R.color.colorBeta));
            holder.mImageViewWaveIconLibrary.setImageDrawable(this.mContext.getDrawable(R.drawable.betaicon));
            holder.mImageViewWaveLine.setImageDrawable(this.mContext.getDrawable(R.drawable.betaorange1));

        }else{
            holder.mTextViewWaveType.setTextColor(this.mContext.getColor(R.color.colorGamma));
            holder.mImageViewWaveIconLibrary.setImageDrawable(this.mContext.getDrawable(R.drawable.gammaicon));
            holder.mImageViewWaveLine.setImageDrawable(this.mContext.getDrawable(R.drawable.gammared1));


        }

        holder.mImageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Track track = mExampleList.get(position);
                //track to delete
                String trackId = track.getTrackId();
               DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tracks").child(trackId);
               databaseReference.removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextViewTitle;
        public TextView mTextViewBase;
        public TextView mTextViewBeat;
        public TextView mTextViewWaveType;
        public ImageView mImageViewDelete;
        public ImageView mImageViewWaveIconLibrary;
        public ImageView mImageViewWaveLine;



        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.text_view_title);
            mTextViewBase = itemView.findViewById(R.id.text_view_base);
            mTextViewBeat = itemView.findViewById(R.id.text_view_beat);
            mTextViewWaveType = itemView.findViewById(R.id.textView_wave_type);
            mImageViewDelete = itemView.findViewById(R.id.imageView_delete);
            mImageViewWaveIconLibrary = itemView.findViewById(R.id.imageView_waveIconLibrary);
            mImageViewWaveLine = itemView.findViewById(R.id.imageView_waveLine);

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
