package com.example.newreaderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ListView lstnews;
    NewsAdapter newsAdapter;
    ArrayList<News> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstnews = (ListView)findViewById(R.id.listViewNews);
        arrayList = new ArrayList<>();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadData().execute("https://vnexpress.net/rss/tin-moi-nhat.rss");
            }

        });
        lstnews.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(MainActivity.this, DetailNews.class);
                intent.putExtra("link",arrayList.get(i).getLink());
                startActivity(intent);
            }
        });

    }
    private class  ReadData extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return docNoidung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);

            NodeList nodeList =document.getElementsByTagName("item");
            NodeList nodeListDecription = document.getElementsByTagName("description");
            String title ="";
            String link = "";
            String hinhAnh ="";
            for(int i =0; i<nodeList.getLength();i++){
                String cdata = nodeListDecription.item(i+1).getTextContent();

                Pattern pattern = Pattern.compile("<img[^>]+src\\s*=['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = pattern.matcher(cdata);

                if(matcher.find()){
                    hinhAnh = matcher.group(1);
                }
                Element element = (Element) nodeList.item(i);
                title = parser.getValue(element,"title");
                link = parser.getValue(element,"link");

                arrayList.add(new News(title,link,hinhAnh));
            }
            newsAdapter = new NewsAdapter(MainActivity.this,
                    android.R.layout.activity_list_item, arrayList);
            lstnews.setAdapter(newsAdapter);
            super.onPostExecute(s);
        }
    }
    private String docNoidung_Tu_URL(String theUrl){
        StringBuilder content = new StringBuilder();
        try {
            //create a url object
            URL url = new URL(theUrl);
            //create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            //read from urlconnection -> the bufferedReader
            while ((line=bufferedReader.readLine()) !=null){
                content.append(line+"\n");
            }
            bufferedReader.close();
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

}