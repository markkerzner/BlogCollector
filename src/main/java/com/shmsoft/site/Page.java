package com.shmsoft.site;

import com.shmsoft.blogcollector.Settings;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mark
 */
public class Page implements Comparable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Page.class);
    private final Pattern PAGE_PATTERN = Pattern.compile("[0-9]+");
    private String name; // like 'kiddushin23.html'
    private String title; // like "Kiddushin 1 - Who is allows and who is not..."
    private String contents; // text contents
    private List<Link> links = new ArrayList<>();
    private String imageLink; // like 'images/image-name'
    private String imageTitle;

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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the contents
     */
    public String getContents() {
        return contents;
    }

    /**
     * @param contents the contents to set
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * @return the imageLink
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     * @param imageLink the imageLink to set
     */
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    /**
     * @return the imageTitle
     */
    public String getImageTitle() {
        return imageTitle;
    }

    /**
     * @param imageTitle the imageTitle to set
     */
    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    /**
     * @return the links
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * @param links the links to set
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String formHtml() {
        StringBuilder b = new StringBuilder();
        b.append("<html><title>").append(title).append("</title>").append("<body>");
        b.append(title).append("<br/><br/>");
        if (imageLink == null) {
            LOGGER.warn("No image for page {}", name);
        }
        if (imageLink != null) {
            b.append("<img src=\"").append(imageLink.replaceFirst(Settings.getSettings().getSite(), "..")).append("\"" + "/>");
        }
        String contentsReplace = contents.replace("\n", "<br/>");
        for (Link link : links) {
            contentsReplace = contentsReplace.replaceFirst(link.getText(),
                    "<a href = \"" + link.getSiteRef() + "\">" + link.getText() + "</a>");
        }
        b.append(contentsReplace);
        b.append("</body></html>");
        return b.toString();
    }
    @Override
    public int compareTo(Object other) {
        Matcher m = PAGE_PATTERN.matcher(getTitle());
        String n1 = "1";
        String n2 = "1";
        if (m.find()) {
            n1 = m.group();
        }
        Page pOther = (Page) other;
        Matcher mOther = PAGE_PATTERN.matcher(pOther.getTitle());
        if (mOther.find()) {
            n2 = mOther.group();
        }
        return new Integer(Integer.parseInt(n1)).compareTo(Integer.parseInt(n2));
    }
}
