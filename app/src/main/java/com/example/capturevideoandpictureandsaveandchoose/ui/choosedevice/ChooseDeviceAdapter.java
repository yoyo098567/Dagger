package com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capturevideoandpictureandsaveandchoose.R;

import java.util.ArrayList;

public class ChooseDeviceAdapter extends RecyclerView.Adapter{
    private ArrayList<ChooseDeviceItemData> dataList;
    private int CurrentPosition;
    boolean clickStatus = false;

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView EQNO;

        ViewHolder(View itemView) {
            super(itemView);
            EQNO = itemView.findViewById(R.id.text_device_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EQNO.setBackgroundColor(0xFF0000FF);
                }
            });
        }
    }

    public void setDataList(ArrayList<ChooseDeviceItemData> dataList){
        this.dataList=dataList;
        notifyDataSetChanged();

    }
    public int getCurrentPosition(){
        return CurrentPosition;
    }
    public boolean getClickStatus(){
        return clickStatus;
    }
    public void setClickStatusFalse(){
        clickStatus = false;
    }
    public void addDataToDataList(ChooseDeviceItemData mChooseDeviceItemData){
        dataList.add(mChooseDeviceItemData);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStatus = true;
                Log.v("ggg","position:" + position + "CurrentPosition:" + CurrentPosition);
                if(!(position == CurrentPosition)){
                    dataList.get(CurrentPosition).setBackgroundChange(false);
                }
                CurrentPosition=position;
                dataList.get(position).setBackgroundChange(!dataList.get(position).isBackgroundChange());
                notifyDataSetChanged();
            }
        });
        if (holder instanceof ChooseDeviceAdapterViewHolder) {
            ((ChooseDeviceAdapterViewHolder) holder).getItemDevice().setText(dataList.get(position).getEQNO());
            ((ChooseDeviceAdapterViewHolder) holder).getNumber().setText(""+position);
            if(dataList.get(position).isBackgroundChange()){
                ((ChooseDeviceAdapterViewHolder) holder).getItemDevice().setTextColor(Color.parseColor("#dc143c"));
            }else{
                ((ChooseDeviceAdapterViewHolder) holder).getItemDevice().setTextColor(Color.parseColor("#000000"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        return new ChooseDeviceAdapterViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    public ArrayList<ChooseDeviceItemData> getDataList(){
        return dataList;
    }
}
