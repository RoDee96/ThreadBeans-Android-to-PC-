package com.threadbeans2.beans;

import java.io.Serializable;

/**
 * Created by Rohit on 7/30/2017.
 */

public class FileBean implements Serializable{
    String filename;
    long filesize;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }
}
