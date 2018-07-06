package sg.edu.rp.c346.mybmicalculator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etWeight;
    EditText etHeight;
    Button btnCalculate;
    Button btnReset;
    TextView tvDate;
    TextView tvBMI;
    TextView tvBMIRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWeight = findViewById(R.id.editTextWeight);
        etHeight = findViewById(R.id.editTextHeight);
        btnCalculate = findViewById(R.id.buttonCalculate);
        btnReset = findViewById(R.id.buttonReset);
        tvDate = findViewById(R.id.textViewDate);
        tvBMI = findViewById(R.id.textViewBMI);
        tvBMIRange = findViewById(R.id.textViewBMIRange);


        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check of Edit text is empty
                if (etHeight.getText().toString().trim().isEmpty() || etWeight.getText().toString().trim().isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Either Weight or Height is empty", Toast.LENGTH_LONG);
                    toast.show();
                }
                //Check if Values are out of range height 0.2m - 3.0m, weight 20kg - 500kg
                else if(Float.valueOf(etHeight.getText().toString()) > 3.0 || Float.valueOf(etHeight.getText().toString()) < 0.2
                        || Float.valueOf(etWeight.getText().toString()) < 20 ||  Float.valueOf(etWeight.getText().toString()) > 500.0 ){
                    Toast toast = Toast.makeText(getApplicationContext(),  "Either height or weight is out of range", Toast.LENGTH_LONG);
                    toast.show();
                }
                //No other errors
                else {
                    float floatWeight = Float.valueOf(etWeight.getText().toString());
                    float floatHeight = Float.valueOf(etHeight.getText().toString());
                    Calendar now = Calendar.getInstance();  //Create a Calendar object with current date and time
                    String datetime = now.get(Calendar.DAY_OF_MONTH) + "/" +
                            (now.get(Calendar.MONTH) + 1) + "/" +
                            now.get(Calendar.YEAR) + " " +
                            now.get(Calendar.HOUR_OF_DAY) + ":" +
                            now.get(Calendar.MINUTE);

                    float floatBMI = floatWeight / (floatHeight * floatHeight);


                    tvDate.setText("Last Calculated Date: " + datetime);
                    tvBMI.setText("Last Calculated BMI: " + floatBMI);
                    if (floatBMI < 18.5) {
                        tvBMIRange.setText("You are Underweight");
                    } else if (floatBMI < 25.0) {
                        tvBMIRange.setText("Your BMI is normal");
                    } else if (floatBMI < 30) {
                        tvBMIRange.setText("You are Overweight");
                    } else if (floatBMI >= 30) {
                        tvBMIRange.setText("You are obese");
                    } else {
                        tvBMIRange.setText("");
                    }
                    etWeight.setText("");
                    etHeight.setText("");

                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDate.setText(null);
                tvBMI.setText(null);
                tvBMIRange.setText(null);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        String strBMI = tvBMI.getText().toString();
        String strDate = tvDate.getText().toString();
        String strBMIRange = tvBMIRange.getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor preEdit = prefs.edit();

        preEdit.putString("BMI",strBMI);
        preEdit.putString("date", strDate);
        preEdit.putString("BMIRange", strBMIRange);

        preEdit.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        tvDate.setText(prefs.getString("date","Last Calculated Date: "));
        tvBMI.setText(prefs.getString("BMI","Last Calculated BMI: "));
        tvBMIRange.setText(prefs.getString("BMIRange",""));

    }
}
