package com.akila.config;

import org.hibernate.dialect.Oracle12cDialect;
//import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class OracleDialect extends Oracle12cDialect {

    public static final String MATCH_NATURAL_FUNC = "matchNatural";

    public OracleDialect() {
        super();
//        this.registerFunction(MATCH_NATURAL_FUNC, new SQLFunctionTemplate(StandardBasicTypes.DOUBLE,
//                "match(?1) against  (?2 in natural language mode)"));
//        this.registerFunction("date_format", new SQLFunctionTemplate(StandardBasicTypes.STRING, "to_char(?1, ?2)"));
    }
}
