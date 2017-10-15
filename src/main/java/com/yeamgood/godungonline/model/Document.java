package com.yeamgood.godungonline.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "document")
public class Document extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "document_Id")
	private Long documentId;
	
	@Transient
	private String documentIdEncrypt;
	
	@Column(name="name", length=100, nullable=false)
    private String name;
     
    @Column(name="description")
    @Length(max = 100, message = "{validation.max.lenght}")
    private String description;
     
    @Column(name="type", length=100, nullable=false)
    private String type;
    
    @Column(name="size", nullable=false)
    private long size;
     
    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name="content", nullable=false)
    private byte[] content;
    
    public void encryptData() {
		this.documentIdEncrypt = AESencrpUtils.encryptLong(this.documentId);
		this.documentId = null;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public String getDocumentIdEncrypt() {
		return documentIdEncrypt;
	}

	public void setDocumentIdEncrypt(String documentIdEncrypt) {
		this.documentIdEncrypt = documentIdEncrypt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
    	
}
