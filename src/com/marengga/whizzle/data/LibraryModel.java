package com.marengga.whizzle.data;


public class LibraryModel {   
    private String libraryId;
    private int category;
    private String title;
    private String author;
    private String description;
    private byte[] cover;
    
	// constructors
	public LibraryModel() {
	}
	
	public LibraryModel(String libraryId, int category, String title, String author, String description, byte[] cover){
		this.setLibraryId(libraryId);
		this.setCategory(category);
	    this.setTitle(title);
	    this.setAuthor(author);
	    this.setDescription(description);
	    this.setCover(cover);
	}

	public String getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(String libraryId) {
		this.libraryId = libraryId;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public byte[] getCover(){
		return cover;
	}
	
	public void setCover(byte[] bs) {
		this.cover = bs;
	}
	
}