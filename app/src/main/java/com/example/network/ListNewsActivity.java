package com.example.network;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.network.adapter.NewsAdapter;
import com.example.network.interfaces.NewsOnClick;
import com.example.network.model.Item;
import com.example.network.network.APIManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListNewsActivity extends AppCompatActivity {
    RecyclerView rvListNews;
    List<Item> listDatas;
    NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        getListData();

        listDatas = new ArrayList<>();
        adapter = new NewsAdapter(ListNewsActivity.this, listDatas);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);

        rvListNews = findViewById(R.id.rvListNews);
        rvListNews.setLayoutManager(layoutManager);
        rvListNews.setAdapter(adapter);

        adapter.setiOnClick(new NewsOnClick() {
            @Override
            public void onClickItem(int position) {
                Item model = listDatas.get(position);
                Intent intent = new Intent(ListNewsActivity.this, DetailActivity.class);
                intent.putExtra("URL", model.getContent().getUrl());
                startActivity(intent);
            }
        });
    }

    private void getListData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIManager.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIManager service = retrofit.create(APIManager.class);
        service.getListData().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(response.body() != null) {
                    listDatas = response.body();
                    adapter.reloadData(listDatas);
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(ListNewsActivity.this, "Fail", Toast.LENGTH_LONG).show();
            }
        });
    }
}