package com.moutamid.maintenanceinspectionapp.models;

public class SliderModal {
    private String title;
    private String heading;
    private int imgUrl;

    public SliderModal() {
    }

    public SliderModal(String title, String heading, int imgUrl) {
        this.title = title;
        this.heading = heading;
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }
}
