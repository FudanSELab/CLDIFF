package edu.fdu.se.javaparser;


import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

/**
 * Created by huangkaifeng on 2018/3/12.
 */
public class JDTParserUtil {

    public static String getDeclarationAsString(MethodDeclaration methodDeclaration){
//    	System.out.println(methodDeclaration.toString());
        return methodDeclaration.getName().toString();
    }
}
