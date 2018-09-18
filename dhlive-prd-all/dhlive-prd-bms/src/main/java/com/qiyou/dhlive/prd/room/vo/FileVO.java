package com.qiyou.dhlive.prd.room.vo;

/**
 * Created by admin on 2017/10/16.
 */
public class FileVO {

    public FileVO(Integer error, String url) {
        this.error = error;
        this.url = url;
    }

    private Integer error;

    private String url;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
