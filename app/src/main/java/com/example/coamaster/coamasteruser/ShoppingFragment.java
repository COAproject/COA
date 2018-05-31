package com.example.coamaster.coamasteruser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingFragment extends Fragment {

    private Context context;

    private ListView cartListView;
    private CartListAdapter adapter;
    private List<Cart> cartList;
    private EditText edit;
    private TextView textView;
    private Button delButton;
    private Button orderButton;
    private ArrayList<String> items;
    private CheckBox checkBox;
    private AlertDialog dialog;

    public String userID;
    public int sum = 0;
    public String cartmenu;
    public String ordermenu;

    public Fragment payFragment;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ShoppingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingFragment newInstance(String param1, String param2) {
        ShoppingFragment fragment = new ShoppingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        SaveActivity userInfo = (SaveActivity)getActivity().getApplication();
        userID = userInfo.getUser_id();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);

        final Button orderbutton = (Button)rootView.findViewById(R.id.orderButton);

        orderbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //장바구니가 비었을경우, 주문 불가
                if (sum == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("주문 할 상품이 없습니다")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }

                            })
                            .create();
                    dialog.show();
                }
                else{
                    Response.Listener<String> inforesponseLister = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                adapter.notifyDataSetChanged();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    int count = adapter.getCount() ;
                    for ( int i=0; i<count; i++){
                        if(i==0){
                            Cart cart = (Cart)adapter.getItem(i);
                            ordermenu = cart.menu;
                        }
                        else{
                            Cart cart = (Cart)adapter.getItem(i);
                            ordermenu = ordermenu + " " + cart.menu;
                        }


                    }

                    payFragment = new PayFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, payFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    EditText request = (EditText)getView().findViewById(R.id.editText);
                    String requestText = request.getText().toString();
                    SaveActivity userInfo = (SaveActivity)getActivity().getApplication();
                    userInfo.setRequest(requestText);
                    userInfo.setOrdermenu(ordermenu);
                    userInfo.setPrice(sum);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //you can set the title for your toolbar here for different fragments different titles
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("CART");

        cartListView = (ListView)getView().findViewById(R.id.cartListView);

        cartList = new ArrayList<Cart>();

        adapter = new CartListAdapter(getContext().getApplicationContext(), cartList);
        cartListView.setAdapter(adapter);
        cartListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        new BackgroundTask().execute();

        //50자 글자수 제한
        edit = (EditText)getView().findViewById(R.id.editText);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(100);
        edit.setFilters(FilterArray);

        edit.addTextChangedListener(new TextWatcher() {
            String strCur;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                strCur = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 50 ){
                    edit.setText(strCur);
                    edit.setSelection(start);
                }
                else {
                    textView.setText(String.valueOf(s.length()));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        textView = (TextView)getView().findViewById(R.id.textView);


        delButton = (Button)getView().findViewById(R.id.delButton);
        delButton.setOnClickListener(new Button.OnClickListener(){
            String target2;
            @Override
            public void onClick(View v) {

                int count = adapter.getCount() ;
                SparseBooleanArray checkedItems = cartListView.getCheckedItemPositions();

                if(cartListView.getCheckedItemCount() != 0){
                    for ( int i=0; i<count; i++){
                        if( cartListView.isItemChecked(i) == true){
                            Cart cart = (Cart)adapter.getItem(i);
                            cartmenu = cart.menu;
                            new BackgroundTask2().execute();
                        }
                        else{
                            // isItemChecked가 false일 경우, 삭제되지 않음
                        }

                    }
                    cartListView.clearChoices();
                    adapter.notifyDataSetChanged();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("삭제되었습니다")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    refresh();
                                }
                            })
                            .create();
                    dialog.show();

                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("삭제할 상품을 선택해주세요")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }

                            })
                            .create();
                    dialog.show();
                }
                adapter.notifyDataSetChanged();
            }
        });

    }
    private void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;
        TextView sumText;


        @Override
        protected void onPreExecute() {
            target = "http://coamaster.dothome.co.kr/android/CartList.php?user_id=" + userID ;
        }

        @Override

        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();


            } catch (Exception e){
                e.printStackTrace();
            }

            return null;


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override

        protected void onPostExecute(String result) {
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String cartMenu;
                int cartPrice;

                while(count < jsonArray.length())
                {

                    JSONObject object = jsonArray.getJSONObject(count);
                    cartMenu = object.getString("cartMenu");
                    cartPrice = object.getInt("cartPrice");
                    Cart cart = new Cart(cartMenu, cartPrice);
                    cartList.add(cart);
                    adapter.notifyDataSetChanged();
                    count++;
                    sum = sum + cartPrice;
                }

            }catch (Exception e){
                e.printStackTrace();
            }


            sumText = (TextView)getView().findViewById(R.id.sumText);
            sumText.setText("총 " + sum + "원");


        }

    }

    class BackgroundTask2 extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://coamaster.dothome.co.kr/android/CartDel.php?user_id=" + userID +
                    "&cart_menu=" + cartmenu;
        }

        @Override

        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();


            } catch (Exception e){
                e.printStackTrace();
            }

            return null;


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }



    }


}