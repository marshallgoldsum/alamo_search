package com.goldsum.austinplacesearch;

import com.goldsum.austinplacesearch.model.PlaceResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by marshallgoldsum on 2/27/18.
 * Singleton manager class for all of our network requests.
 */

public class PlacesNetworkManager {

    //Storing these keys here in a plain text format is very insecure, but out of scope of this
    //project to obfuscate
    private static final String CLIENT_ID = "KERDYQH4KX3CD3YT4324CZKSEVTNGKOTUDPCNRJRMQSG4CEX";
    private static final String CLIENT_SECRET = "33GEJISZWVHXJROXPWK2WMDEDNXHRB2Y3BDQTWN344DVXXWG";
    private static final String VERSION_DATE = "201802227";

    private static PlacesNetworkManager sPlacesNetworkManager;
    private Retrofit mRetrofit;
    private  PlacesAPI placesAPI;
    interface PlacesAPI {
        @GET("venues/search")
        Call<FoursquareResponse> searchVenues(@Query("client_id") String clientID,
                                                @Query("client_secret") String clientSecret,
                                                @Query("query") String query,
                                                @Query("ll") String latlng,
                                                @Query("v") String version);
    }

    public static PlacesNetworkManager getInstance(){
        if (sPlacesNetworkManager == null){
            sPlacesNetworkManager = new PlacesNetworkManager();
        }
        return sPlacesNetworkManager;
    }

    private PlacesNetworkManager()
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logInterceptor);
        httpClient.connectTimeout(15, TimeUnit.SECONDS);
        httpClient.retryOnConnectionFailure(true);
        httpClient.readTimeout(15, TimeUnit.SECONDS);
        httpClient.writeTimeout(15, TimeUnit.SECONDS);
        OkHttpClient okClient = httpClient.build();
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.foursquare.com/v2/")
                .client(okClient)
                .addConverterFactory(ProtoConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
//        mRetrofit.setLogLevel(RestAdapter.LogLevel.FULL);
        placesAPI = mRetrofit.create(PlacesAPI.class);
    }

    public void searchVenues(String query, Callback<FoursquareResponse> responseCallback)
    {
        placesAPI.searchVenues(CLIENT_ID, CLIENT_SECRET, query, "30.2672,-97.7431", VERSION_DATE).enqueue(responseCallback);
    }

    //POJO to handle the conversion from Foursquare's JSON
    public static class FoursquareResponse {
        PlaceResultsResponse response;
    }

    //POJO to handle the conversion from Foursquare's JSON
    public static class PlaceResultsResponse {
        // A group object within the response.
        List<PlaceResult> venues = new ArrayList<>();

        public List<PlaceResult> getPlaces() {
            return venues;
        }
    }
}
