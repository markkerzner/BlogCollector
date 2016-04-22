package com.shmsoft.blogcollector.selenium;

import com.google.common.annotations.VisibleForTesting;
import com.shmsoft.blogcollector.Settings;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Automate file downloads from Talmud Illuminated,
 * http://mkerzner.blogspot.com/ Although some settings are specific to this
 * blog, you can clone this class and adjust it to other situations.
 *
 * @author mark
 */
public class TiCollector implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TiCollector.class);
    private boolean stop = false;
    private List<String> titles;

    public TiCollector() {
        logger.debug("Initiated TiCollector instance");
    }

    @Override
    public void run() {
        Date startDate = new Date();
        String source = Settings.getSettings().getSource();
        switch (Settings.getSettings().getSource()) {
            case Settings.BLOG:
                downloadFromBlog();
                break;
            case Settings.BLOG_LOCAL:
                loadFromBlogLocal();
                break;
            case Settings.SITE_LOCAL:
                loadFromSiteLocal();
                break;
            default:
                logger.warn("Nothing to do - source not set");
        }
        long duration = (new Date().getTime() - startDate.getTime()) / 1000;
        logger.info("Duration in seconds: {}", duration);
    }

    private void loadFromBlogLocal() {

    }

    private void loadFromSiteLocal() {

    }

    private void downloadFromBlog() {
        WebDriver driver = new HtmlUnitDriver();

        String[] tags = Settings.getSettings().getSelectedTagsByName();
        logger.info("Ready to search for {} tages", tags.length);
        for (String tag : tags) {
            titles = new ArrayList<>();
            try {
                logger.info("Downloading for tag {}", tag);
                driver.get("http://mkerzner.blogspot.com/search/label/" + tag);
                boolean more = true;
                while (more) {
                    String html = driver.getPageSource();
                    html = sanitize(html);
                    List<String> posts = findPosts(html);
                    savePosts(tag, posts);
                    logger.debug("Downloading {} documents for the tag {}", posts.size(), tag);
                    more = goToNextPage(driver, html);
                }
                arrangeDownloadedFiles(tag);
            } catch (Exception e) {
                logger.warn("Problem with tag {}, going to the next one", tag, e);
            }
        }

    }

    private String sanitize(String html) {
        // TODO this may really be a hack, we should be able to to special characters, but for now, 
        // let's substitute them
        html = html.replaceAll("–", "-");
        html = html.replaceAll("“", "\"");
        html = html.replaceAll("”", "\"");
        // TODO did not work for some reason. I am telling you, sanitize all :)
        html = html.replaceAll("&nbsp;", " ");
        return html;
    }

    private boolean goToNextPage(WebDriver driver, String html) {
        WebElement query;
        try {
            query = driver.findElement(By.id("Blog1_blog-pager-older-link"));
        } catch (Exception e) {
            logger.info("No Older Posts");
            return false;
        }
        query.click();
        return true;
    }

    public static void main(String[] args) throws Exception {
        TiCollector instance = new TiCollector();
        Settings settings = Settings.getSettings();
        int[] selectedTags = new int[2];
        selectedTags[0] = 1;
        selectedTags[1] = 2;
        settings.setSelectedTags(selectedTags);
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
        String startMarker = "<h3 class=\"post-title entry-title\" itemprop=\"name\">";
        String stopMarker = "<div class=\"post-footer\">";
        int start = 0;
        while (true) {
            start = html.indexOf(startMarker, start);
            if (start < 0) {
                break;
            }
            int end = html.indexOf(stopMarker, start);
            if (end < 0) {
                break;
            }
            posts.add(html.substring(start, end));
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

    private void arrangeDownloadedFiles(String tag) {
        try {
            Collections.reverse(titles);
            FileUtils.write(new File(Settings.getSettings().getMyDownloadDir() + "/"
                    + tag.toLowerCase() + "/index.html"),
                    "<html>" + StringUtils.join(titles, "<br/>\n") + "</html>", true);
        } catch (IOException e) {
            logger.error("Could not arrange titles", e);
        }
    }

    @VisibleForTesting
    String getTagDir(String tag) {
        String[] parts = tag.split(":");
        return parts[1];
    }

    @VisibleForTesting
    void savePosts(String tag, List<String> posts) {
        try {
            for (String post : posts) {
                String title = getTitle(post).trim();
                titles.add(getTitleWithLink(title));
                FileUtils.write(new File(Settings.getSettings().getMyDownloadDir() + "/"
                        + tag.toLowerCase() + "/" + getHtmlFileName(title)), "<html>" + post + "</html>");
            }
        } catch (IOException e) {
            logger.error("Problem saving the post titles", e);
        }
    }

    private String getTitle(String post) {
        int start = post.indexOf('>');
        if (start < 0) {
            return "";
        }
        start = post.indexOf('>', start + 1);
        if (start < 0) {
            return "";
        }
        int end = post.indexOf('<', start + 1);
        return post.substring(start + 1, end);
    }

    private String getTitleWithLink(String title) {
        String[] words = title.split(" ");
        boolean longTitle = false;
        if (words.length >= 3) {
            try {
                Integer.parseInt(words[2]);
                longTitle = true;
            } catch (Exception e) {
                longTitle = false;
            }
        }
        StringBuilder builder;
        if (longTitle) {
            String link = words[0].toLowerCase() + words[1].toLowerCase() + words[2] + ".html";
            builder = new StringBuilder("<a href=\"" + link + "\">").
                    append(words[0]).append(" ").append(words[1]).append(" ").append(words[2]);
        } else {
            String link = words[0].toLowerCase() + words[1] + ".html";
            builder = new StringBuilder("<a href=\"" + link + "\">").
                    append(words[0]).append(" ").append(words[1]).append(" ");
        }
        builder.append("</a>");
        for (int i = (longTitle ? 3 : 2); i < words.length; ++i) {
            builder.append(words[i]).append(" ");
        }
        return builder.toString();
    }

    private String getHtmlFileName(String title) {
        String[] words = title.split(" ");
        boolean longTitle = false;
        if (words.length >= 3) {
            try {
                Integer.parseInt(words[2]);
                longTitle = true;
            } catch (Exception e) {
                longTitle = false;
            }
        }
        if (longTitle) {
            return words[0].toLowerCase() + words[1].toLowerCase() + words[2] + ".html";
        } else {
            return words[0].toLowerCase() + words[1] + ".html";
        }
    }
}
