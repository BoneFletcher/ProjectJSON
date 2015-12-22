package denis.frost.myjsonproject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shakhov on 19.12.2015.
 */
public class NewsItem {
    private static final String KEY_JSON_TITLE = "titleNoFormatting";
    private static final String KEY_JSON_PUBLISHER = "publisher";
    private static final String KEY_JSON_CLUSTERURL = "clusterUrl";

    public String title;
    public String publisher;
    public String clusterUrl;
    public String getClusterUrl() {
        return clusterUrl;
    }

    public void setClusterUrl(String clusterUrl) {
        this.clusterUrl = clusterUrl;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public NewsItem() {
    }

    public static NewsItem getItemFromJson(JSONObject json) throws JSONException {
        NewsItem item = new NewsItem();
        item.title = json.getString(KEY_JSON_TITLE);
        item.publisher = json.getString(KEY_JSON_PUBLISHER);
        item.clusterUrl = json.getString(KEY_JSON_CLUSTERURL);
        return item;
    }
}
