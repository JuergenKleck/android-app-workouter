package com.juergenkleck.android.app.workouter.datamodel;

import java.io.Serializable;
import java.util.Objects;

/**
 * Android app - Workouter
 *
 * Copyright 2022 by Juergen Kleck <develop@juergenkleck.com>
 */
public class BaseItem implements Serializable {

    public final static String TAG_ID = "id";

    public Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseItem baseItem = (BaseItem) o;
        return Objects.equals(id, baseItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
