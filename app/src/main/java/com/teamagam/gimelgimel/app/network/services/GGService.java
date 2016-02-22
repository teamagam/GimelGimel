package com.teamagam.gimelgimel.app.network.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.teamagam.gimelgimel.app.model.entities.FriendsEntity;
import com.teamagam.gimelgimel.app.model.rest.GGApi;

import java.util.Date;
import java.util.List;

import retrofit.RetrofitError;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class GGService extends WakefulIntentService {

    private static final String ACTION_GET_TIPS = "com.GG.app.control.services.action.ACTION_GET_TIPS";
    private static final String TAG = "GGService";
    private GGApi mGGApi;

    /**
     * Get an intent to start this service to perform action Get Tips with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static Intent actionGetTipsIntent(Context context) {
        Intent intent = new Intent(context, GGService.class);
        intent.setAction(ACTION_GET_TIPS);
        return intent;
    }

    public GGService() {
        super("GGService");
    }

    /***
     * Note: This method runs on the UI Thread
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // The following code creates a new Gson instance that will convert all fields from lower
        // case with underscores to camel case and vice versa. It also registers a type adapter for
        // the Date class. This DateTypeAdapter will be used anytime Gson encounters a Date field.
        // The gson instance is passed as a parameter to GsonConverter, which is a wrapper
        // class for converting types.
        // (Source: http://square.github.io/retrofit/)
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();

        // Creating the rest adapter (Retrofit)
        //todo: clean
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint(getString(R.string.GG_api_base_url))
//                .setConverter(new GsonConverter(gson))
//                .build();

//        mGGApi = restAdapter.create(GGApi.class);
    }

    @Override
    protected void doWakefulWork(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            try {
                if (ACTION_GET_TIPS.equals(action)) {
                    handleGetTips();
                }
            }
            catch (RetrofitError e){
                // TODO: Broadcast failures with their kind
                e.printStackTrace();
                switch (e.getKind()){
                    case NETWORK:
                        Log.e(TAG, "GGApi encountered a NETWORK error");
                        break;
                    case CONVERSION:
                        Log.e(TAG, "GGApi encountered a CONVERSION error");
                        break;
                    case HTTP:
                        Log.e(TAG, "GGApi encountered an HTTP error");
                        break;
                    case UNEXPECTED:
                        Log.e(TAG, "GGApi encountered an UNEXPECTED error");
                        break;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    /**
     * Handle get tips in the provided background thread
     */
    private void handleGetTips() {
        List<FriendsEntity> tipEntities = mGGApi.listFriends();

        ContentValues[] allContent = new ContentValues[tipEntities.size()];

        // Create content values for each entity
        int contentIndex = 0;
        for (FriendsEntity tip : tipEntities) {

            // Before inserting the new values from the API, we must take
            // locally saved values and set them so we will not override them.
            // (The ContentProvider is inserting with "CONFLICT_REPLACE" flag)
            tip.initWithPreviousValuesFromProvider(getContentResolver());
            
            ContentValues content = new ContentValues();
            content.put(FriendsEntity.DB.ID, tip.id);
            content.put(FriendsEntity.DB.CREATED_TIMESTAMP, tip.createdTimestamp);
            content.put(FriendsEntity.DB.TITLE, tip.title);
            content.put(FriendsEntity.DB.IS_FAVORITE, tip.isFavorite);

            // Add the content to the all of contents
            allContent[contentIndex] = content;
            contentIndex++;
        }

        // Store the entities in a database
        int inserted = getContentResolver().bulkInsert(FriendsEntity.CONTENT_URI, allContent);
        Log.d(TAG, String.format("Inserted %d tips", inserted));
    }
}