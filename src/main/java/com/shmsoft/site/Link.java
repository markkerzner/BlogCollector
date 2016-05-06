package com.shmsoft.site;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mark
 */
public class Link {

    private String ref;
    private String text;
    private final Pattern BLOGSPOT_PATTERN = Pattern.compile("-[0-9]+");
    private String siteRef;
    
    public Link(String ref, String text) {
        this.ref = ref;
        this.text = text;
    }

    /**
     * @return the ref
     */
    public String getRef() {
        return ref;
    }

    /**
     * @param ref the ref to set
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * In case that the link points to another place on the blog, it is
     * converted to a site link This relies on the regularity in page naming,
     * which will not be 100% consistent so manual fixes may still be needed
     * later
     */
    private void convertBlogRefToSite() {
        if (ref.contains("blogspot")) {
            Matcher m = BLOGSPOT_PATTERN.matcher(ref);
            if (m.find()) {
                String numberRef = m.group();
                int numberIndex = ref.indexOf(numberRef);
                int lastSlash = ref.lastIndexOf("/", numberIndex);
                String pageRef = ref.substring(lastSlash, numberIndex);                
                    siteRef = "http://talmudilluminated.com" + pageRef +
                            pageRef + numberRef.substring(1) + ".html";                
            }

        }
    }

    /**
     * @return the siteRef
     */
    public String getSiteRef() {
        convertBlogRefToSite();
        return siteRef;
    }
}
