package com.mdzyuba.popularmovies.service;

import com.mdzyuba.popularmovies.model.Movie;

import org.hamcrest.Matchers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class TestDataUtils {

    private static final String POPULAR_MOVIES_RESPONSE_JSON =
            "popular_movies_response.json";
    private static final String DELIMITER = "\\A";

    private static final String[] POSTER_PATH_ITEMS = {"/xRWht48C2V8XNfzvPehyClOvDni.jpg",
            "/iiZZdoQBEYBv6id8su7ImL0oCbD.jpg", "/v3QyboWRoA4O9RbcsqH8tJMe8EB.jpg",
            "/7BsvSuDQuoqhWmU2fL7W2GOcZHU.jpg", "/svIDTNUoajS8dLEo7EosxvyAsgJ.jpg",
            "/xvx4Yhf0DVH8G4LzNISpMfFBDy2.jpg", "/hgWAcic93phg4DOuQ8NrsgQWiqu.jpg",
            "/wNJF8R5QE6nBT7DQoKk8t6YD1MM.jpg", "/lHu1wtNaczFPGFDTrjCSzeLPTKN.jpg",
            "/lvfIaThG5HA8THf76nghKinjjji.jpg", "/whtt9F8PFqvEgc4fDSHZPkitFk4.jpg",
            "/kQKcbJ9uYkTQql2R8L4jTUz7l90.jpg", "/f2PN2ff0VwcVUSgeJUx6pKHwp4r.jpg",
            "/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg", "/AiRfixFcfTkNbn2A73qVJPlpkUo.jpg",
            "/cwBq0onfmeilU5xgqNNjJAMPfpw.jpg", "/gLhYg9NIvIPKVRTtvzCWnp1qJWG.jpg",
            "/hXgmWPd1SuujRZ4QnKLzrj79PAw.jpg", "/wrFpXMNBRj2PBiN4Z5kix51XaIZ.jpg",
            "/b5RMzLAyq5QW6GtN9sIeAEMLlBI.jpg"};

    public String readPopularMoviesJsonResponse() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream =
                     classLoader.getResourceAsStream(POPULAR_MOVIES_RESPONSE_JSON)) {
            if (inputStream == null) {
                return null;
            }
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter(DELIMITER);

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
    }

    public void assertMoviesParsed(List<Movie> movies) {
        assertNotNull(movies);
        assertThat(movies.stream().map(m -> m.getPosterPath()).collect(Collectors.toList()),
                   Matchers.containsInAnyOrder(POSTER_PATH_ITEMS));
    }

}
