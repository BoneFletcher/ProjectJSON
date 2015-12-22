package denis.frost.myjsonproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "my_log";
    TextView tvTitleNoFormatting;
    TextView tvPublisher;
    WebView wvMyWebCluster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitleNoFormatting = (TextView) findViewById(R.id.tv_titlenoformatting);
        tvPublisher = (TextView) findViewById(R.id.tv_publisher);

        wvMyWebCluster = (WebView) findViewById(R.id.wv_clusterurl);
        initData();

    }
    private void initData() {
        MovieFetcherAsync task = new MovieFetcherAsync();
        task.execute("https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=ukraine&rsz=1");
    }
    public class MovieFetcherAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String json = null;

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null)
                    return null;
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null)
                    buffer.append(line + "\n");
                if (buffer.length() == 0)
                    return null;
                json = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping to parse it.
                return null;
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null)
                return;

            try {
                JSONObject json = new JSONObject(result);
                JSONObject json1 = json.getJSONObject("responseData");
                JSONArray jsonArray = json1.getJSONArray("results");
                for (int i =0; i <jsonArray.length(); i++) {
                    JSONObject json2 = jsonArray.getJSONObject(0);
                    NewsItem item =  NewsItem.getItemFromJson(json2);
                     //new NewsItem();
                    if (!TextUtils.isEmpty(item.title))
                        tvTitleNoFormatting.setText(item.title);
                    if (!TextUtils.isEmpty(item.publisher))
                        tvPublisher.setText(item.publisher);
                    if (!TextUtils.isEmpty(item.title))
                        wvMyWebCluster.loadUrl(item.clusterUrl);
//                    item.setTitle(json2.getString("titleNoFormatting"));
//                    item.setPublisher(json2.getString("publisher"));
//                    item.setClusterUrl(json2.getString("clusterUrl"));
//                    news.add(item);
//                    tvTitleNoFormatting.setText(item.getTitle());
//                    tvPublisher.setText(item.getPublisher());
//                    wvMyWebCluster.loadUrl(item.getClusterUrl());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(LOG_TAG, result);
        }
    }
}
