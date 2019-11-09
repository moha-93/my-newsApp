package com.moha.nytimesapp.rest;




import com.moha.nytimesapp.modal.Response;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebServiceAPI {

    @GET("svc/mostpopular/v2/emailed/{period}.json")
    Observable<Response> getEmailedArticles(@Path("period")Integer period,
                              @Query("api-key") String key);

    @GET("svc/mostpopular/v2/shared/{period}/{share_type}.json")
    Observable<Response> getSharedArticles(@Path("period") Integer period,
                             @Path("share_type")String shareType,
                             @Query("api-key") String key);

    @GET("svc/mostpopular/v2/viewed/{period}.json")
    Observable<Response> getViewedArticles(@Path("period") Integer period,
                                           @Query("api-key") String key);



}
