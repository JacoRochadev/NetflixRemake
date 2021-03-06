package com.jacorocha.netflixremake.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaCodec;
import android.os.AsyncTask;
import android.util.Log;

import com.jacorocha.netflixremake.model.Category;
import com.jacorocha.netflixremake.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class CategoryTask extends AsyncTask<String, Void, List<Category>> {
    private final WeakReference<Context> context;
    private ProgressDialog dialog;
    private CategoryLoader categoryLoader;

    public CategoryTask(Context context){
        this.context = new WeakReference<>(context);
    }
    public void setCategoryLoader(CategoryLoader categoryLoader){
        this.categoryLoader = categoryLoader;

    }

    //main thread
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();
        if(context != null)
            dialog = ProgressDialog.show(context, "Carregando", "", true);
    }

    //thread background
    @Override
    protected List<Category> doInBackground(String... strings) {
        String url = strings[0];
        try {
            URL requestUrl = new URL(url);
            HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(2000);

            int responseCode = urlConnection.getResponseCode();
            if(responseCode > 400){
                throw new IOException("Error na comunicação com o servidor");
            }
            InputStream inputStream = urlConnection.getInputStream();

            BufferedInputStream in =  new BufferedInputStream(urlConnection.getInputStream());
            
            String jsonAsString = toString(in);
            Log.i("Teste", jsonAsString);

            List<Category> categories = getCategories(new JSONObject(jsonAsString));
            in.close();
            return categories;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Category> getCategories(JSONObject jsonObject) throws JSONException {
        List<Category> categories = new ArrayList<>();
        JSONArray category = jsonObject.getJSONArray("category");
        for (int i = 0; i < category.length(); i++) {
            JSONObject categoryObject = category.getJSONObject(i);
            String title = categoryObject.getString("title");

            List<Movie> movies = new ArrayList<>();
            JSONArray movieArray = categoryObject.getJSONArray("movie");
            for (int j = 0; j < movieArray.length(); j++) {
                JSONObject movieObject = movieArray.getJSONObject(j);
                String cover_url = movieObject.getString("cover_url");

                Movie movieObj =new Movie();
                movieObj.setCoverUrl(cover_url);
                movies.add(movieObj);
            }
            Category categoryObj = new Category();
            categoryObj.setName(title);
            categoryObj.setMovies(movies);

            categories.add(categoryObj);
        }
        return categories;
    }

    private String toString(InputStream in) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = in.read(bytes)) > 0){
            baos.write(bytes,0,lidos);
        }
        return new String(baos.toByteArray());
    }

    //main thread
    @Override
    protected void onPostExecute(List<Category> categories) {
        super.onPostExecute(categories);
        dialog.dismiss();

        //listener
        if (categoryLoader != null) {
            categoryLoader.onResult(categories);

        }

    }

    public interface CategoryLoader{
        void onResult(List<Category> categories);
    }
}
