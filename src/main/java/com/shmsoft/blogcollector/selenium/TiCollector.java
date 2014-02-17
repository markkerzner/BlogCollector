package com.shmsoft.blogcollector.selenium;

import com.google.common.annotations.VisibleForTesting;
import com.shmsoft.blogcollector.Settings;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Automate file downloads from Talmud Illuminated, http://mkerzner.blogspot.com/ Although some
 * settings are specific to this blog, you can clone this class and adjust it to other situations.
 *
 * @author mark
 */
public class TiCollector implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TiCollector.class);
    private int totalCount;
    private int perTagCount;
    private boolean stop = false;

    public TiCollector() {
        logger.debug("Initiated TiCollector instance");
    }

    @Override
    public void run() {
        Date startDate = new Date();
        boolean firstDownload = true;
        // The Firefox driver supports javascript 
        WebDriver driver = new FirefoxDriver();
//        when you need to execute Javascript - do as below        
//        if (driver instanceof JavascriptExecutor) {
//            ((JavascriptExecutor) driver).executeScript("your-java-script");
//        }

        String[] tags = Settings.getSettings().getTags();
        logger.info("Ready to search for {} tages", tags.length);
        Settings settings = Settings.getSettings();
        totalCount = 0;
        for (String tag : tags) {
            try {
                perTagCount = 0;
                logger.info("Downloading for tag {}", tag);
                driver.get("http://mkerzner.blogspot.com/search/label/" + tag);
                // select one ticker
                logger.debug("select ticker {}", tag);
                WebElement query = driver.findElement(By.name(
                        "_criteria$_searchSection$_searchToggle$_criteria__searchSection__searchToggle__entitySearch_searchbox"));
                String html = driver.getPageSource();
                List<String> ids = findPosts(html);
                logger.debug("Preparing to download {} documents for the ticker {}", ids.size(), tag);
                // TODO here we should have a loop over pages
                for (String id : ids) {
                    logger.debug("Downloading for {}", id);
                    try {
                        if (isStop()) {
                            break;
                        }
                        // find the first PDF to download
                        query = driver.findElement(By.id(id));
                        // request it
                        query.click();
                        // confirm download - first time done by operator, later automatic     
                        if (firstDownload) {
                            firstDownload = false;
                            wait(20);
                        }
                        ++perTagCount;
                        if (settings.getPerTagLimit() > 0
                                && perTagCount >= settings.getPerTagLimit()) {
                            break;
                        }
                        // TODO make sure we don't break off the last download
                    } catch (Exception e) {
                        logger.warn("Problem download with id {}, going to the next one ", id, e);
                    }
                }
                if (isStop()) {
                    break;
                }
                // next page
                // if we have Page 1, 2, 3..., go to them
                ((JavascriptExecutor) driver).executeScript("javascript:__doPostBack('_transcriptsGrid$_dataGrid','Page$2')");
                // if not, go the ">" next page

                // Here we should end loop over the page and go to the next page
                // go to the next page for this ticker
                // TODO what happens when there is not "next" arrow?
                //((JavascriptExecutor) driver).executeScript("javascript:__doPostBack('_transcriptsGrid$_dataGrid','Page$Next')");
                // Alternatively, show all
                //((JavascriptExecutor) driver).executeScript("javascript:__doPostBack('_transcriptsGrid$_dataGrid','Page$All')");
                ++totalCount;
                if (settings.getTotalLimit() > 0 && totalCount >= settings.getTotalLimit()) {
                    break;
                }
                moveDownloadedFiles(tag);
            } catch (Exception e) {
                logger.warn("Problem with ticker {}, going to the next one", tag, e);
            }
        }
        long duration = (new Date().getTime() - startDate.getTime()) / 1000;
        logger.info("Duration in seconds: {}", duration);
    }

    public static void main(String[] args) throws Exception {
        TiCollector instance = new TiCollector();
        Settings settings = Settings.getSettings();
        String[] tags = {"Sukkah", "NasdaqGS:GOOG", "NasdaqGS:MSFT"};
        settings.setTags(tags);
        settings.setTotalLimit(10);
        settings.setPerTagLimit(3);
        instance.run();
        System.exit(0);
    }

    /**
     * Find all posts on the html page for blogpost
     *
     * @param html page source
     * @return all posts
     */
    @VisibleForTesting
    List<String> findPosts(String html) {
        List<String> posts = new ArrayList<>();
        String startMarker = "<div class='post-body entry-content'";
        String stopMarker = "<div class='post-footer'>";
        int start = 0;
        while (true) {
            start = html.indexOf(startMarker, start);
            if (start < 0) {
                break;
            }
            int end = html.indexOf(stopMarker, start);
            posts.add(html.substring(start + 1, end));
            start = end + 1;
        }
        return posts;
    }

    /**
     * @return the stop
     */
    synchronized public boolean isStop() {
        return stop;
    }

    /**
     * @param stop the stop to set
     */
    synchronized public void setStop(boolean stop) {
        this.stop = stop;
    }

    private void moveDownloadedFiles(String tag) {
//        Settings settings = Settings.getSettings();
//        String moveTo = settings.getMyDownloadDir() + File.separator + getTagDir(tag);
//        try {
//            File moveToFile = new File(moveTo);
//            moveToFile.mkdirs();
//            FileUtils.cleanDirectory(new File(moveTo));
//            for (File file : files) {
//                FileUtils.moveFileToDirectory(file, new File(moveTo), true);
//            }
//        } catch (IOException e) {
//            logger.error("Problem moving downloaded files", e);
//        }
    }

    @VisibleForTesting
    String getTagDir(String tag) {
        String[] parts = tag.split(":");
        return parts[1];
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            // nothing there
        }
    }
}
