package de.hackatum2018.sixtcarpool.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import de.hackatum2018.sixtcarpool.R;


public class DashboardFragment extends Fragment {

    WebView webView;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        webView = view.findViewById(R.id.dashboard);
        webView.getSettings().setJavaScriptEnabled(true);
        String s = "<html><body style='margin:0;padding:0;'><div class='tableauPlaceholder' id='viz1542464115990'>" +
                "<noscript>" +
                "                    <img alt=' ' src='https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;33&#47;33SF8BXFP&#47;1_rss.png'" +
                "                        style='border: none' /></noscript>" +
                "            <object class='tableauViz' style='display:none;'>" +
                "                <param name='host_url' value='https%3A%2F%2Fpublic.tableau.com%2F' />" +
                "                <param name='embed_code_version' value='3' />" +
                "                <param name='path' value='shared&#47;33SF8BXFP' />" +
                "                <param name='toolbar' value='yes' />" +
                "                <param name='static_image' value='https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;33&#47;33SF8BXFP&#47;1.png' />" +
                "                <param name='animate_transition' value='yes' />" +
                "                <param name='display_static_image' value='yes' />" +
                "                <param name='display_spinner' value='yes' />" +
                "                <param name='display_overlay' value='yes' />" +
                "                <param name='display_count' value='yes' />" +
                "            </object>" +
                "        </div>" +
                "<script type='text/javascript'>\n" +
                "            var divElement = document.getElementById('viz1542464115990');\n" +
                "            var vizElement = divElement.getElementsByTagName('object')[0];\n" +
                "            vizElement.style.width = '1000px';\n" +
                "            vizElement.style.height = '827px';\n" +
                "            var scriptElement = document.createElement('script');\n" +
                "            scriptElement.src = 'https://public.tableau.com/javascripts/api/viz_v1.js';\n" +
                "            vizElement.parentNode.insertBefore(scriptElement, vizElement);\n" +
                "        </script></body></html>";
        //String encodedHtml = Base64.encodeToString(s.getBytes(), Base64.NO_PADDING);
        webView.loadData(s, "text/html", null);
        return view;
    }

}
