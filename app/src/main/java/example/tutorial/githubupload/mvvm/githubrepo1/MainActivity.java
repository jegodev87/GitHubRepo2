package example.tutorial.githubupload.mvvm.githubrepo1;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Git updated", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Edit Text added", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Added a button", Toast.LENGTH_SHORT).show();
    }
}