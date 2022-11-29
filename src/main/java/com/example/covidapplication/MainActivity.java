package com.example.covidapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText et;
    ImageView flag;
    RecyclerView rv;
    List<CountryModule> list;
    MaterialButton fetch_btn;
    TextView act_n,recov_n,aff_n,death_n;
    Switch swtch;

    interface GetInfos{
        void sendCountries(List<CountryModule> list);
        void sendSlug(String slug);
        void sendStats(String deaths,String recovered,String active,String affected);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.et);
        flag = findViewById(R.id.logo);
        rv = findViewById(R.id.rv);
        fetch_btn = findViewById(R.id.confirm_btn);
        act_n = findViewById(R.id.act_n);
        recov_n = findViewById(R.id.recov_n);
        aff_n = findViewById(R.id.aff_n);
        death_n = findViewById(R.id.death_n);
        swtch = findViewById(R.id.switch1);

        swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                System.out.println(swtch.isChecked());
                if(b){
                    et.setText("World");
                    Picasso.get().load("https://icons.iconarchive.com/icons/treetog/junior/256/earth-icon.png").into(flag);
                }
            }
        });

        list = new ArrayList<>();
        DataFromApi dataFromApi = new DataFromApi(MainActivity.this);
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    dataFromApi.listAllCountries(new GetInfos() {
                        @Override
                        public void sendCountries(List<CountryModule> resList) {
                            list.clear();
                            for(CountryModule countryModule:resList){
                                list.add(countryModule);
                            }
                            rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            MyAdapter adapter = new MyAdapter(MainActivity.this,list,et,flag);
                            rv.setAdapter(adapter);
                        }
                        @Override
                        public void sendSlug(String slug) { }
                        @Override
                        public void sendStats(String deaths, String recovered, String active, String affected) { }
                    });

            }}
        });


        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int size = list.size();
                List<CountryModule> newList = new ArrayList<>();
                for(int i =0; i < size;i++){
                    if(list.get(i).getCountryName().startsWith(editable.toString())){
                        newList.add(list.get(i));
                        System.out.println(list.get(i));
                    }

                }
                System.out.println("list size"+list.size());
                System.out.println("new list size"+newList.size());
                rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                MyAdapter adapter = new MyAdapter(MainActivity.this, newList,et,flag);
                rv.setAdapter(adapter);
            }
        });
        fetch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(swtch.isActivated());
                if(et.getText().toString().isEmpty()){
                    et.setError("Please select a country.");
                    return ;
                }
                if(swtch.isChecked()){
                    System.out.println("Global Search");
                    dataFromApi.getGlobalStats(new GetInfos() {
                        @Override
                        public void sendCountries(List<CountryModule> list) {

                        }

                        @Override
                        public void sendSlug(String slug) {

                        }

                        @Override
                        public void sendStats(String deaths, String recovered, String active, String affected) {
                            act_n.setText(active);
                            death_n.setText(deaths);
                            aff_n.setText(affected);
                            recov_n.setText(recovered);
                        }
                    });
                }
                dataFromApi.getSlugFromCountry(et.getText().toString(), new GetInfos() {
                    @Override
                    public void sendCountries(List<CountryModule> list) { }

                    @Override
                    public void sendSlug(String slug) {
                        dataFromApi.getStats(slug, new GetInfos() {
                            @Override
                            public void sendCountries(List<CountryModule> list) { }
                            @Override
                            public void sendSlug(String slug) { }
                            @Override
                            public void sendStats(String deaths, String recovered, String active, String affected) {
                                act_n.setText(active);
                                death_n.setText(deaths);
                                aff_n.setText(affected);
                                recov_n.setText(recovered);
                            }
                        });
                    }
                    @Override
                    public void sendStats(String deaths, String recovered, String active, String affected) { }
                });
            }
        });
    }
}