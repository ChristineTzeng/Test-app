package com.machinetribe.christinetzeng.edpsych;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_INFO = "com.machinetribe.christinetzeng.edpsych.INFO";

    ImageButton tutorialImageButton;
    ImageButton testImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
    }

    public void addListenerOnButton() {

        tutorialImageButton = (ImageButton) findViewById(R.id.imageButton1);
        testImageButton = (ImageButton) findViewById(R.id.imageButton2);

        tutorialImageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
                startActivity(intent);

            }
        });
        testImageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                final EditText edittext = new EditText(MainActivity.this);
                alertDialogBuilder.setTitle(R.string.testTitle);
                alertDialogBuilder.setMessage(R.string.test_required_info);

                alertDialogBuilder.setView(edittext);

                alertDialogBuilder.setPositiveButton("Start Testing", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (edittext.getText().toString().matches("")) {
                            Toast.makeText(MainActivity.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(MainActivity.this, TestActivity.class);
                        intent.putExtra(EXTRA_INFO, edittext.getText().toString());
                        startActivity(intent);
                    }
                });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // any action for cancel
                    }
                });

                if(!MainActivity.this.isFinishing()) {
                    alertDialogBuilder.show();
                }
            }
        });

    }
}
