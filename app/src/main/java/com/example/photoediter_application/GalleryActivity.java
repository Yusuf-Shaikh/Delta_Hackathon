package com.example.photoediter_application;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GalleryActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<File>list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        gridView = (GridView)findViewById(R.id.grid_view);

    }

    private static final int REQUEST_PERMISSIONS = 1234;

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static  final int PERMISSIONS_COUNT = 2;

    @SuppressLint("NewApi")
    private boolean arePermissionsDenied(){
        for (int i=0;i<PERMISSIONS_COUNT;i++){
            if(checkSelfPermission(PERMISSIONS[i])!= PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        return false;
    }

    @SuppressLint("NewApi")
    @Override
    public void  onRequestPermissionsResult(final int requestCode, final String[] permissions,
                                            final int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSIONS && grantResults.length>0){
            if (arePermissionsDenied()){
                ((ActivityManager) Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE)))
                        .clearApplicationUserData();
                recreate();
            }else {
                onResume();
            }
        }
    }

    private boolean isGalleryInitizlized;

    private List<String> filesList;
    private void addimages(String dirPath){
        final File imagesDir = new File(dirPath);
        final File[] files = imagesDir.listFiles();
        for (File file : files) {
            final String path = file.getAbsolutePath();
            if (path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".jpeg")) {
                filesList.add(path);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && arePermissionsDenied()){
            requestPermissions(PERMISSIONS,REQUEST_PERMISSIONS);
            return;
        }
        if(!isGalleryInitizlized){
            filesList= new ArrayList<>();
            addimages(String.valueOf(Environment.
                    getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
            addimages(String.valueOf(Environment.
                    getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
            addimages(String.valueOf(Environment.
                    getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)));

            final GridView gridView = findViewById(R.id.grid_view);
            final ImageAdapter imageAdapter = new ImageAdapter();

            imageAdapter.setData(filesList);
            gridView.setAdapter(imageAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(GalleryActivity.this, filesList.get(position), Toast.LENGTH_SHORT).show();
                    editor(filesList.get(position));
                    return;
                }
            });

            isGalleryInitizlized = true;
        }
    }

    private void editor(String path) {
        Intent intent = new Intent(GalleryActivity.this,EditorActivity.class);
        intent.putExtra("path",path);
        startActivity(intent);
    }


    public class ImageAdapter extends BaseAdapter {

        private List<String> data = new ArrayList<>();
        void setData(List<String>data){
            if(this.data.size()>0){
                data.clear();
            }
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            if (convertView==null){
                imageView = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,
                        parent,false);
            }else {
                imageView = (ImageView) convertView;
            }
            Glide.with(GalleryActivity.this).load(data.get(position)).centerCrop().into(imageView);
            return imageView;
        }
    }

}