package com.himangi.imagepreview;

import java.io.Serializable;

/**
 * Created by Himangi on 4/7/18
 */
public class PreviewFile implements Serializable {
    private String imageURL;
    private String imageDescription;

    public PreviewFile(String imageURL, String imageDescription) {
        this.imageURL = imageURL;
        this.imageDescription = imageDescription;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }
}
