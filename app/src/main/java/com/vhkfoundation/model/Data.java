package com.vhkfoundation.model;

public class Data {
    private int blog_category_id;
    private String content;
    private String created_at;
    private String details_img;
    private String featured_image;
    private int id;
    private Object image; // Use Object if it can be null or variable type
    private String is_donate;
    private String is_emergency;
    private String meta_description;
    private String meta_keywords;
    private String meta_title;
    private String short_desc;
    private String slug;
    private String status;
    private String tags;
    private String title;
    private String trash;
    private String type;
    private String updated_at;
    private User user;
    private int user_id;
    private String feed_created_by;
    private String total_like;
    private String is_like;
    private String datetime;

    // Getters and setters for all fields

    public int getBlog_category_id() { return blog_category_id; }
    public void setBlog_category_id(int blog_category_id) { this.blog_category_id = blog_category_id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getDetails_img() { return details_img; }
    public void setDetails_img(String details_img) { this.details_img = details_img; }

    public String getFeatured_image() { return featured_image; }
    public void setFeatured_image(String featured_image) { this.featured_image = featured_image; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Object getImage() { return image; }
    public void setImage(Object image) { this.image = image; }

    public String getIs_donate() { return is_donate; }
    public void setIs_donate(String is_donate) { this.is_donate = is_donate; }

    public String getIs_emergency() { return is_emergency; }
    public void setIs_emergency(String is_emergency) { this.is_emergency = is_emergency; }

    public String getMeta_description() { return meta_description; }
    public void setMeta_description(String meta_description) { this.meta_description = meta_description; }

    public String getMeta_keywords() { return meta_keywords; }
    public void setMeta_keywords(String meta_keywords) { this.meta_keywords = meta_keywords; }

    public String getMeta_title() { return meta_title; }
    public void setMeta_title(String meta_title) { this.meta_title = meta_title; }

    public String getShort_desc() { return short_desc; }
    public void setShort_desc(String short_desc) { this.short_desc = short_desc; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTrash() { return trash; }
    public void setTrash(String trash) { this.trash = trash; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public String getTotal_like() {
        return total_like;
    }

    public void setTotal_like(String total_like) {
        this.total_like = total_like;
    }

    public String getFeed_created_by() {
        return feed_created_by;
    }

    public void setFeed_created_by(String feed_created_by) {
        this.feed_created_by = feed_created_by;
    }
}
