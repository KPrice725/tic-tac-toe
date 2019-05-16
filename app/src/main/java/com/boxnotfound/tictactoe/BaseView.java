package com.boxnotfound.tictactoe;

import androidx.annotation.NonNull;

/**
 * The required interface all View components must implement.
 */
public interface BaseView<T> {

    void setPresenter(@NonNull T presenter);
}