package example.tutorial.githubupload.mvvm.githubrepo1;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import example.tutorial.githubupload.mvvm.githubrepo1.models.ContentEntity;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_VIDEO = 1;
    DatabaseReference databaseReference;
    ContentEntity contentEntity;
    UploadTask uploadTask;
    private VideoView videoView;
    private AppCompatEditText title, search;
    private ProgressBar progressBar;
    private AppCompatButton upload, pickVideo;
    private Uri contentUri;
    private MediaController mediaController;
    //firebase
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentEntity = new ContentEntity();
        storageReference = FirebaseStorage.getInstance().getReference("Video");
        databaseReference = FirebaseDatabase.getInstance().getReference("Video");

        upload = findViewById(R.id.button);
        pickVideo = findViewById(R.id.pick_video);
        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
        title = findViewById(R.id.editTextTextPersonName);
        search = findViewById(R.id.editTextTextPersonName3);

        pickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_VIDEO);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideo();
            }
        });

    }

    private void uploadVideo() {

        final String titleString = Objects.requireNonNull(title.getText()).toString();
        final String searchString = Objects.requireNonNull(search.getText()).toString().trim().toLowerCase();

        if (contentUri != null || TextUtils.isEmpty(titleString)) {
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getExtension(contentUri));
            uploadTask = reference.putFile(contentUri);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        Log.e("errpr", task.getException().toString());
                        throw Objects.requireNonNull(task.getException());
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Content saved", Toast.LENGTH_SHORT).show();
                        assert downloadUrl != null;
                        contentEntity.setContentUrl(downloadUrl.toString());
                        contentEntity.setSearchTitle(searchString);
                        contentEntity.setTitle(titleString);
                        String i = databaseReference.push().getKey();
                        assert i != null;
                        databaseReference.child(i).setValue(contentEntity);
                    } else {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    double progress = (100.0 * taskSnapshot.getBytesTransferred());
                    progressBar.setProgress((int) progress, true);


                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });

        } else {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != PICK_VIDEO || resultCode == RESULT_OK || data != null || data.getData() != null) {
            assert data != null;
            contentUri = data.getData();
            videoView.setVideoURI(contentUri);
        }
    }

    private String getExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}