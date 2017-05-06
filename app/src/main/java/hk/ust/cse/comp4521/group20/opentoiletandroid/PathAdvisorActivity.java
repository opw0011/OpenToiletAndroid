package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PathAdvisorActivity extends AppCompatActivity {

    private WebView mWebView;
    private static String toiletId;

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
            // TODO: get the x, y coordinate of the toilet
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
                mWebView.loadUrl("javascript:setTimeout(function() " +
                        "{clickFillInItem({roomName:'LIFT 19', floor:'4' , " +
                        "coor: { x:'2080', y:'1810'} , roomId:'p228'})}, 1000);");
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
