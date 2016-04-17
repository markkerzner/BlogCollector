package com.shmsoft.site;

/**
 *
 * @author mark
 */
public class Site {
    private static final Site site = new Site();
    private Site () {
        // Singleton
    };
    static public Site site() {
        return site;
    }
    
}
