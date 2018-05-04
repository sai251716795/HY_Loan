package com.hx.view.bean;

/**
 * Created by wsq on 2016/5/9.
 */
public class PopupItem {

	public enum Type{
		TITLE, DATA
	}
    int id;
    String name;

    boolean state;
    
    String title;
    
    Type type;

    public PopupItem(){}
    public PopupItem( String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
    
    
}
