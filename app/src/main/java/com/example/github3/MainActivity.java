package com.example.github3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private TextView mTextView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.TextView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
                final retrofit2.Call<List<Repos>> call =
                        gitHubService.getRepos("Artemisisp");

                call.enqueue(new Callback<List<Repos>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Repos>> call, Response<List<Repos>> response) {
                       if (response.isSuccessful()){
                           mTextView.setText(response.body().toString()+"\n");
                           for (int i = 0; i< response.body().size(); i++){
                               mTextView.append(response.body().get(i).getName()+"\n");
                           }
                           mProgressBar.setVisibility(View.INVISIBLE);
                       }else {
                           int statusCode = response.code();

                           ResponseBody errorBody = response.errorBody();
                           try {
                               mTextView.setText(errorBody.string());
                               mProgressBar.setVisibility(View.INVISIBLE);
                           } catch (IOException e){
                               e.printStackTrace();
                           }
                       }
                    }

                    @Override
                    public void onFailure(Call<List<Repos>> call, Throwable throwable) {
                        final TextView textView = (TextView) findViewById(R.id.TextView);
                        textView.setText("Что-то пошло не так: " + throwable.getMessage());
                    }
                });
            }
        });
    }
}