package com.yeamgood.godungonline.form;

import org.springframework.web.multipart.MultipartFile;

public class DocumentForm {
	
	MultipartFile file;
     
    String description;
 
    public MultipartFile getFile() {
        return file;
    }
 
    public void setFile(MultipartFile file) {
        this.file = file;
    }
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
	
}
