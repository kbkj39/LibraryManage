package com.ksamar.library.sqlite;

import org.hibernate.MappingException;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

public class SQLiteIdentityColumnSupport extends IdentityColumnSupportImpl {

    @Override
    public boolean supportsIdentityColumns() {
        return true;
    }

    @Override
    public String getIdentitySelectString(String table, String column, int type)
            throws MappingException {
        return "select last_insert_rowid() + 1";
    }

    @Override
    public String getIdentityColumnString(int type) throws MappingException {
        return "integer";
    }
}