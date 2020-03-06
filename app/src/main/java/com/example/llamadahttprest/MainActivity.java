package com.example.llamadahttprest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import domain.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import service.PostInterface;

public class MainActivity extends AppCompatActivity {

    private Button btnGet;
    private Button btnPost;
    private LinearLayout llayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializaComponentes();
    }

    private void inicializaComponentes() {
        llayout = findViewById(R.id.llayout);
        btnGet = findViewById(R.id.btnGet);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String miUrl = "http://jsonplaceholder.typicode.com/";
                Retrofit retrofit = new Retrofit.Builder().baseUrl(miUrl).addConverterFactory(GsonConverterFactory.create()).build();

                PostInterface post =retrofit.create(PostInterface.class);
                Call<List<Post>> llamada = post.getPosts();
                llamada.enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        List<Post> lista = response.body();
                        for(Post p: lista){
                            Log.i("RESULTADO:",p.toString());
                            Button b = new Button(MainActivity.this);
                            b.setText(p.getId());
                            llayout.addView(b);
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String miUrl = "http://jsonplaceholder.typicode.com/";
                                    Retrofit retrofit = new Retrofit.Builder().baseUrl(miUrl).addConverterFactory(GsonConverterFactory.create()).build();
                                    PostInterface post2 =retrofit.create(PostInterface.class);
                                    Call<List<Post>> llamada = post2.getPosts();
                                    Call<Post> post = post2.getPost(((Button)v).getText().toString());
                                    post.enqueue(new Callback<Post>() {
                                        @Override
                                        public void onResponse(Call<Post> call, Response<Post> response) {
                                            Toast.makeText(MainActivity.this, "hemos pulsado sobre :"+response.body(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Post> call, Throwable t) {

                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {

                    }
                });
            }
        });
        btnPost = findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String miUrl = "http://jsonplaceholder.typicode.com/";
                Retrofit retrofit = new Retrofit.Builder().baseUrl(miUrl).addConverterFactory(GsonConverterFactory.create()).build();

                PostInterface post =retrofit.create(PostInterface.class);
                Post p = new Post();
                p.setTitle("mi titulo");
                p.setUserId(1);
                p.setBody("cuerpo");
                Call<Post> res=post.createPost(p);
                res.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        Toast.makeText(MainActivity.this, "res vale :"+response.body().getId(), Toast.LENGTH_SHORT).show();
                        System.out.println("res vale "+response.body());
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });

            }
        });
    }
}
