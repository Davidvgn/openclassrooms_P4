package com.example.mareu.data;

import androidx.annotation.StringRes;

import com.example.mareu.R;

public enum Room {
    JAVA(R.string.java),
    KOTLIN,
    DART,
    PYTHON,
    ANDROID,
    SWIFT,
    TEAMS,
    OS,
    ITUNES,
    STUDIO;

    public final int stringRes;

    Room(@StringRes int stringRes) {
        this.stringRes = stringRes;
    }
}
