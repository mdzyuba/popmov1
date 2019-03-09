package com.mdzyuba.popularmovies.view;

public enum MoviesSelection {
    MOST_POPULAR(0),
    TOP_RATED(1);

    private final int value;

    MoviesSelection(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MoviesSelection valueOf(int i) {
        switch (i) {
            case 0:
                return MOST_POPULAR;
            case 1:
                return TOP_RATED;
            default:
                return MOST_POPULAR;
        }
    }
}
