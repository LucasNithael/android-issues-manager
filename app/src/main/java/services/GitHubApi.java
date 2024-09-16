package services;

import java.util.List;

import models.Repositorio;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface GitHubApi {
    @GET("users/{user}/repos")
    Call<List<Repositorio>> obterRepositorios(
            @Path("user") String user,
            @Header("Authorization") String token
    );
}
