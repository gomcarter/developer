package com.yiayoframework.httpbaseapi.message.request;

import org.apache.http.Consts;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cn40387 on 15/5/11.
 */
public class PostRequestMessage extends RequestMessage {

    private Map<String, File> files = new HashMap<>();

    private String body;

    private String bodyPartName = "body";

    private Charset charset;

    private ContentType fileContentType = ContentType.APPLICATION_OCTET_STREAM;

    public PostRequestMessage(String apiName) {
        this(apiName, null);
    }

    public PostRequestMessage(String apiName, ContentType contentType) {
        this(apiName, contentType, null, null, null);
    }

    public PostRequestMessage(String apiName, ContentType contentType, Charset charset) {
        this(apiName, contentType, null, null, charset);
    }

    public PostRequestMessage(String apiName, ContentType contentType, Map<String, File> files, String body, Charset charset) {
        super(apiName, contentType);
        this.files = files;
        this.body = body;
        this.charset = charset == null ? Consts.ISO_8859_1 : charset;
    }

    public Map<String, File> getFiles() {
        return files;
    }

    public void setFiles(Map<String, File> files) {
        this.files = files;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodyPartName() {
        return bodyPartName;
    }

    public void setBodyPartName(String bodyPartName) {
        this.bodyPartName = bodyPartName;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public ContentType getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(ContentType fileContentType) {
        this.fileContentType = fileContentType;
    }
}
