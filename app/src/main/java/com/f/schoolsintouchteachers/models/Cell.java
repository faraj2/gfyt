package com.f.schoolsintouchteachers.models;

/**
 * Created by admin on 3/22/2018.
 */

public class Cell {
    private boolean checked;

    public Cell(boolean checked) {
        this.checked = checked;
    }

    public Cell() {
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
