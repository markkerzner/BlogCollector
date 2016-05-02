package com.shmsoft.blogcollector.selenium;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.Files;
import com.shmsoft.blogcollector.Settings;
import com.shmsoft.site.Link;
import com.shmsoft.site.Page;
import com.shmsoft.site.Site;
import com.shmsoft.site.Tractate;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TiCollector.class);
    private boolean stop = false;
    private List<String> titles;
    
    public TiCollector() {
        LOGGER.debug("Initiated TiCollector instance");
    }
    
    @Override
    public void run() {
        try {
            Date startDate = new Date();
            switch (Settings.getSettings().getSource()) {
                case Settings.BLOG:
                    downloadFromBlog();
                    break;
                case Settings.BLOG_LOCAL:
                    try {
                        // Devlation from FailFast pattern - need to debug site building
                        loadFromBlogLocal();
                    } catch (Exception e) {
                        LOGGER.warn("Content not completely read from blog, but continuing to build site");
                    }
                    formSite();
                    break;
                case Settings.SITE_LOCAL:
                    loadFromSiteLocal();
                    break;
                default:
                    LOGGER.warn("Nothing to do - source not set");
            }
            long duration = (new Date().getTime() - startDate.getTime()) / 1000;
            LOGGER.info("Duration in seconds: {}", duration);
        } catch (IOException e) {
            LOGGER.error("Too bad", e);
        }
    }
    
    private void loadFromBlogLocal() throws IOException {
        Settings settings = Settings.getSettings();
        String blogRoot = settings.getMyDownloadDir();
        String[] tractates = new File(blogRoot).list();
        Site site = Site.site();
        for (String readTractate : tractates) {
            Tractate tractate = site.getTractate(readTractate);
            File tractateFile = new File(blogRoot + "/" + readTractate);
            if (tractateFile.isDirectory()) {
                String[] pages = tractateFile.list();
                for (String readPage : pages) {
                    if ("index.html".equals(readPage)) {
                        continue;
                    }
                    File pageFile = new File(blogRoot + "/" + readTractate + "/" + readPage);
                    if (!pageFile.isHidden()) {
                        Page page = new Page();
                        page.setName(readPage);
                        LOGGER.debug("Page: {}", page.getName());
                        String html = Files.toString(pageFile,
                                Charset.defaultCharset());
                        Document doc = Jsoup.parse(html);
                        Elements readLinks = doc.getElementsByTag("a");
                        for (Element readLink : readLinks) {
                            // the first link in the blog is page title
                            if (page.getTitle() == null) {
                                page.setTitle(readLink.text());
                            } else if (page.getImageLink() == null) { // the next link refers to the illustration
                                // get the image URL
                                Element image = doc.select("img").first();
                                String blogImageLink = image.absUrl("src");
                                // save it locally
                                String imageDir = settings.getSite() + "/images/";
                                new File(imageDir).mkdirs();
                                int lastSlash = blogImageLink.lastIndexOf("/");
                                String localImageLocation = imageDir + blogImageLink.substring(lastSlash + 1);
                                page.setImageLink(localImageLocation);
                                if (!new File(localImageLocation).exists()) {
                                    // download the image if not there    
                                    LOGGER.debug("Download image {} to {}", blogImageLink, localImageLocation);
                                    FileUtils.copyURLToFile(new URL(blogImageLink), new File(localImageLocation), 5000, 5000);
//                                    byte[] bytes = Jsoup.connect(blogImageLink).ignoreContentType(true).execute().bodyAsBytes();
//                                    Files.write(bytes, new File(localImageLocation));
                                }
                            } else { // other links are important
                                page.getLinks().add(new Link(readLink.attr("href"), readLink.text()));
                            }
                        }
                        String prep = Jsoup.parse(doc.html().replaceAll("(?i)<br[^>]*>", "br2n")).text();
                        String text = prep.replaceAll("br2n", "\n");
                        if (text.length() <= page.getTitle().length()) {
                            LOGGER.warn("text {} and title {}", text, page.getTitle());
                        }
                        // a bit of a hack - placeholder pages should be deleted from the blog
                        // but since I am on a plane now, it is easier to hack
                        if (!text.toLowerCase().contains("placeholder") && !page.getTitle().toLowerCase().contains("placeholder")) {
                            page.setContents(text.substring(page.getTitle().length() + 1));
                            tractate.addPage(page);
                        }
                    }
                }
            }
        }
    }
    
    private void loadFromSiteLocal() {
        
    }
    
    private void downloadFromBlog() {
        WebDriver driver = new HtmlUnitDriver();
        
        String[] tags = Settings.getSettings().getSelectedTagsByName();
        LOGGER.info("Ready to search for {} tages", tags.length);
        for (String tag : tags) {
            titles = new ArrayList<>();
            try {
                LOGGER.info("Downloading for tag {}", tag);
                driver.get("http://mkerzner.blogspot.com/search/label/" + tag);
                boolean more = true;
                while (more) {
                    String html = driver.getPageSource();
                    html = sanitize(html);
                    List<String> posts = findPosts(html);
                    savePosts(tag, posts);
                    LOGGER.debug("Downloading {} documents for the tag {}", posts.size(), tag);
                    more = goToNextPage(driver, html);
                }
                arrangeDownloadedFiles(tag);
            } catch (Exception e) {
                LOGGER.warn("Problem with tag {}, going to the next one", tag, e);
            }
        }
        
    }
    
    private String sanitize(String html) {
        // We will be parsing, so do not sanitize

        // TODO this may really be a hack, we should be able to to special characters, but for now, 
        // let's substitute them
//        html = html.replaceAll("–", "-");
//        html = html.replaceAll("“", "\"");
//        html = html.replaceAll("”", "\"");
//        // TODO did not work for some reason. I am telling you, sanitize all :)
//        html = html.replaceAll("&nbsp;", " ");
        return html;
    }
    
    private boolean goToNextPage(WebDriver driver, String html) {
        WebElement query;
        try {
            query = driver.findElement(By.id("Blog1_blog-pager-older-link"));
        } catch (Exception e) {
            LOGGER.info("No Older Posts");
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
                    + makeFileName(tag.toLowerCase()) + "/index.html"),
                    "<html>" + StringUtils.join(titles, "<br/>\n") + "</html>", true);
        } catch (IOException e) {
            LOGGER.error("Could not arrange titles", e);
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
                        + makeFileName(tag.toLowerCase()) + "/" + getHtmlFileName(title)), "<html>" + post + "</html>");
            }
        } catch (IOException e) {
            LOGGER.error("Problem saving the post titles", e);
        }
    }
    
    private String makeFileName(String tag) {
        tag = tag.replace(" ", "_");
        tag = tag.replace("\'", "");
        // TODO - decide what todo :)
//        tag = tag.replace("\’", "");        
        return tag;
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
            return makeFileName(words[0].toLowerCase() + words[1] + ".html");
        }
    }
    
    private void formSite() throws IOException {
        Settings settings = Settings.getSettings();
        Site site = Site.site();
        for (Tractate tractate : site.getTractates()) {
            for (Page page : tractate.getPages()) {
                File file = new File(settings.getSite() + "/"
                        + tractate.getName() + "/"
                        + page.getName());
                new File(file.getParent()).mkdirs();
                Files.write(page.formHtml(), file, Charset.defaultCharset());
            }
        }
    }
}
