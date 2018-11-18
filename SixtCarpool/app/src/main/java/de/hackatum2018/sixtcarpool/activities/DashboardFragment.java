package de.hackatum2018.sixtcarpool.activities;


import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import de.hackatum2018.sixtcarpool.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DashboardFragment extends Fragment {

    WebView webView;
    Member driver = new Member("Sixt_Dashboards/User", "https://public.tableau.com/static/images/Si/Sixt_Dashboards/User/1.png", 1200, 1027);
    Member passenger = new Member("Sixt_Dashboards/User", "https://public.tableau.com/static/images/Si/Sixt_Dashboards/User/1.png", 650, 887);

    public DashboardFragment() {
        // Required empty public constructor

    }


    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        driver.scale(size);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        webView = view.findViewById(R.id.dashboard);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("WebViewSixt", consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

        });
        webView.setWebViewClient(new WebViewClient(){
            private int running = 0;

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String urlNewString) {
                running++;
                webView.loadUrl(urlNewString);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                running++;
                String url = request.getUrl().toString();
                Pattern pattern = Pattern.compile("scriptElement.src='(.*)");
                Matcher matcher = pattern.matcher(url);
                //Log.d("WebViewSixt", "shouldOverrideUrlLoading: 1. redirect to " + url);
                if(matcher.find())
                    url = matcher.group(1);
                Log.d("WebViewSixt", "shouldOverrideUrlLoading: redirect to " + url);
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                running = Math.max(running, 1);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(--running == 0) { // just "running--;" if you add a timer.
                    // TODO: finished... if you want to fire a method.
                    //Log.d("WebViewSixt", "onPageFinished: ");
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //Log.d("WebViewSixt", "onReceivedError: " + error.toString());
                super.onReceivedError(view, request, error);
            }
        });
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setBackgroundColor(0);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        String s = "<html><body style='margin:0;padding:0;'><div class='tableauPlaceholder' id='dashboard'>" +
                "<noscript><img alt=' ' src='https://public.tableau.com/static/images/Si/Sixt_Dashboards/User/1_rss.png'" +
                "style='border: none' /></noscript>" +
                "<object class='tableauViz' style='display:none;'>" +
                "<param name='host_url' value='https%3A%2F%2Fpublic.tableau.com%2F' />" +
                "<param name='embed_code_version' value='3' />" +
                "<param name='name' value='" + driver.getName() + "' />" +
                "<param name='tabs' value='no' />" +
                "<param name='toolbar' value='yes' />" +
                "<param name='static_image' value='" + driver.getStatic_image() + "' />" +
                "<param name='animate_transition' value='yes' />" +
                "<param name='display_static_image' value='yes' />" +
                "<param name='display_spinner' value='yes' />" +
                "<param name='display_overlay' value='yes' />" +
                "<param name='display_count' value='yes' />" +
                "</object>" +
                "</div>" +
                "<script type='text/javascript'>" +
                "var divElement = document.getElementById('dashboard');" +
                "var vizElement = divElement.getElementsByTagName('object')[0];" +
                "vizElement.style.width = '" + driver.getWidth() + "';" +
                "vizElement.style.height = '" + driver.getHeight() + "';" +
                "var scriptElement = document.createElement('script');" +
                "scriptElement.src='https://public.tableau.com/javascripts/api/viz_v1.js';" +
                "vizElement.parentNode.insertBefore(scriptElement, vizElement);" +
                "</script></body></html>";
        webView.loadData(s, "text/html", null);
        return view;
    }

    private class Member{
        private String name;
        private String static_image;
        private int width;
        private int height;
        private Point size;
        private int ScaleX;
        private int ScaleY;

        public Member(String name, String static_image, int width, int height){
            this.name = name;
            this.static_image = static_image;
            this.width = width;
            this.height = height;
        }


        public String getName() {
            return name;
        }

        public String getStatic_image() {
            return static_image;
        }

        public String getWidth() {
            return "" + ScaleX + "px";
        }

        public String getHeight() {
            return "" + ScaleY + "px";
        }

        public void scale(Point size){
            int ScreenX = size.x;
            int ScreenY = size.y - 64;
            ScaleX = (width * ScreenY > height * ScreenX) ? ScreenX : width * width / ScreenX;
            ScaleY = (width * ScreenY > height * ScreenX) ? height * height / ScreenY : ScreenY;
            Log.d("Dashboard", "scale: " + width + " " + height + " " + ScreenX + " " + ScreenY);
            Log.d("Dashboard", "scale2: " + ScaleX + " " + ScaleY);
        }
    }

}
