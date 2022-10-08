package com.app.awaaz.Models;

public class PostsModel {
    String id;
    String userfname;
    String userlname;
    String userprofilepic;
    String postimage;
    String postdesc;
    String totalcomment;
    String totallikes;

    public PostsModel(String id, String userfname, String userlname, String userprofilepic, String postimage, String postdesc, String totalcomment, String totallikes) {
        this.id = id;
        this.userfname = userfname;
        this.userlname = userlname;
        this.userprofilepic = userprofilepic;
        this.postimage = postimage;
        this.postdesc = postdesc;
        this.totalcomment = totalcomment;
        this.totallikes = totallikes;
    }

    public String getId() {
        return id;
    }

    public String getUserfname() {
        return userfname;
    }

    public String getUserlname() {
        return userlname;
    }

    public String getUserprofilepic() {
        return userprofilepic;
    }

    public String getPostimage() {
        return postimage;
    }

    public String getPostdesc() {
        return postdesc;
    }

    public String getTotalcomment() {
        return totalcomment;
    }

    public String getTotallikes() {
        return totallikes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserfname(String userfname) {
        this.userfname = userfname;
    }

    public void setUserlname(String userlname) {
        this.userlname = userlname;
    }

    public void setUserprofilepic(String userprofilepic) {
        this.userprofilepic = userprofilepic;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public void setPostdesc(String postdesc) {
        this.postdesc = postdesc;
    }

    public void setTotalcomment(String totalcomment) {
        this.totalcomment = totalcomment;
    }

    public void setTotallikes(String totallikes) {
        this.totallikes = totallikes;
    }
}
