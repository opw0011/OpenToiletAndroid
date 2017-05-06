package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PathAdvisorActivity extends AppCompatActivity {
    private static final String TAG = "PathAdvisorActivity";

    private WebView mWebView;
    private static String toiletId;
    private int toiletX, toiletY;
    private String toiletFloor, toiletName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_advisor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Get the data passed by the intent
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            toiletId = bundle.getString("ToiletId");
            // get the x, y coordinate of the toilet
            toiletName = bundle.getString("name");
            toiletX = bundle.getInt("pos_x");
            toiletY = bundle.getInt("pos_y");
            toiletFloor= bundle.getString("floor");
            Log.d(TAG, String.format("%s (%d, %d)", toiletFloor, toiletX, toiletY));
        }

        getSupportActionBar().setTitle("Path Advisor");

        mWebView = (WebView) findViewById(R.id.mWebView);
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // clickFillInItem({roomName:'LIFT 19', floor:'4' , coor: { x:'2080', y:'1810'} , roomId:'p228'})
                String jsFunctionString =
                        String.format("javascript:setTimeout(function()" +
                                    "{clickFillInItem({roomName:'%s', floor:'%s', " +
                                    "coor: { x:'%d', y:'%d'} , roomId:''})}, 1000);"
                                , toiletName, toiletFloor, toiletX, toiletY);
                Log.d(TAG, jsFunctionString);
                mWebView.loadUrl(jsFunctionString);
            }
        });

        mWebView.loadUrl("http://pathadvisor.ust.hk/m.interface.html");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PathAdvisorActivity.this, ToiletDetailActivity.class);
        intent.putExtra("ToiletId", toiletId);
        setResult(RESULT_OK, intent);
        finish();
    }

}
