package com.example.pasta.Model;

public class Comment {
    private String yorum;
    private String gonderen;

    public Comment() {
    }

    public Comment(String yorum, String gonderen) {
        this.yorum = yorum;
        this.gonderen = gonderen;
    }

    public String getYorum() {
        return yorum;
    }

    public void setYorum(String yorum) {
        this.yorum = yorum;
    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }
}
