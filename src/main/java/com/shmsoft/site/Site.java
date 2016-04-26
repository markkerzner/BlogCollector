package com.shmsoft.site;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark
 */
public class Site {
    private List <Tractate> tractates;
    private static final Site site = new Site();
    private Site () {
        this.tractates = new ArrayList <>();
        // Singleton
    };
    static public Site site() {
        return site;
    }

    /**
     * @return the tractates
     */
    public List <Tractate> getTractates() {
        return tractates;
    }

    /**
     * @param tractates the tractates to set
     */
    public void setTractates(List <Tractate> tractates) {
        this.tractates = tractates;
    }
    public Tractate getTractate(String tractateName) {
        // find or create
        for (Tractate tractate: tractates) {
            if (tractate.getName().equals(tractateName)) {
                return tractate;
            }
        }
        Tractate tractate = new Tractate(tractateName);
        tractates.add(tractate);
        return tractate;
    }
    
}
