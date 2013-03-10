package com.khloke.PlayToXbmcAndroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import com.khloke.PlayToXbmcAndroid.fragments.NewClientFragment;
import com.khloke.PlayToXbmcAndroid.objects.XbmcClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ChooseClientActivity extends FragmentActivity {

    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_list);

        final Intent intent = getIntent();
        final String action = intent.getAction();
        final String type = intent.getType();

        final ArrayList<XbmcClient> xbmcClients = XbmcClient.loadAll(this);
        ListView clientListView = (ListView) findViewById(R.id.clientListView);
        clientListView.setAdapter(getXbmcClientViewAdapter(xbmcClients));

        clientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XbmcClient xbmcClient = xbmcClients.get(position);
                if (Intent.ACTION_SEND.equals(action) && type != null) {
                    if ("text/plain".equals(type)) {
                        playToXbmc(intent.getClipData().getItemAt(0).getText().toString(), xbmcClient);
                    }
                } else {
                    NewClientFragment newClientFragment = new NewClientFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", xbmcClient.getId());
                    newClientFragment.setArguments(bundle);
                    newClientFragment.show(getSupportFragmentManager(), "editClient");
                }
            }
        });

        clientListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                XbmcClient xbmcClient = xbmcClients.get(position);
                XbmcClient.delete(parent.getContext(), String.valueOf(xbmcClient.getId()));

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final ArrayList<XbmcClient> xbmcClients = XbmcClient.loadAll(this);
        ListView clientListView = (ListView) findViewById(R.id.clientListView);
        clientListView.setAdapter(getXbmcClientViewAdapter(xbmcClients));
    }

    private ArrayAdapter<XbmcClient> getXbmcClientViewAdapter(final ArrayList<XbmcClient> aXbmcClients) {
        return new ArrayAdapter<XbmcClient>(this, android.R.layout.simple_list_item_1, aXbmcClients) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView nameView = new TextView(ChooseClientActivity.this);

                XbmcClient item = getItem(position);
                nameView.setHeight(80);
                nameView.setText(item.getName());
                nameView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
                nameView.setGravity(Gravity.CENTER_VERTICAL);
                nameView.setPadding(30, 0, 30, 0);

                return nameView;
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_client_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addClientAction:
                new NewClientFragment().show(getSupportFragmentManager(), "addClient");
                return true;

            default:
                return false;
        }
    }

    private void playToXbmc(String aUrl, XbmcClient aClient) {

        String videoId = getUrlParams(aUrl).get("v");
        String pluginCommand = "plugin://plugin.video.youtube/?action=play_video&videoid=" + videoId;

        String jsonAddRequest = "{\"jsonrpc\": \"2.0\", \"method\": \"Playlist.Add\", \"params\":{\"playlistid\":1, \"item\" :{ \"file\" : \"" + pluginCommand + "\" }}, \"id\" : 1}";
        String jsonPlayRequest = "{\"jsonrpc\": \"2.0\", \"method\": \"Player.Open\", \"params\":{\"item\":{\"playlistid\":1, \"position\" : 0}}, \"id\": 1}";

        post(aClient.getAddress(), aClient.getPort(), jsonAddRequest);
        post(aClient.getAddress(), aClient.getPort(), jsonPlayRequest);

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
//                    Toast.makeText(this, "Malformed URL detected.", Toast.LENGTH_LONG);
                } catch (IOException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                return null;
            }
        };

        asyncTask.execute(aUrl, aPort, aContent);
    }
}
