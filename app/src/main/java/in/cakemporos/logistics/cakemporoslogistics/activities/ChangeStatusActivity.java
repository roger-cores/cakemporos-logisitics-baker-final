package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import in.cakemporos.logistics.cakemporoslogistics.R;

/**
 * Created by maitr on 16-Aug-16.
 */
public class ChangeStatusActivity extends BaseActivity {
    private ImageButton home;
    private RadioGroup radioGroup;
    private Toolbar toolbar;
    private TextView header;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_order_status);
        //find views
        home=(ImageButton)findViewById(R.id.home_img_button_status_change);
        radioGroup=(RadioGroup)findViewById(R.id.radiogroup_status);
        toolbar = (Toolbar) findViewById(R.id.change_order_status_toolbar);
        header = (TextView) findViewById(R.id.change_order_status_header);

        String headerText = getIntent().getExtras().getString("id");
        if(headerText!=null) header.setText(headerText);

        toolbar.inflateMenu(R.menu.menu_change_order_status);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.change_order_status_done){
                    Intent resultIntent=new Intent();
                    int pass_value=radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));
                    resultIntent.putExtra("status",pass_value);
                    setResult(2,resultIntent);
                    finish();
                    return true;
                }
                return false;
            }
        });
        //onclick
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        //
        //disable radio buttons depending on status
        Intent pastIntent=getIntent();
        String status=pastIntent.getStringExtra("status");
        Toast.makeText(this,status,Toast.LENGTH_SHORT).show();
        switch (status)
        {
            case "READY":
                radioGroup.getChildAt(0).setEnabled(false);
                break;
            case "DISPATCHED":
                radioGroup.getChildAt(0).setEnabled(false);
                radioGroup.getChildAt(1).setEnabled(false);
                radioGroup.getChildAt(2).setEnabled(false);
                break;
            case "CANCELLED":
                radioGroup.getChildAt(0).setEnabled(false);
                radioGroup.getChildAt(1).setEnabled(false);
                radioGroup.getChildAt(2).setEnabled(false);
                break;
            default:
                Toast.makeText(this,"Pending/Delivered",Toast.LENGTH_SHORT).show();
        }
    }
}
