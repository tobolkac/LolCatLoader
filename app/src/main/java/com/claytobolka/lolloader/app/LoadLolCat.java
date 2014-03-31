package com.claytobolka.lolloader.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tobolkac on 3/31/14.
 *
 * Asynctask to load lolcat from flickr using the flickr api and update the
 * lolCat ImageView with the downloaded image, while displaying a progress dialog
 * to the user
 */
public class LoadLolCat extends AsyncTask<String, Integer, Bitmap> {
    private ProgressDialog pd;

    Random rand = new Random();
    private final WeakReference<ImageView> lolCatImageViewReference;
    private final WeakReference<Context> context;

    private String flickrURL = "https://www.flickr.com/services/rest/";
    private String flickrMethod = "flickr.photos.search";
    private String flickrAPI = "2bb3f833a0f9313282d182146da58194";
    private String flickrPrivacyFilter = "1";
    private String flickrSearchTags = "lolcat,lolcats,lolcatz";
    private String flickrContentType = "1";
    private String flickrSort = "relevance";

    public LoadLolCat(Context ctx, ImageView i) {
        lolCatImageViewReference = new WeakReference<ImageView>(i);
        context = new WeakReference<Context>(ctx);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context.get());
        pd.setMessage("Loading LoLCat...");
        pd.show();
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        String responseURL = String.format("%s?method=%s&format=rest&tags=%s&api_key=%s&privacy_filter=%s&content_type=%s&sort=%s",
                flickrURL, flickrMethod, flickrSearchTags, flickrAPI, flickrPrivacyFilter, flickrContentType, flickrSort);

        FlickrPhoto photo = getFlickrImages(responseURL);

        return getRandomFlickrPhoto(photo);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        pd.dismiss();
        lolCatImageViewReference.get().setImageBitmap(result);

    }

    /*
     * Using Using flickr api url passed in, get xml response and parse through creating
     * a list of FlickrPhoto objects conatining information about each photo returned, then
     * return the a random FlickrPhoto from the list FlickrPhoto objects
    */
    FlickrPhoto getFlickrImages(String responseURL) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;
        String response = null;

        HttpGet httpGet = new HttpGet(String.valueOf(responseURL));

        try {
            httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XmlPullParserFactory factory = null;
        XmlPullParser xpp = null;
        List<FlickrPhoto> photos = new ArrayList<FlickrPhoto>();
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            xpp.setInput(new StringReader(response));

            int event = xpp.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name = xpp.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        //put photo information into a object and add to list
                        if (name.equals("photo")) {
                            FlickrPhoto p = new FlickrPhoto();
                            p.setId(xpp.getAttributeValue(null, "id"));
                            p.setOwner(xpp.getAttributeValue(null, "owner"));
                            p.setSecret(xpp.getAttributeValue(null, "secret"));
                            p.setServer(xpp.getAttributeValue(null, "server"));
                            p.setFarm(xpp.getAttributeValue(null, "farm"));
                            p.setTitle(xpp.getAttributeValue(null, "title"));
                            photos.add(p);
                        }
                        break;
                    case XmlPullParser.END_TAG:

                        break;
                }
                event = xpp.next();
            }
        }
        catch(XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        FlickrPhoto pReturn = photos.get(rand.nextInt(photos.size()));

        return pReturn;
    }

    /*
     * Using the Photo object passed in retrive the image it corrisponds to
     * through the flickr api and return the Bitmap of the image
     */
    Bitmap getRandomFlickrPhoto(FlickrPhoto photo)
    {
        Bitmap image = null;

        String imageURL = String.format("http://farm%s.staticflickr.com/%s/%s_%s.jpg",
                photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret());

        try {
            InputStream in = new java.net.URL(imageURL).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        Log.d("image URL", imageURL);



    return image;
    }

}
