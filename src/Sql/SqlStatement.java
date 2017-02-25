/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sql;

/**
 *
 * @author David
 */
public enum SqlStatement {
    CreateDatabase("CREATE DATABASE `%s`;", "CREATE DATABASE `%s`;", "CREATE DATABASE `%s`;", "CREATE DATABASE `%s`;"),
    CreateTable("CREATE TABLE `%s` (", "", "", ""),
    EndCreateTable(");", "", "", ""),
    addRow("`%s`", "", "", ""),
    Insert("INSERT INTO `%s` (", "", "", ""),
    EndInsert(")", "", "", ""),
    InsertColumn("`%s`", "", "", ""),
    InsertValues("VALUES ", "", "", ""),
    EndofInsertValues(");", "", "", ""),
    InsertValue("`%s`", "", "", ""),
    EndofInsertValue("),", "", "", ""),
    NotNull("NOT NULL", "", "", ""),
    PrimaryKey("PRIMARY KEY", "", "", ""),
    ForeignKey("FOREIGN KEY (id_%s) REFERENCES %s(id_%s)", "", "", ""),
    AutoIncrement("AUTO_INCREMENT", "", "", ""),
    StringText("VARCHAR(%s)","","",""),
    IntNumber("INT", "", "", ""),
    UnsignedIntNumber("INT UNSIGNED", "", "", ""),
    LongNumber("BIG INT", "", "", ""),
    UnsignedLongNumber("BIG INT UNSIGNED", "", "", ""),
    FloatNumber("FLOAT", "", "", ""),
    Url("VARCHAR(255)", "", "", ""),
    Date("date with format %s","","","");
    
    
    private final String mySqlStatement;
    private final String oracleStatement;
    private final String microsoftStatement;
    private final String accessStatement;

    private SqlStatement(String mySqlStatement, String oracleStatement, String microsoftStatement, String accessStatement) {
        this.mySqlStatement = mySqlStatement;
        this.oracleStatement = oracleStatement;
        this.microsoftStatement = microsoftStatement;
        this.accessStatement = accessStatement;
    }
    


    public String getStatement(DatabaseType dbType) {
        switch (dbType) {
            case MySql:
                return mySqlStatement;
            
            case Oracle:
                return oracleStatement;
            
            case Microsoft:
                return microsoftStatement;
            
            case Access:
                return accessStatement;
            
            default: 
                return null;
        }
    }
}
