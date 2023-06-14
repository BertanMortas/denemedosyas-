package com.hrms.utility;

import java.util.UUID;

public class CodeGenerator {
    public static String generateCode(){
        String code = UUID.randomUUID().toString();
        String[] data = code.split("-");
        String newCode="";
        for (String str : data){
            newCode += str.charAt(0); //1e483
        }
        return newCode;
    }
}
