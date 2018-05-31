package com.example.coamaster.coamasteruser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayFragment extends Fragment {
    public String user_id;
    public String request;
    public String ordermenu;
    public int orderprice;
    public FragmentManager fragmentManager;
    private AlertDialog dialog;
    EditText cardnumber1;
    EditText cardnumber2;
    EditText cardnumber3;
    EditText cardnumber4;
    EditText card_mm;
    EditText card_yy;
    private TextView textView;
    public Fragment orderlistFragment;
    public Fragment shoppingFragment;
    public Button payButton;
    public Button paycancelButton;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PayFragment newInstance(String param1, String param2) {
        PayFragment fragment = new PayFragment();
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
        user_id = userInfo.getUser_id();
        request = userInfo.getRequest();
        ordermenu = userInfo.getOrdermenu();
        orderprice = userInfo.getPrice();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardnumber1 = (EditText)getView().findViewById(R.id.cardnum1);
        cardnumber2 = (EditText)getView().findViewById(R.id.cardnum2);
        cardnumber3 = (EditText)getView().findViewById(R.id.cardnum3);
        cardnumber4 = (EditText)getView().findViewById(R.id.cardnum4);
        card_mm = (EditText)getView().findViewById(R.id.card_mm);
        card_yy = (EditText)getView().findViewById(R.id.card_yy);

        payButton = (Button)getView().findViewById(R.id.paybutton);
        paycancelButton = (Button)getView().findViewById(R.id.paycancelbutton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardnum1 = cardnumber1.getText().toString();
                String cardnum2 = cardnumber2.getText().toString();
                String cardnum3 = cardnumber3.getText().toString();
                String cardnum4 = cardnumber4.getText().toString();
                String cardmm = card_mm.getText().toString();
                String cardyy = card_yy.getText().toString();


                if (cardnum1.length() != 4 || cardnum2.length() != 4 || cardnum3.length() != 4 || cardnum4.length() != 4 || cardmm.length() != 2 || cardyy.length() != 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("카드번호 또는 유효기간을 다시 확인하세요.")
                            .setNegativeButton("다시 시도", null)
                            .create();
                    dialog.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("결제가 완료되었습니다")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    orderlistFragment = new OrderlistFragment();
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.content_frame, orderlistFragment);
                                    transaction.commit();
                                }
                            })
                            .create();
                    dialog.show();
                    new BackgroundTask3().execute();
                }


            }
        });
        paycancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                dialog = builder.setMessage("결제가 취소되었습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                shoppingFragment = new ShoppingFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.content_frame, shoppingFragment);
                                transaction.commit();

                            }

                        })
                        .create();
                dialog.show();

            }
        });

        cardnumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(cardnumber1.length()==4)
                    cardnumber2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cardnumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(cardnumber2.length()==4)
                    cardnumber3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cardnumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(cardnumber3.length()==4)
                    cardnumber4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cardnumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(cardnumber4.length()==4)
                    card_mm.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        card_mm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(card_mm.length()==2)
                    card_yy.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        card_yy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(card_yy.length()==2){
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(card_yy.getWindowToken(),0);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_pay, container, false);
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

    class BackgroundTask3 extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://coamaster.dothome.co.kr/android/shopping.php?user_id=" + user_id +
                    "&ordermenu=" + ordermenu + "&request=" + request + "&price=" + orderprice;
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
