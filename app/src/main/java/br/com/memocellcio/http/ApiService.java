package br.com.memocellcio.http;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/card/find")
    Call<List<Card>> listCards();

}
