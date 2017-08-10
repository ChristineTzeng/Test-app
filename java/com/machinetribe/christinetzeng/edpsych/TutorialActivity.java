package com.machinetribe.christinetzeng.edpsych;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

public class TutorialActivity extends AppCompatActivity {
    GridView promptGridView1;
    GridView solutionGridView1;
    PromptImageAdapter promtpAdapter;
    SolutionImageAdapter solutionAdapter;
    Button nextButton, previousButton;
    int currentQ, totalQ = 2;

    static final int[][] prompts = new int[][] {
            //example1
            {R.drawable.tu1b1, R.drawable.tu1b2, R.drawable.tu1b3, R.drawable.tu1g1, R.drawable.tu1g2, R.drawable.tu1g3, R.drawable.tu1p1, R.drawable.tu1p2, R.drawable.blank},
            //example2
            {R.drawable.tu2rectangle, R.drawable.tu2rectangle, R.drawable.tu2rectangle, R.drawable.tu2triangle, R.drawable.tu2triangle, R.drawable.tu2triangle, R.drawable.tu2house1, R.drawable.tu2house1, R.drawable.blank}};
    static final int[][] options = new int[][] {
            {R.drawable.tu1b3, R.drawable.tu1p2, R.drawable.tu1g1, R.drawable.tu1b1, R.drawable.tu1p4, R.drawable.tu1g3, R.drawable.tu1b2, R.drawable.tu1p3},
            {R.drawable.tu2house1, R.drawable.tu2rectangle, R.drawable.tu2triangle, R.drawable.tu2house2, R.drawable.tu2triangle, R.drawable.tu2rectanglentriangle, R.drawable.tu2house1, R.drawable.tu2rectangle}};
    static final int[][] promptAngles = new int[][] {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 270, 180, 0, 270, 180, 0, 270, 0}};
    static final int[][] optionAngles = new int[][] {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 180, 270, 0, 180, 0, 180, 0}};
    static final int[] solutions = new int[] {R.drawable.tu1p3, R.drawable.tu2house1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        promptGridView1 = (GridView) findViewById(R.id.promptGridView1);
        solutionGridView1 = (GridView) findViewById(R.id.solutionGridView1);
        nextButton = (Button) findViewById(R.id.nextBtn1);
        previousButton = (Button) findViewById(R.id.previousBtn1);

        currentQ = 0;
        promtpAdapter = new PromptImageAdapter(this, prompts[currentQ], promptAngles[currentQ]);
        promptGridView1.setAdapter(promtpAdapter);

        solutionAdapter = new SolutionImageAdapter(this, options[currentQ], optionAngles[currentQ]);
        solutionGridView1.setAdapter(solutionAdapter);

        solutionGridView1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                promtpAdapter.updateImage(options[currentQ][position], optionAngles[currentQ][position]);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        TutorialActivity.this);
                AlertDialog dialog;

                //display result
                if (promtpAdapter.getSelectedImage() != solutions[currentQ]) {
                    alertDialogBuilder
                            .setMessage(R.string.tutorial_wrong_msg)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    return;
                                }
                            });
                    dialog = alertDialogBuilder.create();
                    if(!TutorialActivity.this.isFinishing()) {
                        dialog.show();
                    }
                } else {
                    alertDialogBuilder
                            .setMessage(R.string.tutorial_correct_msg)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //nothing
                                    previousButton.setVisibility(View.VISIBLE);

                                    if (currentQ == 0) {
                                        nextButton.setText("Finish");
                                        currentQ++;
                                        promtpAdapter.updateImages(prompts[currentQ], promptAngles[currentQ]);
                                        solutionAdapter.updateImages(options[currentQ], optionAngles[currentQ]);
                                    } else {

                                        alertDialogBuilder.setTitle(R.string.gz);
                                        alertDialogBuilder
                                                .setMessage(R.string.tutorial_done_msg)
                                                .setCancelable(false)
                                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        TutorialActivity.this.finish();
                                                    }
                                                });

                                        AlertDialog dialog2 = alertDialogBuilder.create();
                                        if (!TutorialActivity.this.isFinishing()) {
                                            dialog2.show();
                                        }

                                        //reset target data
                                        for (int i = 0; i < prompts.length; i++) {
                                            prompts[i][8] = R.drawable.blank;
                                            promptAngles[i][8] = 0;
                                        }
                                    }
                                }
                            });
                    dialog = alertDialogBuilder.create();
                    if (!TutorialActivity.this.isFinishing()) {
                        dialog.show();
                    }

                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (currentQ != 0) {

                    currentQ--;
                    promtpAdapter.updateImages(prompts[currentQ], promptAngles[currentQ]);
                    solutionAdapter.updateImages(options[currentQ], optionAngles[currentQ]);

                    previousButton.setVisibility(View.GONE);
                    nextButton.setText("Next");
                } else {
                    previousButton.setVisibility(View.VISIBLE);
                }
            }
        });

        nextButton.setText("Next");
        previousButton.setVisibility(View.GONE);
    }
}
