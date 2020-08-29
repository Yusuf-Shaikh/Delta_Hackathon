package com.example.photoediter_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class EditorActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");

        imageView = findViewById(R.id.image);
        Glide.with(EditorActivity.this).load(path).centerCrop().into(imageView);
    }
}