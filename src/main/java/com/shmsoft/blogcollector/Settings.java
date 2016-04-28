package com.shmsoft.blogcollector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collect all download settings throughout the run.
 *
 * @author mark
 */
public class Settings {

    public static final String VERSION = "2.0.1";
    private String source;

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }
    public static final String BLOG = "blog";
    public static final String BLOG_LOCAL = "blog_local";
    public static final String SITE_LOCAL = "site_local";
    private String[] tags;
    private int[] selectedTags;
    private String myDownloadDir = "ti-downloads";
    private String site = "site";
    private static final Settings settings = new Settings();
    private final static Logger logger = LoggerFactory.getLogger(Settings.class);

    private Settings() {
        // singleton
        tags = new String[1];
        try {
            tags = FileUtils.readLines(new File("conf/talmud-tags.txt")).toArray((new String[0]));            
            selectedTags = new int[1];
            selectedTags[0] = 0;
        } catch (IOException e) {
            logger.error("Problem reading the tags", e);
        }
    }

    public static Settings getSettings() {
        return settings;
    }


    /**
     * @return the tags
     */
    public String[] getTags() {
        return tags;
    }

    /**
     * @return the selected tags
     */
    public String[] getSelectedTagsByName() {
        List <String> sTags = new ArrayList<>();
        for (int i = 0; i < selectedTags.length; ++i) {
            sTags.add(tags[selectedTags[i]]);
        }
        return sTags.toArray(new String[0]);
    }
    
    /**
     * @param tags the tags to set
     */
    public void setTags(String[] tags) {
        this.tags = tags;
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
    public String getSite() {
        return site;
    }
}
