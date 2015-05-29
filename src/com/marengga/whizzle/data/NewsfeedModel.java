package com.marengga.whizzle.data;

public class NewsfeedModel {
    private String id, title, newsContent, image, profilePic, timeStamp, url;
    private int category;
 
    public NewsfeedModel() {
    }
 
    public NewsfeedModel(String id, String title, String newsContent, String image,
            String profilePic, String timeStamp, String url, int category) {
        super();
        this.setId(id);
        this.setTitle(title);
        this.setImage(image);
        this.setNewsContent(newsContent);
        this.setProfilePic(profilePic);
        this.setTimeStamp(timeStamp);
        this.setUrl(url);
        this.setCategory(category);
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNewsContent() {
		return newsContent;
	}

	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

}