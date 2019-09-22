package com.aaron.nicedialogdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aaron.nicedialoglibrary.NiceDialog;

public class MainActivity extends AppCompatActivity {
    private NiceDialog niceDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(niceDialog != null){
                    niceDialog.dismiss();
                    niceDialog = null;
                }
                showDialog("dddddd");
            }
        });
    }

    public void showDialog(String title) {
//        niceDialog = NiceDialog.createProgressDialog(getSupportFragmentManager(), title);
//        niceDialog = NiceDialog.createDialogWithConfirmButton(getSupportFragmentManager()
//                , title, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        niceDialog.dismiss();
//                    }
//                });
        niceDialog = NiceDialog.createDialogWithAllFunction(getSupportFragmentManager()
                , title, "34343"
                , view -> Toast.makeText(MainActivity.this,"点击了取消",Toast.LENGTH_SHORT).show()
                , view -> Toast.makeText(MainActivity.this,"点击了ok",Toast.LENGTH_SHORT).show());
    }
}
