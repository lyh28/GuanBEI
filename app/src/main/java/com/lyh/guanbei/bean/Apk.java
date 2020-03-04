package com.lyh.guanbei.bean;

import com.lyh.guanbei.common.Contact;

public class Apk {
    private String version;
    private String path;
    private long size;

    public Apk(String version, String path, long size) {
        this.version = version;
        this.path = path;
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return Contact.FILRURL+path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Apk{" +
                "version='" + version + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                '}';
    }
}
