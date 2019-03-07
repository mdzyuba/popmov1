package com.mdzyuba.popularmovies.service;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NetworkDataProvider {

    private static final String DELIMITER = "\\A";

    @Nullable
    public String getResponseFromHttpUrl(URL url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = HttpClientProvider.getClient();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            try (InputStream in = body.byteStream())  {
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter(DELIMITER);

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            }
        }
    }

}
