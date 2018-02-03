package com.lzdn.note.common.support.search;

/**
 * WHERE过滤条件连接类型
 * @package core.support.search
 */
public enum SearchType {
    OR("OR连接", "or"), AND("AND连接","and");

    private final String info;
    private final String type;

    SearchType(final String info, String type) {
        this.info = info;
        this.type = type;
    }

    public String getInfo() {
        return this.info;
    }

    public String getTypeStr() {
        return this.type;
    }
}
