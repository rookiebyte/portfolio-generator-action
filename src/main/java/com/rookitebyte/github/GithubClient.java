package com.rookitebyte.github;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rookitebyte.github.exception.GithubClientException;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class GithubClient {

    private static final String GITHUB_URI = "api.github.com";

    HttpClient httpClient;

    private GithubClient() {
        httpClient = HttpClientBuilder.create().build();
    }

    public static GithubClient create() {
        return new GithubClient();
    }

    public List<Repository> fetchUsersRepositories(String userName) {

        var request = ClassicRequestBuilder.get().setUri(usersRepositoriesUri(userName)).build();
        try {
            return List.of(executeRequest(request));
        } catch (IOException ex) {
            throw new GithubClientException(ex);
        }
    }

    private InputStream fetchReadme(Repository repository) {
        var request = ClassicRequestBuilder.get().setUri(repository.buildLinkToReadme()).build();
        try {
            return List.of(executeRequest(request));
        } catch (IOException ex) {
            throw new GithubClientException(ex);
        }
    }

    private URI usersRepositoriesUri(String userName) {
        try {
            return new URIBuilder()
                    .setScheme("https")
                    .setHost(GITHUB_URI)
                    .appendPath("users")
                    .appendPath(userName)
                    .appendPath("repos")
                    .build();
        } catch (URISyntaxException ex) {
            throw new GithubClientException(ex);
        }
    }

    private <T> T executeRequest(ClassicHttpRequest request, T... reified) throws IOException {
        var tClass = getClassOf(reified);
        return httpClient.execute(request, new JsonResponseHandler<>(tClass));
    }

    static <T> Class<T> getClassOf(T[] array) {
        return (Class<T>) array.getClass().getComponentType();
    }

    private static class JsonResponseHandler<T> implements HttpClientResponseHandler<T> {

        private final Class<T> tClass;
        private final ObjectMapper objectMapper;


        public JsonResponseHandler(Class<T> tClass) {
            this.tClass = tClass;
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        @Override
        public T handleResponse(ClassicHttpResponse classicHttpResponse) throws HttpException, IOException {
            var content = classicHttpResponse.getEntity().getContent();
            return objectMapper.readValue(content, tClass);
        }
    }
}
