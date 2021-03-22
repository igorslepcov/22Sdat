package ru.startandroid.kinopoiskapp2.ui.home;

import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProviders;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.ArrayList;
        import java.util.List;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import ru.startandroid.kinopoiskapp2.ApiClient;
        import ru.startandroid.kinopoiskapp2.MainActivity;
        import ru.startandroid.kinopoiskapp2.Movies;
        import ru.startandroid.kinopoiskapp2.MoviesAdapter;
        import ru.startandroid.kinopoiskapp2.MoviesAdapterHor;
        import ru.startandroid.kinopoiskapp2.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    RecyclerView mRecyclerView;
    RecyclerView mRecyclerViewHor;

    List<Movies> mMovies;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        mMovies = new ArrayList<>();

        mRecyclerView = (RecyclerView) root.findViewById(R.id.rvMainMovies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        MoviesAdapter moviesAdapter = new MoviesAdapter(mMovies);
        mRecyclerView.setAdapter(moviesAdapter);

        // Horizontal start

        mRecyclerViewHor = (RecyclerView) root.findViewById(R.id.rvTrendMovies);
        LinearLayoutManager layoutManagerHor = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
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
                    Toast.makeText(getContext(), response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Movies>> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }
}
