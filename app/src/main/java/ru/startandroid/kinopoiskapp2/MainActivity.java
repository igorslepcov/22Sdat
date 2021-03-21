package ru.startandroid.kinopoiskapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView mRecyclerViewHor;

    List<Movies> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovies = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.rvMainMovies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        MoviesAdapter moviesAdapter = new MoviesAdapter(mMovies);
        mRecyclerView.setAdapter(moviesAdapter);

        // Horizontal start

        mRecyclerViewHor = (RecyclerView) findViewById(R.id.rvTrendMovies);
        LinearLayoutManager layoutManagerHor = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewHor.setLayoutManager(layoutManagerHor);
        MoviesAdapterHor moviesAdapterHor = new MoviesAdapterHor(mMovies);
        mRecyclerViewHor.setAdapter(moviesAdapterHor);

        // Horizontal End

        final Call<List<Movies>> call = ApiClient.getService().getMovie();
        call.enqueue(new Callback<List<Movies>>() {
            @Override
            public void onResponse(Call<List<Movies>> call, Response<List<Movies>> response) {
                if (response.isSuccessful()) {
                    mMovies.addAll(response.body());
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    mRecyclerViewHor.getAdapter().notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Movies>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
