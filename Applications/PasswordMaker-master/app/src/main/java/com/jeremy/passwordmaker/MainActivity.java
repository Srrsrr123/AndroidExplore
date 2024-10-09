package com.jeremy.passwordmaker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

public class MainActivity extends AppCompatActivity {

    Button newPassButton;
    TextView newPass;
    TextView seekBarTextMin;
    TextView seekBarTextMax;
    CheckBox noSymbols;
    CheckBox noNumbers;
     CheckBox englishLetters;
    CheckBox englishWords;
    CheckBox germanWords;
   RngPass rngPass = new RngPass();
    ClipboardManager clipboardManager;
    ClipData dataClip;
    String text;
    SeekBar seekBarMax;
    SeekBar seekBarMin;
    public boolean Symbols;
    public boolean engOnly;
    public boolean engWords;
    public boolean gerWords;
    public boolean zeroNumbers;
    private int minNumLength;
    String values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        newPassButton = (Button) findViewById(R.id.button);
          newPass = (TextView) findViewById(R.id.NewPass);
        seekBarTextMin = (TextView) findViewById(R.id.tvSBMin);
        seekBarTextMax = (TextView) findViewById(R.id.tvSBMax);
        noSymbols = (CheckBox) findViewById(R.id.noSymbols);
        englishLetters = (CheckBox) findViewById(R.id.englishLetters);
        englishWords = (CheckBox) findViewById(R.id.englishWords);
        germanWords = (CheckBox) findViewById(R.id.germanWords);
        noNumbers = (CheckBox) findViewById(R.id.noNum);
        seekBarMin = (SeekBar) findViewById(R.id.seekBarMin);
        seekBarMax = (SeekBar) findViewById(R.id.seekBarMax);

        seekBarMin.setMax(20);





        seekBarMax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {



                int value = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset()) / seekBar.getMax());
                seekBarTextMax.setText("" +  progress);
                seekBarTextMax.setX(seekBar.getX() + value + seekBar.getThumbOffset() / 2 );


                setMax();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekBarMin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset()) / seekBar.getMax());
                seekBarTextMin.setText("" +  progress);
                seekBarTextMin.setX(seekBar.getX() + value + seekBar.getThumbOffset() / 2);

                setMax();


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        englishLetters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    englishWords.setChecked(false);
                    germanWords.setChecked(false);

                }
                if(noSymbols.isChecked() && noNumbers.isChecked())
                {
                    Toast.makeText(getApplicationContext(), "Advisable to have symbols and numbers",Toast.LENGTH_SHORT).show();
                }
        }
        });



        englishWords.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    englishLetters.setChecked(false);
                    germanWords.setChecked(false);


                }
                if(noSymbols.isChecked() && noNumbers.isChecked())
                {
                    Toast.makeText(getApplicationContext(), "Advisable to have symbols and numbers",Toast.LENGTH_SHORT).show();
                }
            }

        });


        germanWords.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    englishLetters.setChecked(false);
                    englishWords.setChecked(false);
                }
                if(noSymbols.isChecked() && noNumbers.isChecked())
                {
                    Toast.makeText(getApplicationContext(), "Advisable to have symbols and numbers",Toast.LENGTH_SHORT).show();
                }
            }
        });

        newPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setMax();

                if(seekBarMin.getProgress() == 0)
                {
                    Snackbar snackbar = Snackbar.make(v,"Please Select Min length",Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                else
                {
                    Symbols = noSymbols.isChecked();
                    engOnly = englishLetters.isChecked();
                    zeroNumbers = noNumbers.isChecked();
                    gerWords = germanWords.isChecked();
                    engWords = englishWords.isChecked();
                    minNumLength = seekBarMin.getProgress();

                    rngPass.setBoolNoSymbols(Symbols);
                    rngPass.setBoolEnglishLetters(engOnly);
                    rngPass.setBoolNoNumbers(zeroNumbers);
                    rngPass.setBoolEnglishWords(engWords);
                    rngPass.setBoolGermanWords(gerWords);
                    rngPass.setMinLength(minNumLength);

                    text = rngPass.newPass(newPass.toString());
                    values = String.valueOf(germanWords.isChecked());
                    values = String.valueOf(text);

                    newPass.setText(text);
                    newPass.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left));

                   // Toast.makeText(getApplicationContext(),values,Toast.LENGTH_SHORT).show();
                }




            }
        });

        newPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataClip = ClipData.newPlainText("text",text);
                clipboardManager.setPrimaryClip(dataClip);
                //Toast popup to display it password is in the phone's Clipboard
                Toast.makeText(getApplicationContext(),"Password Copied:\n" + text,Toast.LENGTH_SHORT).show();
            }
        });





    }

    private void setMax(){
        if (seekBarMax.getProgress() < seekBarMin.getMax())
        {
            seekBarMax.setProgress(seekBarMin.getMax());


        }
    }

    // Simple App for new passwords



}


