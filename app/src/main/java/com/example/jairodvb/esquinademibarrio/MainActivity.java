package com.example.jairodvb.esquinademibarrio;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;


public class MainActivity extends ActionBarActivity
{
    private String APP_DIRECTORY = "EsquinademiBarrio/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private String TEMPORAL_PICTURE_NAME = "temporal.jpg";

    private final int PHOTOCODE = 100;
    private final int SELECT_PICTURE = 200;

    private ImageView ImagenView;


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImagenView = (ImageView) findViewById(R.id.setPicture);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                final CharSequence[] options = {"Tomar foto", "Elegir galeria", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Escoja una opción: ");
                builder.setItems(options, new DialogInterface.OnClickListener()
                {
                   @Override
                    public void onClick(DialogInterface dialog, int seleccion)
                   {
                       if (options[seleccion] == "Tomar foto")
                       {
                           openCamera();
                       } else  if (options[seleccion] == "Elegir galeria")
                       {
                           Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                           intent.setType("image/=");
                           startActivityForResult(intent.createChooser(intent, "Seleccionar imagen"), SELECT_PICTURE);
                       } else if (options[seleccion] == "Cancelar")
                       {
                           dialog.dismiss();
                       }
                   }
                });
            }
        });
    }

    private void openCamera()
    {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        file.mkdirs();

        String ruta = Environment.getExternalStorageState() + File.separator + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;

        File nuevafile = new File(ruta);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(nuevafile));
        startActivityForResult(intent, PHOTOCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case PHOTOCODE:
                if(resultCode == RESULT_OK)
                {
                    String dir = Environment.getExternalStorageState() + File.separator + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;
                    decodeBitmap(dir);
                }
                break;
            case SELECT_PICTURE:
                if(resultCode == RESULT_OK)
                {
                    Uri ruta = data.getData();
                    ImagenView.setImageURI(ruta);
                }
        }
    }

    private void decodeBitmap(String dir)
    {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(dir);

        ImagenView.setImageBitmap(bitmap);
    }
}