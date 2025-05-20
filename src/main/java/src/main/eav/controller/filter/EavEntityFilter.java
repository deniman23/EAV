package src.main.eav.controller.filter;


public class EavEntityFilter {

    private String search;

    private SortOrder sortOrder = SortOrder.ASC;

    public enum SortOrder {
        ASC,
        DESC;
    }

    public String getSearch() {
        return search;
    }

    public EavEntityFilter setSearch(String search) {
        this.search = search;
        return this;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public EavEntityFilter setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }
}