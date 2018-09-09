package com.example.isaiasspiry.tomarfoto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    
    private ImageView img;
    //private Button btnfoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img=(ImageView)findViewById(R.id.imageView2);
        //btnfoto=(Button) findViewById(R.id.btnfoto);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }
    }

    //Metodo para crear un nombre unico a cada fotografia (solo se genera el nombre)
    String mCurrentPhotoPath;
    private File createImageFile() throws IOException{
        //Crea un nombre del archivo de imagen
        String timeStamp= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName= "JPEG_"+ timeStamp + "_";
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image= File.createTempFile(imageFileName,".jpg", storageDir);

        mCurrentPhotoPath=image.getAbsolutePath();
        return image;
    }

    //Metodo para tomar foto y crear el archivo
    static final int REQUEST_TAKE_PHOTO = 1;
    public void TomarFoto(View view){
        //crear un objeto de tipo intent, indica a la aplicacion que cerrara de manera momenta el activity principal para abrir otro activity para ver lo que esta tomando la camara
        Intent takePictureIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (IOException ex){

            }

            if(photoFile!=null){
                Uri photoURI= FileProvider.getUriForFile(this,"com.example.android.FileProvider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent,REQUEST_TAKE_PHOTO);
            }
        }
    }

    //Metodo para mostrar vista previa en una imageview de la foto tomada
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap= (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);
        }
    }

}
