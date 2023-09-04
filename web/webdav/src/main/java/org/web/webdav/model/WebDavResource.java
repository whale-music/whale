package org.web.webdav.model;

import io.milton.annotations.*;
import io.milton.http.Auth;
import io.milton.http.Request;
import io.milton.resource.Resource;

import java.util.Date;

/**
 * Webdav文件
 */
public class WebDavResource implements Resource {
    private String name;
    private Date createdDate;
    private Date modifiedDate;
    private Long contentLength;
    
    public WebDavResource(String name, Date createdDate, Date modifiedDate, Long contentLength) {
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.contentLength = contentLength;
    }
    
    public WebDavResource(String name, Long contentLength) {
        this.name = name;
        this.contentLength = contentLength;
    }
    
    public WebDavResource() {
    }
    
    @Override
    public String getUniqueId() {
        return null;
    }
    
    @Name
    @UniqueId
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public Object authenticate(String user, String password) {
        return null;
    }
    
    @Override
    public boolean authorise(Request request, Request.Method method, Auth auth) {
        return false;
    }
    
    @Override
    public String getRealm() {
        return null;
    }
    
    @ModifiedDate
    public Date getModifiedDate() {
        return modifiedDate;
    }
    
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    @Override
    public String checkRedirect(Request request) {
        return null;
    }
    
    @CreatedDate
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    @ContentLength
    public Long getContentLength() {
        return contentLength;
    }
    
    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }
}
