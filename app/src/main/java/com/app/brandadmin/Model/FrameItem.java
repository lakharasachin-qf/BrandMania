package com.app.brandadmin.Model;

public class FrameItem implements Cloneable  {
    private String frame1;
    private String frameId;

    public Object clone() throws
            CloneNotSupportedException {
        return super.clone();
    }

    public FrameItem() {
    }

    public String getFrame1() {
        return frame1;
    }

    public void setFrame1(String frame1) {
        this.frame1 = frame1;
    }

    public String getFrameId() {
        return frameId;
    }

    public void setFrameId(String frameId) {
        this.frameId = frameId;
    }
}
