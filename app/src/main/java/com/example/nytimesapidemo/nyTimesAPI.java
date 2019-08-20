package com.example.nytimesapidemo;




import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface nyTimesAPI {

    @GET("svc/mostpopular/v2/emailed/{period}.json")
    Call<Response> getEmailed(@Path("period")Integer period,
                              @Query("api-key") String key);

    @GET("svc/mostpopular/v2/shared/{period}/{share_type}.json")
    Call<Response> getShared(@Path("period") Integer period,
                             @Path("share_type")String shareType,
                             @Query("api-key") String key);

    @GET("svc/mostpopular/v2/viewed/{period}.json")
    Call<Response> getViewed(@Path("period") Integer period,
                             @Query("api-key") String key);



}
