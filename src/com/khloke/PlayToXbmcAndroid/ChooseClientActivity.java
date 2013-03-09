package com.khloke.PlayToXbmcAndroid;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ChooseClientActivity extends Activity {

    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                intent.getExtras();
//                playToXbmc(intent.getClipData().getItemAt(0).getText().toString());
            }
        }
    }

    private void playToXbmc(String aUrl) {

        String videoId = getUrlParams(aUrl).get("v");
        String pluginCommand = "plugin://plugin.video.youtube/?action=play_video&videoid=" + videoId;

        String jsonAddRequest = "{\"jsonrpc\": \"2.0\", \"method\": \"Playlist.Add\", \"params\":{\"playlistid\":1, \"item\" :{ \"file\" : \"" + pluginCommand + "\" }}, \"id\" : 1}";
        String jsonPlayRequest = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.Open\", \"params\":{\"item\":{\"playlistid\":1, \"position\" : 0}}, \"id\": 1}";

        post("192.168.171.137", "8085", jsonAddRequest);
        post("192.168.171.137", "8085", jsonPlayRequest);

        Toast.makeText(this, aUrl, Toast.LENGTH_SHORT).show();
    }

    private HashMap<String, String> getUrlParams(String aUrl) {

        HashMap<String, String> hashes = new HashMap<String, String>();
        String[] split = aUrl.substring(aUrl.indexOf("?") + 1).split("&");
        for (String s : split) {
            String[] keyAndValue = s.split("=");
            hashes.put(keyAndValue[0], keyAndValue[1]);
        }

        return hashes;
    }

    private void post(String aUrl, String aPort, String aContent) {
        AsyncTask<String, String, String> asyncTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String url = params[0];
                    String port = params[1];
                    String content = params[2];

                    HttpURLConnection urlConnection = (HttpURLConnection)new URL("http", url, Integer.valueOf(port), "/jsonrpc").openConnection();
                    urlConnection.addRequestProperty(CONTENT_LENGTH, String.valueOf(content.getBytes().length));
                    urlConnection.addRequestProperty(CONTENT_TYPE, CONTENT_TYPE_JSON);
                    urlConnection.setDoOutput(true);
                    urlConnection.getOutputStream().write(content.getBytes());
                    urlConnection.connect();
                    urlConnection.getContent();
                } catch (MalformedURLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                return null;
            }
        };

        asyncTask.execute(aUrl, aPort, aContent);
    }
}
