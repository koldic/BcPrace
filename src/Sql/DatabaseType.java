/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author David
 */
public enum DatabaseType {
    
    MySql(new ArrayList<>(Arrays.asList("DATE", "DATETIME", "TIMESTAMP", "TIME", "YEAR(%s)"))),
    Oracle(new ArrayList<>(Arrays.asList("DATE", "DATETIME", "TIMESTAMP", "TIME", "YEAR(%s)"))),
    Microsoft(new ArrayList<>(Arrays.asList("DATE", "DATETIME", "TIMESTAMP", "TIME", "YEAR(%s)"))),
    Access(new ArrayList<>(Arrays.asList("DATE", "DATETIME", "TIMESTAMP", "TIME", "YEAR(%s)"))),
    DB2(new ArrayList<>(Arrays.asList("DATE", "DATETIME", "TIMESTAMP", "TIME", "YEAR(%s)")));
    
    private final List<String> dateTypes; 

    private DatabaseType(List<String> dateTypes) {
        this.dateTypes = dateTypes;
    }

    public List<String> getDateTypes() {
        return dateTypes;
    }

}
