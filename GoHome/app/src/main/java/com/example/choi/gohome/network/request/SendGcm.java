package com.example.choi.gohome.network.request;

/**
 * Created by choi on 2016-09-12.
 */
public class SendGcm {
    private String to;
    private MsgData data;

    public SendGcm(String to, MsgData data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public MsgData getData() {
        return data;
    }

    public void setData(MsgData data) {
        this.data = data;
    }
}
