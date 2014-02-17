package com.shmsoft.blogcollector;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collect all download settings throughout the run.
 *
 * @author mark
 */
public class Settings {

    public static final String VERSION = "1.0.1";
    private String site;
    private String[] sites = {"mkerzner.blogspot.com"};
    private int totalLimit;
    private int perTagLimit;
    private String[] tags;
    private int[] selectedTags = new int[0];
    private String myDownloadDir = "ti-downloads";
    private static final Settings settings = new Settings();
    private final static Logger logger = LoggerFactory.getLogger(Settings.class);

    private Settings() {
        // singleton
        tags = new String[1];
        try {
            tags = FileUtils.readLines(new File("conf/talmud-tags.txt")).toArray((new String[0]));            
        } catch (IOException e) {
            logger.error("Problem reading the tags", e);
        }
    }

    public static Settings getSettings() {
        return settings;
    }

    /**
     * @return the site
     */
    public String getSite() {
        return site;
    }

    /**
     * @param site the site to set
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * @return the totalLimit
     */
    public int getTotalLimit() {
        return totalLimit;
    }

    /**
     * @param totalLimit the totalLimit to set
     */
    public void setTotalLimit(int totalLimit) {
        this.totalLimit = totalLimit;
    }

    /**
     * @return the tickers
     */
    public String[] getTags() {
        return tags;
    }

    /**
     * @param tickers the tickers to set
     */
    public void setTags(String[] tickers) {
        this.tags = tickers;
    }

    /**
     * @return the myDownloadDir
     */
    public String getMyDownloadDir() {
        return myDownloadDir;
    }

    /**
     * @param myDownloadDir the myDownloadDir to set
     */
    public void setMyDownloadDir(String myDownloadDir) {
        this.myDownloadDir = myDownloadDir;
    }

    /**
     * @return the perTagLimit
     */
    public int getPerTagLimit() {
        return perTagLimit;
    }

    /**
     * @param perTagLimit the perTagLimit to set
     */
    public void setPerTagLimit(int perTagLimit) {
        this.perTagLimit = perTagLimit;
    }

    /**
     * @return the sites
     */
    public String[] getSites() {
        return sites;
    }

    /**
     * @return the selectedTags
     */
    public int[] getSelectedTags() {
        return selectedTags;
    }

    /**
     * @param selectedTags the selectedTags to set
     */
    public void setSelectedTags(int[] selectedTags) {
        this.selectedTags = selectedTags;
    }

    private static enum OS {

        LINUX, WINDOWS, MACOSX, UNKNOWN
    };

    /**
     * Determine the underlying OS.
     *
     * @return OS on which we are running
     */
    static OS getOs() {
        String platform = System.getProperty("os.name").toLowerCase();
        if (platform.startsWith("windows")) {
            return OS.WINDOWS;
        } else if (platform.startsWith("linux")) {
            return OS.LINUX;
        } else if (platform.startsWith("mac os x")) {
            return OS.MACOSX;
        } else {
            return OS.UNKNOWN;
        }
    }
}
