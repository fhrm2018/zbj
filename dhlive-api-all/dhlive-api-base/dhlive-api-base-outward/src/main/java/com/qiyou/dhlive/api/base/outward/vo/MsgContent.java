package com.qiyou.dhlive.api.base.outward.vo;

import java.util.Map;

/**
 * describe:
 *
 * @author fish
 * @date 2018/02/03
 */
public class MsgContent {

    private String Data;

    private String code;

    private String Text;

    private String Ext;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getExt() {
        return Ext;
    }

    public void setExt(String ext) {
        Ext = ext;
    }
}
