package com.xiaweizi.jianshu;

/**
 * 工程名：  Mianshi
 * 包名：    com.xiaweizi.mianshi
 * 类名：    JianshuBean
 * 创建者：  夏韦子
 * 创建日期： 2017/2/28
 * 创建时间： 14:16
 */

public class JianshuBean {
    private String authorName;          // 作者
    private String authorLink;          // 作者链接
    private String time;                // 更新时间
    private String primaryImg;          // 主图
    private String avatarImg;           // 头像

    private String title;               // 标题
    private String titleLink;           // 标题链接
    private String content;             // 内容
    private String collectionTagLink;   // 专题链接
    private String readNum;             // 阅读量

    private String talkNum;             // 评论
    private String likeNum;             // 喜欢
    private String collectionTag;       // 专题

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorLink() {
        return authorLink;
    }

    public void setAuthorLink(String authorLink) {
        this.authorLink = authorLink;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrimaryImg() {
        return primaryImg;
    }

    public void setPrimaryImg(String primaryImg) {
        this.primaryImg = primaryImg;
    }

    public String getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(String avatarImg) {
        this.avatarImg = avatarImg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleLink() {
        return titleLink;
    }

    public void setTitleLink(String titleLink) {
        this.titleLink = titleLink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCollectionTagLink() {
        return collectionTagLink;
    }

    public void setCollectionTagLink(String collectionTagLink) {
        this.collectionTagLink = collectionTagLink;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

    public String getTalkNum() {
        return talkNum;
    }

    public void setTalkNum(String talkNum) {
        this.talkNum = talkNum;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public String getCollectionTag() {
        return collectionTag;
    }

    public void setCollectionTag(String collectionTag) {
        this.collectionTag = collectionTag;
    }

    @Override
    public String toString() {
        return "JianshuBean{" +
                "authorName='" + authorName + '\'' +
                ", authorLink='" + authorLink + '\'' +
                ", time='" + time + '\'' +
                ", primaryImg='" + primaryImg + '\'' +
                ", avatarImg='" + avatarImg + '\'' +
                ", title='" + title + '\'' +
                ", titleLink='" + titleLink + '\'' +
                ", content='" + content + '\'' +
                ", collectionTagLink='" + collectionTagLink + '\'' +
                ", readNum='" + readNum + '\'' +
                ", talkNum='" + talkNum + '\'' +
                ", likeNum='" + likeNum + '\'' +
                ", collectionTag='" + collectionTag + '\'' +
                '}';
    }
}
