package com.example.coamaster.coamasteruser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderListAdapter extends BaseAdapter{

    private Context context;
    private List<Order> orderList;

    public OrderListAdapter(Context context, List<Order> orderlist) {
        this.context = context;
        this.orderList = orderlist;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.orderdetail, null);
        TextView orderText = (TextView) v.findViewById(R.id.orderText);
        TextView condiText = (TextView) v.findViewById(R.id.condiText);
        TextView dateText = (TextView) v.findViewById(R.id.dateText);

        orderText.setText(orderList.get(i).getOrder());
        condiText.setText(orderList.get(i).getCondi());
        dateText.setText(orderList.get(i).getDate());

        v.setTag(orderList.get(i).getOrder());
        return v;

    }


}
