package com.ajit.bjp.network;

import com.ajit.bjp.model.Example;
import com.ajit.bjp.model.corona.ExampleCorona;
import com.ajit.bjp.model.karyakarta.ExampleKaryakarta;
import com.ajit.bjp.model.ExampleVillage;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("/feeds/list/1SsblfbSqDKUYjz5AsI1Gcy5vBOce0T9v3QxBYpAmekg/1/public/values")
    Flowable<Example> getDirectoryData(@Query("alt") String queryString);

    @GET("/feeds/list/1SsblfbSqDKUYjz5AsI1Gcy5vBOce0T9v3QxBYpAmekg/2/public/values")
    Flowable<ExampleVillage> getVillageData(@Query("alt") String queryString);

    @GET("/feeds/cells/110lSXdNQcl8lt6prlFfHjK1EXbXsc9RRvcSrOQqZVHs/1/public/values")
    Flowable<ExampleKaryakarta> getKaryakartaData(@Query("alt") String queryString);

    @GET("/feeds/cells/1T_JRht6REqnf9cxHYE9uGTfBj2-5FphDH-YPlxaB8CM/1/public/values")
    Flowable<ExampleCorona> getCoronaComplaints(@Query("alt") String queryString);
}
