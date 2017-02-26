package com.projects.trega.ichimokudroid;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.trega.ichimokudroid.DataProvider.StockRecord;

import java.util.ArrayList;

public class StockCardAdapter  extends RecyclerView.Adapter<StockCardAdapter.ViewHolder>{
    ArrayList<StockCard> sampleStockCardsList = new ArrayList<>();

    private void prepareSampleItems(){
        for (int i=0 ; i<100; ++i){
            sampleStockCardsList.add(new StockCard());
        }
    }
    public  StockCardAdapter(){
        prepareSampleItems();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView card = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_card, parent, false);
        ViewHolder vh = new ViewHolder(card);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StockCard stockCard = sampleStockCardsList.get(position);
        holder.symbolTxtV.setText(stockCard.getSymbolName() + position);
        holder.priceTxtV.setText(stockCard.getPrice() + position);
    }

    @Override
    public int getItemCount() {
        return sampleStockCardsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView symbolTxtV, priceTxtV;
        public ViewHolder(View v) {
            super(v);
            symbolTxtV = (TextView)v.findViewById(R.id.symbolTxtView);
            priceTxtV = (TextView)v.findViewById(R.id.priceTxtView);
        }
    }
}
