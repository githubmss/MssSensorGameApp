package com.mss.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author master software solutions
 *         <p>
 *         <p>
 *         This is launcher Activity that shows user option to start game
 *         (Active/passive)
 */
public class MainActivity extends Activity implements OnClickListener {

    public Button btnPlayGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPlayGame = (Button) findViewById(R.id.btn_playgame);
        btnPlayGame.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_playgame:
                startActivity(new Intent(MainActivity.this, PlayActivity.class));
                break;

            default:
                break;
        }

    }

}
