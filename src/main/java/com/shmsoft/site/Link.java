package com.shmsoft.site;

/**
 *
 * @author mark
 */
public class Link {
    private String ref;
    private String text;

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
     * In case that the link points to another place on the blog, it is converted to a site link
     * This relies on the regularity in page naming, which will not be 100% consistent
     * so manual fixes may still be needed later
     */
    public void convertBlogToSite() {
        if (ref.contains("blogspot")) {
            int lastSlash = ref.lastIndexOf("/");
            
        }
    }
}
