package com.akila.config;

import org.hibernate.dialect.InnoDBStorageEngine;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.MySQLStorageEngine;
import org.hibernate.type.StandardBasicTypes;

public class MySQLDialect extends MySQL8Dialect {

    public static final String MATCH_NATURAL_FUNC = "matchNatural";

    public MySQLDialect() {
        super();
//        this.registerFunction(MATCH_NATURAL_FUNC, new SQLFunctionTemplate(StandardBasicTypes.DOUBLE,
//                "match(?1) against  (?2 in natural language mode)"));
//        this.registerFunction("date_format", new SQLFunctionTemplate(StandardBasicTypes.STRING, "to_char(?1, ?2)"));
    }

    @Override
    protected MySQLStorageEngine getDefaultMySQLStorageEngine() {
        return InnoDBStorageEngine.INSTANCE;
    }
}
