package com.yeamgood.godungonline.datatable;

public class DataTablesColumn {
	private String name;
    private boolean searchable;
    private boolean orderable;
    private DataTablesSearch search;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean isOrderable() {
        return orderable;
    }

    public void setOrderable(boolean orderable) {
        this.orderable = orderable;
    }

    public DataTablesSearch getSearch() {
        return search;
    }

    public void setSearch(DataTablesSearch search) {
        this.search = search;
    }
}
