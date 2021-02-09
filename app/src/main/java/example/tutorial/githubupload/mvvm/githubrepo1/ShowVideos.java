package example.tutorial.githubupload.mvvm.githubrepo1;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import example.tutorial.githubupload.mvvm.githubrepo1.models.ContentEntity;

public class ShowVideos extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_videos);


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference("Video");


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ContentEntity> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<ContentEntity>()
                        .setQuery(databaseReference, ContentEntity.class)
                        .build();

        FirebaseRecyclerAdapter<ContentEntity, ViewHolder> adapter =
                new FirebaseRecyclerAdapter<ContentEntity, ViewHolder>(firebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull ContentEntity contentEntity) {

                        viewHolder.setData(getApplication(), contentEntity.getTitle(), contentEntity.getContentUrl());
                    }

                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item, parent, false);
                        return new ViewHolder(view);
                    }
                };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}