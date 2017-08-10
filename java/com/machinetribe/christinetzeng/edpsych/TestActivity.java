package com.machinetribe.christinetzeng.edpsych;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveApi.DriveIdResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class TestActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener  {//
    private static final String TAG = "TestActivity";
    private static final int REQUEST_CODE_RESOLUTION = 1;

    GridView promptGridView2;
    GridView solutionGridView2;
    TextView timerTextView2;
    PromptImageAdapter promtpAdapter;
    SolutionImageAdapter solutionAdapter;
    Button button;
    GoogleApiClient mGoogleApiClient;
    int currentQ, totalQ = 7, attemptOrder;
    int usedTime;
    String message;
    private DriveId mFolderDriveId;
    Handler handler;
    long MillisecondTime, StartTime;

    static final int[][] prompts = new int[][] {
            //star
            {R.drawable.t1rs1, R.drawable.t1rs2, R.drawable.t1rs3, R.drawable.t1bm1, R.drawable.t1bm2, R.drawable.t1bm3, R.drawable.t1gl1, R.drawable.t1gl2, R.drawable.blank},
            //lolipop
            {R.drawable.t2lolipop1, R.drawable.t2lolipop1, R.drawable.t2lolipop1, R.drawable.t2lolipop2, R.drawable.t2lolipop2, R.drawable.t2lolipop2, R.drawable.t2lolipop3, R.drawable.t2lolipop3, R.drawable.blank},
            //foldshape
            {R.drawable.t3p1, R.drawable.t3p2, R.drawable.t3p3, R.drawable.t3p4, R.drawable.t3p5, R.drawable.t3p6, R.drawable.t3p7, R.drawable.t3p8, R.drawable.blank},
            //shaded2
            {R.drawable.t4p1, R.drawable.t4p2, R.drawable.t4p3, R.drawable.t4p4, R.drawable.t4p5, R.drawable.t4p6, R.drawable.t4p7, R.drawable.t4p8, R.drawable.blank},
            //combineShapes
            {R.drawable.t5p1, R.drawable.t5p2, R.drawable.t5p3, R.drawable.t5p4, R.drawable.t5p5, R.drawable.t5p6, R.drawable.t5p7, R.drawable.t5p8, R.drawable.blank},
            //rotateNumbers
            {R.drawable.t6_1, R.drawable.t6_2i, R.drawable.t6_3, R.drawable.t6_3, R.drawable.t6_2i, R.drawable.t6_1, R.drawable.t6_1, R.drawable.t6_2i, R.drawable.blank},
            //xmarks
            {R.drawable.t7_1, R.drawable.t7_1, R.drawable.t7_2, R.drawable.t7_3, R.drawable.t7_3, R.drawable.t7_4, R.drawable.t7_5, R.drawable.t7_5, R.drawable.blank}};
            //123
//            {R.drawable.t8_1, R.drawable.t8_2, R.drawable.t8_3, R.drawable.t8_4, R.drawable.t8_5, R.drawable.t8_6, R.drawable.t8_7, R.drawable.t8_8, R.drawable.blank}};
//    promts of T9
//    prompt of T10
    static final int[][] options = new int[][] {
            {R.drawable.t1ys3, R.drawable.t1yl3, R.drawable.t1gs3, R.drawable.t1bm2, R.drawable.t1gl1, R.drawable.t1gm3, R.drawable.t1gl3, R.drawable.t1rs3},
            {R.drawable.t2lolipop3, R.drawable.t2lolipop2, R.drawable.t2lolipop3, R.drawable.t2lolipop2, R.drawable.t2lolipop1, R.drawable.t2lolipop3, R.drawable.t2lolipop1, R.drawable.t2lolipop1},
            {R.drawable.t3s1, R.drawable.t3s2, R.drawable.t3s3, R.drawable.t3s4, R.drawable.t3s5, R.drawable.t3s6, R.drawable.t3s7, R.drawable.t3s8},
            {R.drawable.t4s1, R.drawable.t4p4, R.drawable.t4s3, R.drawable.t4p3, R.drawable.t4s5, R.drawable.t4s6, R.drawable.t4s7, R.drawable.t4s8},
            {R.drawable.t5p6, R.drawable.t5s2, R.drawable.t5p8, R.drawable.t5p3, R.drawable.t5p7, R.drawable.t5s6, R.drawable.t5s7, R.drawable.t5s8},
            {R.drawable.t6_3, R.drawable.t6_2i, R.drawable.t6_1, R.drawable.t6_1, R.drawable.t6_4, R.drawable.t6_3, R.drawable.t6_2, R.drawable.t6_3},
            {R.drawable.t7_2, R.drawable.t7_6, R.drawable.t7_7, R.drawable.t7_8, R.drawable.t7_9, R.drawable.t7_6, R.drawable.t7_10, R.drawable.t7_4}};
//option of T8
//option of T9
//option of T10
    static final int[][] promptAngles = new int[][] {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 90, 180, 0, 90, 180, 0, 90, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 90, 270, 270, 0, 180, 180, 0},
            {0, 90, 0, 0, 90, 0, 0, 90, 0}};
//            {0, 0, 0, 0, 0, 0, 0, 0, 0}};
    static final int[][] optionAngles = new int[][] {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {270, 270, 180, 90, 180, 90, 90, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 270, 0, 0, 270, 0, 90},
            {0, 0, 0, 0, 0, 90, 0, 0}};
