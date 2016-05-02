package com.shmsoft.site;

import com.shmsoft.blogcollector.Settings;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark
 */
public class Page {
    private String name; // like 'kiddushin23.html'
    private String title; // like "Kiddushin 1 - Who is allows and who is not..."
    private String contents; // text contents
    private List <Link> links = new ArrayList <> ();
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
    public List <Link> getLinks() {
        return links;
    }

    /**
     * @param links the links to set
     */
    public void setLinks(List <Link> links) {
        this.links = links;
    }
    public String formHtml() {
        StringBuilder b = new StringBuilder();
        b.append("<html><title>").append(title).append("</title>").append("<body>");
        b.append(title).append("<br/><br/>");
        b.append("<img src=\"").append(imageLink.replace(Settings.getSettings().getSite(), "..")).append("\"" + "/>");
        String contentsReplace = contents.replace("\n", "<br/>");
        for (Link link: links) {
            contentsReplace = contentsReplace.replace(link.getText(), 
                    "<a href = \"" + link.getRef() + "\">" + link.getText() + "</a>");
        }
        b.append(contentsReplace);
        b.append("</body></html>");
        return b.toString();
    }
}
