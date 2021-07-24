package com.esmc.mcnp.client.dto;

public class ExceptionMessage {

	private Integer resultat;
    private Long timestamp;
    private String status;
    private String error;
    private String message;
    private String path;
    private String className;
    private String date;

    public ExceptionMessage() {
    }

    public ExceptionMessage(String message, String className, String path, String date) {
        this.message = message;
        this.className = className;
        this.path = path;
        this.date = date;
    }

    public Integer getResultat() {
		return resultat;
	}

	public void setResultat(Integer resultat) {
		this.resultat = resultat;
	}

	public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
   
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
