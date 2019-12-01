package link.clock;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ClockView clockView;
    static public int minuts;
    static public int hours;
    static boolean isAuto=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    protected void rbClick(View view){
        EditText min = (EditText)findViewById(R.id.etMin);
        EditText h = (EditText)findViewById(R.id.etHours);
        CheckBox rb = (CheckBox)findViewById(R.id.rbAuto);
        if(rb.isChecked()){
            isAuto=true;
            min.setEnabled(true);
            h.setEnabled(true);
        }
        else{

            try {

                minuts = Integer.parseInt(min.getText().toString());

                hours = Integer.parseInt(h.getText().toString());
            }
            catch(Exception e){
                Toast.makeText(this,"ERROR - Incorrect format! ", Toast.LENGTH_LONG).show();
                rb.setChecked(true);
                min.setEnabled(true);
                h.setEnabled(true);
                return;
            }

            if(minuts>=60 || hours>25){
                Toast.makeText(this,"ERROR - Incorrect format!", Toast.LENGTH_LONG).show();
                rb.setChecked(true);
                min.setEnabled(true);
                h.setEnabled(true);
                return;
            }
            hours = hours>12 ? hours%12 : hours;
            min.setEnabled(false);
            h.setEnabled(false);
            isAuto=false;
        }
    }
}
