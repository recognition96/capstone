package com.otaliastudios.cameraview.filter;

import androidx.annotation.NonNull;

/**
 * Contains commonly used {@link Filter}s.
 * <p>
 * You can use {@link #newInstance()} to create a new instance and
 */
public enum Filters {

    /**
     * @see NoFilter
     */
    NONE(NoFilter.class);

    private Class<? extends Filter> filterClass;

    Filters(@NonNull Class<? extends Filter> filterClass) {
        this.filterClass = filterClass;
    }

    /**
     * Returns a new instance of the given filter.
     *
     * @return a new instance
     */
    @NonNull
    public Filter newInstance() {
        try {
            return filterClass.newInstance();
        } catch (IllegalAccessException e) {
            return new NoFilter();
        } catch (InstantiationException e) {
            return new NoFilter();
        }
    }
}
