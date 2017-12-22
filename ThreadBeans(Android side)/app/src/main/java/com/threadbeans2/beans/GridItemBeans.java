package com.threadbeans2.beans;

public class GridItemBeans {

    private String path;
    private boolean is_path;
    private String filename;
    private boolean checked;
    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public GridItemBeans(String filename, String path, boolean is_path) {
        this.path = path;
        this.is_path = is_path;
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public boolean is_path() {
        return is_path;
    }

    public String getFilename() {
        return filename;
    }
}
