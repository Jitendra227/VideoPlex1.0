package com.jitendra.videoplex10.Network;

import com.jitendra.videoplex10.Config.YoutubeConfig;
import com.jitendra.videoplex10.Model.YoutubeModel.PopularResponse;
import com.jitendra.videoplex10.Model.YoutubeModel.YtMediaFiles;
import com.jitendra.videoplex10.Model.YoutubeSearchModel.SearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiCallInterface {
    //public static final String POPULAR_VID = "videos?part=snippet%2CcontentDetails%2Cstatistics&chart=mostPopular&regionCode=US&key=AIzaSyDdcgZZhCL09EkHNh3JmPAsocL_CePOCCQ";

    public static final String KEY ="";


    @GET("videos?part=snippet%2CcontentDetails%2Cstatistics&chart=mostPopular&maxResults=35&regionCode=US&key=AIzaSyDdcgZZhCL09EkHNh3JmPAsocL_CePOCCQ")
    Call<PopularResponse> getAllPopularVideos();

    @GET
    Call<SearchResponse> getSearchedVideos(@Url String url);
}
