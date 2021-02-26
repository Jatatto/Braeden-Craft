package com.jakehonea.braedencraft.utils;

import java.util.List;

public class Scroller<T> {

    private final List<T> items;
    private final int maxPerPage;

    private int currentPage;

    public Scroller(List<T> items, int maxPerPage) {

        this.items      = items;
        this.maxPerPage = maxPerPage;

    }

    public void next() {

        if ((currentPage + 1) * maxPerPage < items.size())
            currentPage++;

    }

    public void back() {

        if (currentPage > 0)
            currentPage--;

    }

    public List<T> getItemsOnPage() {

        return items.subList(currentPage * maxPerPage, Math.min(items.size(), (currentPage + 1) * maxPerPage));

    }


}
