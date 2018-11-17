package de.hackatum2018.sixtcarpool.activities;


import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import de.hackatum2018.sixtcarpool.R;


public class DashboardFragment extends Fragment {

    WebView webView;
    Member driver = new Member("Sixt_Dashboards&#47;Driver", "https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;Si&#47;Sixt_Dashboards&#47;Driver&#47;1.png", 1200, 1027);
    Member passenger = new Member("Sixt_Dashboards&#47;User", "https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;Si&#47;Sixt_Dashboards&#47;User&#47;1.png", 650, 887);

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
        webView.getSettings().setDomStorageEnabled(true);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        String s = "<html><body style='margin:0;padding:0;'><div class='tableauPlaceholder' id='dashboard'>" +
                "<noscript><img alt=' ' src='https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;33&#47;33SF8BXFP&#47;1_rss.png'" +
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
                "scriptElement.src = 'https://public.tableau.com/javascripts/api/viz_v1.js';" +
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
