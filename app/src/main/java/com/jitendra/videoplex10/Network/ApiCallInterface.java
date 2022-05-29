package com.jitendra.videoplex10.Network;

import com.jitendra.videoplex10.Config.YoutubeConfig;
import com.jitendra.videoplex10.Model.YtMediaFiles;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiCallInterface {
    //public static final String POPULAR_VID = "videos?part=snippet%2CcontentDetails%2Cstatistics&chart=mostPopular&regionCode=US&key=AIzaSyDdcgZZhCL09EkHNh3JmPAsocL_CePOCCQ";

    @GET("videos?part=snippet%2CcontentDetails%2Cstatistics&chart=mostPopular&regionCode=US&key=AIzaSyDdcgZZhCL09EkHNh3JmPAsocL_CePOCCQ")
    Call<List<YtMediaFiles>> getPopularVideos();

}
