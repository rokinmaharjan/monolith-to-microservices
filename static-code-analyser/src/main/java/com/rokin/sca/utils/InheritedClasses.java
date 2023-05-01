package com.rokin.sca.utils;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class InheritedClasses extends VoidVisitorAdapter<Void> {

    @Override
    public void visit(ClassOrInterfaceDeclaration cid, Void arg) {
        super.visit(cid, arg);
        //I guess you just want to print the inherited class's name at the console
        System.out.println("" + cid.getExtendedTypes());
//        return cid.getExtendedTypes();
    }
}

