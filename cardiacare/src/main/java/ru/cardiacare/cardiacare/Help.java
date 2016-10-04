package ru.cardiacare.cardiacare;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class Help extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.help_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_help));

//        // кнопка назад в ActionBar
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        // back button in left side of ActionBar
//        if (getActionBar() != null) {
//            getActionBar().setDisplayHomeAsUpEnabled(true);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*if ( item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);*/
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}