//            {0, 0, 0, 0, 0, 0, 0, 0}};

    static final String[] optionMapping = {"A","B","C","D","E","F","G","H"};

    ArrayList<String> attempts = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        promptGridView2 = (GridView) findViewById(R.id.promptGridView2);
        solutionGridView2 = (GridView) findViewById(R.id.solutionGridView2);
        timerTextView2 = (TextView) findViewById(R.id.timerTextView2);
        button = (Button) findViewById(R.id.nextBtn2);

        handler = new Handler() ;

        attemptOrder = 1;

        //define a countdown timer with 90s for each question
        final CountDownTimer countDownTimer = new CountDownTimer(90000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextView2.setText("Time remaining: " + (millisUntilFinished / 1000)/60 + ":" + (millisUntilFinished / 1000)%60);
                usedTime = (int) (90 - (millisUntilFinished / 1000));//201707
            }

            public void onFinish() {
                timerTextView2.setText("Time out!");
                button.performClick();
                attemptOrder = 1;
            }
        };

        //get data from main activity
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_INFO);

        currentQ = 0;
        promtpAdapter = new PromptImageAdapter(this, prompts[currentQ], promptAngles[currentQ]);
        promptGridView2.setAdapter(promtpAdapter);

        solutionAdapter = new SolutionImageAdapter(this, options[currentQ], optionAngles[currentQ]);
        solutionGridView2.setAdapter(solutionAdapter);

        solutionGridView2.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                attempts.add(message + ",M" + (currentQ + 1) + "," + attemptOrder + "," + optionMapping[position] + "," + usedTime + "\n");
                stopTimer();
                resetTimer();
                attemptOrder++;
                promtpAdapter.updateImage(options[currentQ][position], optionAngles[currentQ][position]);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                stopTimer();

                attemptOrder = 1;

                if (currentQ == totalQ - 2) {
                    button.setText("Finish");
                }
                if (currentQ == totalQ - 1) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            TestActivity.this);
                    alertDialogBuilder.setTitle(R.string.gz);
                    alertDialogBuilder
                            .setMessage(R.string.test_done_msg)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    sendFile();
                                    TestActivity.this.finish();
                                }
                            });

                    AlertDialog dialog = alertDialogBuilder.create();
                    if(!TestActivity.this.isFinishing()) {
                        dialog.show();
                    }

                    //reset target data
                    for (int i = 0 ; i < prompts.length ; i++) {
                        prompts[i][8] = R.drawable.blank;
                        promptAngles[i][8] = 0;
                    }
                } else {
                    currentQ++;
                    promtpAdapter.updateImages(prompts[currentQ], promptAngles[currentQ]);
                    solutionAdapter.updateImages(options[currentQ], optionAngles[currentQ]);
                    countDownTimer.start();
                    resetTimer();
                }
            }
        });

        button.setText("Next");

        countDownTimer.start();
        resetTimer();

    }

    //create file with google drive API
    private void sendFile() {
        // create new contents resource
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {

            /**
             * Create the API client and bind it to an instance variable.
             * We use this instance as the callback for connection and connection failures.
             * Since no account name is passed, the user is prompted to choose.
             */
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Drive.DriveApi.fetchDriveId(mGoogleApiClient, "0B3njbRhFgnYIc2tHYVQ4Y1lzTjA")
                .setResultCallback(idCallback);

    }

    //2017070x
    final private ResultCallback<DriveIdResult> idCallback = new ResultCallback<DriveIdResult>() {
        @Override
        public void onResult(DriveIdResult result) {
            if (!result.getStatus().isSuccess()) {
                Toast.makeText(getApplicationContext(), "Cannot find DriveId. Are you authorized to view this file?", Toast.LENGTH_LONG).show();
                return;
            }
            mFolderDriveId = result.getDriveId();
//            Drive.DriveApi.newDriveContents(mGoogleApiClient)
//                    .setResultCallback(driveContentsCallback);
        }
    };

    @Override
    public void onConnectionSuspended(int cause) {

        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());

        if (!result.hasResolution()) {

            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }

        try {

            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);

        } catch (SendIntentException e) {

            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {

            // disconnect Google API client connection
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    final ResultCallback<DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveContentsResult>() {
        @Override
        public void onResult(DriveContentsResult result) {

            if (result.getStatus().isSuccess()) {
                    CreateFileOnGoogleDrive(result);
            }
        }
    };

    //Create a file in assigned folder with MetadataChangeSet object.
    public void CreateFileOnGoogleDrive(DriveContentsResult result){

        final DriveContents driveContents = result.getDriveContents();

        final DriveFolder folder = mFolderDriveId.asDriveFolder();

        // Perform I/O off the UI thread.
        new Thread() {
            @Override
            public void run() {
                // write content to DriveContents
                OutputStream outputStream = driveContents.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream);
                try {
                    for (String content : attempts) {
                        writer.write(content);
                    }

                    writer.close();

                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }

                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                        .setTitle(message)
                        .setMimeType("text/csv")
                        .setStarred(true).build();

//                // create a file in root folder
//                Drive.DriveApi.getRootFolder(mGoogleApiClient)
//                        .createFile(mGoogleApiClient, changeSet, driveContents).setResultCallback(fileCallback);

                folder.createFile(mGoogleApiClient, changeSet, driveContents).setResultCallback(fileCallback);
            }
        }.start();
    }

    //Handle result of Created file
    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
    ResultCallback<DriveFolder.DriveFileResult>() {
        @Override
        public void onResult(DriveFolder.DriveFileResult result) {
            if (result.getStatus().isSuccess()) {
                Toast.makeText(getApplicationContext(), "Data sent", Toast.LENGTH_LONG).show();
            }

            return;

        }
    };

    //reset timer for each click
    private void resetTimer() {
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }

    //stop timer and update reported time for each click
    private void stopTimer() {
        handler.removeCallbacks(runnable);
        usedTime = (int) (MillisecondTime / 1000);
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            handler.postDelayed(this, 0);
        }

    };
}
