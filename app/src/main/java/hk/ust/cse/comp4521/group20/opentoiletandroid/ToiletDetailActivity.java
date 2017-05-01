package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import hk.ust.cse.comp4521.group20.opentoiletandroid.R;

public class ToiletDetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back to home navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        // Get the data passed from by the intent
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            collapsingToolbarLayout.setTitle(bundle.getString("ToiletName"));
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.primary_text_light));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Bookmark toilet into local storage
                Snackbar.make(view, "Successfully bookmarked the toilet.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Load web view
        webView = (WebView) findViewById(R.id.wv_main);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://pathadvisor.ust.hk/m.interface.html");

        // http://stackoverflow.com/questions/32304237/android-webview-loading-data-performance-very-slow
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyWebViewClient  extends WebViewClient {  //HERE IS THE MAIN CHANGE.

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.equals("http://pathadvisor.ust.hk/m.interface.html")) {
                // clickFillInItem({roomName:'LIFT 19', floor:'4' , coor: { x:'2080', y:'1810'} , roomId:'p228'})
                webView.loadUrl("javascript:clickFillInItem({roomName:'LIFT 19', floor:'4' , coor: { x:'2080', y:'1810'} , roomId:'p228'});");
            }
        }
    }
}
