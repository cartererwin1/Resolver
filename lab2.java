import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
public class lab2 {
    public static String getPredicate(String clause) {

        String predicate = clause;
        if(predicate.indexOf("(")== -1) {
          if (predicate.contains("!")) {
            predicate = predicate.substring(1, predicate.length());
          }
          return predicate;
        }
        predicate = clause.split("\\(")[0];
        if (predicate.contains("!")) {
            predicate = predicate.substring(1, predicate.length());
        }
        return predicate;
    }
    public static ArrayList<String> removeDuplicates(ArrayList<String> arr) {
        ArrayList<String> newArr = new ArrayList<>();
        for(String str : arr) {
            if(!newArr.contains(str)) {
                newArr.add(str);
            }
        }
        return newArr;
    }

    public static String getConstant(String clause) {
        String constant = clause;
        constant = clause.split("\\(")[1];
        constant = constant.substring(0, constant.length() - 1);
        return constant;
    }
    public static String resolve(String[] predicates, String[] variables, String[] constants, ArrayList<ArrayList<String>> clauses) {
        ArrayList<String> line1 = new ArrayList<>();
        ArrayList<String> line2 = new ArrayList<>();
        for(ArrayList<String> clause : clauses) {
            //System.out.println(clause);
        }
        //loop for first collection of clauses
        for(int x = 0; x < clauses.size(); x++) {
            for(int a = 0; a < clauses.size(); a++) {
                if(a == x) {
                    continue;}
                for(int y = 0; y < clauses.get(x).size(); y++) {
                    ArrayList<String> resolve = new ArrayList<>();
                    boolean resolved = false;
                    //find another collection of clauses to compare
                    for(int b = 0; b < clauses.get(a).size(); b++) {
                        resolved = false;
                        //line1 is the current collection of clauses looked at
                        line1 = new ArrayList<String>();
                        line1.addAll(clauses.get(x));
                        //clause 1 is the first clause to compare
                        String clause1 = line1.get(y);                            
                        line2 = new ArrayList<String>();
                        line2.addAll(clauses.get(a));
                        //clause2 is the second clause to compare
                        String clause2 = line2.get(b);
                        //if clause1 is negated
                        if(clause1.contains("!")) {
                            //if clause1 is negated and clause 2 is not
                            if(!clause2.contains("!")) {
                                //if the predicated match
                                if(getPredicate(clause1).equals(getPredicate(clause2))) {
                                    //if the clauses contain constants/variables buy(x3)
                                    if(clause1.contains("(") && clause2.contains("(")) {
                                        String constant1 = getConstant(clause1);
                                        String constant2 = getConstant(clause2);
                                        //if the constants match
                                        if(constant1.equals(constant2)) {
                                            line1.remove(y);
                                            line2.remove(b);
                                            resolve.addAll(line1);
                                            resolve.addAll(line2);
                                            resolved = true;
                                        }
                                    //else the clauses are just predicates that match !night and night
                                    } else {
                                      line1.remove(y);
                                      line2.remove(b);
                                      resolve.addAll(line1);
                                      resolve.addAll(line2);
                                      resolved = true;
                                    }   
                                }
                            }
                        }
                        else if(!clause1.contains("!")) {
                            //if clause1 is negated and clause 2 is not
                            if(clause2.contains("!")) {
                                //if the predicated match
                                if(getPredicate(clause1).equals(getPredicate(clause2))) {
                                    //if the clauses contain constants/variables
                                    if(clause1.contains("(") && clause2.contains("(")) {
                                        String constant1 = getConstant(clause1);
                                        String constant2 = getConstant(clause2);
                                        //if the constants match
                                        if(constant1.equals(constant2)) {
                                            line1.remove(y);
                                            line2.remove(b);
                                            resolve.addAll(line1);
                                            resolve.addAll(line2);
                                            resolved = true;
                                        }
                                    //else the clauses are just predicates that match !night and night
                                    } else {
                                      line1.remove(y);
                                      line2.remove(b);
                                      resolve.addAll(line1);
                                      resolve.addAll(line2);
                                      resolved = true;
                                    }   
                                }
                            }
                        }
                        if(!clauses.contains(resolve) && resolved) {
                            resolve = removeDuplicates(resolve);
                            clauses.add(resolve);
                        }//&& resolv???
                    }
                    if(resolve.isEmpty() && resolved) { //&& resolved
                        //System.out.println("-------");
                        for(ArrayList<String> clause : clauses) {
                            //System.out.println(clause);
                        }
                        return "no";
                    } 
                }
            }
        }
        for(ArrayList<String> clause : clauses) {
            //System.out.println(clause);
        }
        return "yes";            
    }


    public static void main(String[] args) {
        Scanner sc;
        File inputFile = new File(args[0]);
        try {
            sc = new Scanner(inputFile);
            String[] line = sc.nextLine().strip().split(" ");
            String[] predicates = new String[line.length-1];
            for(int i = 1; i < line.length; i++) {
                predicates[i-1] = line[i];
            }

            line = sc.nextLine().strip().split(" ");
            String[] variables = new String[line.length-1];
            for(int i = 1; i < line.length; i++) {
                variables[i-1] = line[i];
            }

            line = sc.nextLine().strip().split(" ");
            String[] constants = new String[line.length-1];
            for(int i = 1; i < line.length; i++) {
                constants[i-1] = line[i];
            }
        
            line = sc.nextLine().strip().split(" ");
            String[] functions = new String[line.length-1];
            for(int i = 1; i < line.length; i++) {
                functions[i-1] = line[i];
            }
            ArrayList<ArrayList<String>> clauses = new ArrayList<>();
            sc.nextLine();
            int lineNum = 0;
            while(sc.hasNextLine()) {
                line = sc.nextLine().split(" ");
                clauses.add(new ArrayList<String>());
                for(int i = 0; i < line.length; i++) {
                    clauses.get(lineNum).add(line[i]);
                }
                lineNum++;
            }
            sc.close();
            System.out.println(resolve(predicates, variables, constants, clauses));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
