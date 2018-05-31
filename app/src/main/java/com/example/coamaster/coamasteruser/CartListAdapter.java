package com.example.coamaster.coamasteruser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CartListAdapter extends BaseAdapter{

    private Context context;
    private List<Cart> cartList;

    public CartListAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int i) {
        return cartList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.cartdetail, null);
        TextView menuText = (TextView) v.findViewById(R.id.menuText);
        TextView priceText = (TextView) v.findViewById(R.id.priceText);

        Cart cart = cartList.get(i);

        menuText.setText(cartList.get(i).getMenu());
        priceText.setText(cartList.get(i).getPrice() + "원");


        v.setTag(cartList.get(i).getMenu());
        return v;

    }


}