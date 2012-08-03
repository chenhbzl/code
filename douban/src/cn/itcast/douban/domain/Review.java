package cn.itcast.douban.domain;

public class Review {
	private String title;
	private String reviewpath;
	private String iconpath;
	private String reviewauthor;
	private String summary;
	private int rating;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getReviewpath() {
		return reviewpath;
	}
	public void setReviewpath(String reviewpath) {
		this.reviewpath = reviewpath;
	}
	public String getIconpath() {
		return iconpath;
	}
	public void setIconpath(String iconpath) {
		this.iconpath = iconpath;
	}
	public String getReviewauthor() {
		return reviewauthor;
	}
	public void setReviewauthor(String reviewauthor) {
		this.reviewauthor = reviewauthor;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
}
