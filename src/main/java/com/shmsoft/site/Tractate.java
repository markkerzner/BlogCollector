package com.shmsoft.site;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark
 */
public class Tractate {

    private String name;
    private final List <Page> pages = new ArrayList<>();

    public Tractate(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the pages
     */
    public List <Page> getPages() {
        return pages;
    }
    public void addPage(Page page) {
        pages.add(page);
    }
}
