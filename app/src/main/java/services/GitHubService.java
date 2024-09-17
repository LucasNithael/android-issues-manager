package services;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import models.Repositorio;

public class GitHubService {

    private static final String GITHUB_TOKEN = ""; // Use variáveis de ambiente ou secure storage
    private static final String BASE_URL = "https://api.github.com/users/lucasnithael";
    private static final String REPO_URL = "https://api.github.com/repos/lucasnithael/";

    public interface RepositoryCallback {
        void onSuccess(List<Repositorio> repositorios);
        void onError(String error);
    }

    public interface IssuesCallback {
        void onSuccess(List<String> issues);
        void onError(String error);
    }

    public interface IssueCreateCallback {
        void onSuccess(String issueUrl);
        void onError(String error);
    }

    public void obterRepositorios(RepositoryCallback callback) {
        new AsyncTask<Void, Void, List<Repositorio>>() {
            @Override
            protected List<Repositorio> doInBackground(Void... voids) {
                List<Repositorio> repositorios = new ArrayList<>();
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(BASE_URL + "/repos");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
                    conn.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);

                    Log.d("GitHubService", "Enviando requisição para: " + url.toString());

                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        throw new RuntimeException("Erro: HTTP código " + conn.getResponseCode());
                    }

                    // Lendo a resposta da API
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        output.append(line);
                    }

                    // Convertendo a resposta para JSON
                    JSONArray jsonArray = new JSONArray(output.toString());

                    // Iterando sobre os repositórios
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject repoJson = jsonArray.getJSONObject(i);
                        String nome = repoJson.getString("name");
                        String descricao = repoJson.isNull("description") ? "Sem descrição" : repoJson.getString("description");

                        // Adicionando o repositório à lista
                        repositorios.add(new Repositorio(nome, descricao));
                    }
                } catch (Exception e) {
                    Log.e("GitHubService", "Erro ao obter repositórios", e);
                    return null;
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
                return repositorios;
            }

            @Override
            protected void onPostExecute(List<Repositorio> repositorios) {
                if (repositorios != null) {
                    callback.onSuccess(repositorios);
                } else {
                    callback.onError("Erro ao obter repositórios");
                }
            }
        }.execute();
    }

    public void obterIssues(String repoName, IssuesCallback callback) {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                List<String> issues = new ArrayList<>();
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(REPO_URL + repoName + "/issues");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
                    conn.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);

                    Log.d("GitHubService", "Enviando requisição para: " + url.toString());

                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        throw new RuntimeException("Erro: HTTP código " + conn.getResponseCode());
                    }

                    // Lendo a resposta da API
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        output.append(line);
                    }

                    // Convertendo a resposta para JSON
                    JSONArray jsonArray = new JSONArray(output.toString());

                    // Iterando sobre as issues
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject issueJson = jsonArray.getJSONObject(i);
                        String titulo = issueJson.getString("title");

                        // Adicionando a issue à lista
                        issues.add(titulo);
                    }
                } catch (Exception e) {
                    Log.e("GitHubService", "Erro ao obter issues", e);
                    return null;
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
                return issues;
            }

            @Override
            protected void onPostExecute(List<String> issues) {
                if (issues != null) {
                    callback.onSuccess(issues);
                } else {
                    callback.onError("Erro ao obter issues");
                }
            }
        }.execute();
    }

    public void criarIssue(String repoName, String titulo, String descricao, IssueCreateCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(REPO_URL + repoName + "/issues");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
                    conn.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);

                    // Criando o JSON para o corpo da issue
                    JSONObject issueJson = new JSONObject();
                    issueJson.put("title", titulo);
                    issueJson.put("body", descricao);

                    // Enviando o corpo da requisição
                    OutputStream os = conn.getOutputStream();
                    os.write(issueJson.toString().getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    Log.d("GitHubService", "Enviando requisição para: " + url.toString());

                    if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                        throw new RuntimeException("Erro: HTTP código " + conn.getResponseCode());
                    }

                    // Lendo a resposta da API
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        output.append(line);
                    }

                    // Convertendo a resposta para JSON
                    JSONObject responseJson = new JSONObject(output.toString());
                    return responseJson.getString("html_url"); // URL da issue criada

                } catch (Exception e) {
                    Log.e("GitHubService", "Erro ao criar issue", e);
                    return null;
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }

            @Override
            protected void onPostExecute(String issueUrl) {
                if (issueUrl != null) {
                    callback.onSuccess(issueUrl);
                } else {
                    callback.onError("Erro ao criar issue");
                }
            }
        }.execute();
    }
}
