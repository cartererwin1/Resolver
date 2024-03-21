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

    public static String getConstant(String clause) {
        String constant = clause;
        constant = clause.split("\\(")[1];
        constant = constant.substring(0, constant.length() - 1);
        return constant;
    }
    public static String resolve(String[] predicates, String[] variables, String[] constants, ArrayList<ArrayList<String>> clauses) {
        //ArrayList<String> resolve = new ArrayList<>();
        //resolve.addAll(clauses);
        ArrayList<String> line1 = new ArrayList<>();
        ArrayList<String> line2 = new ArrayList<>();
        System.out.println(clauses);
        for(int x = 0; x < clauses.size(); x++) {
          //line1 is the current collection of clauses looked at
          line1 = new ArrayList<String>();
          line1.addAll(clauses.get(x));
            for(int y = 0; y < line1.size(); y++) {
                //clause 1 is the first clause to compare
                String clause1 = line1.get(y);
                //if clause1 is negated
                if(clause1.contains("!")) {
                    //find another collection of clauses to compare
                    for(int a = 0; a < clauses.size(); a++) {
                        line2 = new ArrayList<String>();
                        line2.addAll(clauses.get(a));
                        for(int b = 0; b < line2.size(); b++) {
                            //clause2 is the second clause to compare
                            String clause2 = line2.get(b);
                            //if clause1 is negated and clause 2 is not
                            if(!clause2.contains("!")) {
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
                                        }
                                    //else the clauses are just predicates that match !night and night
                                    } else {
                                      line1.remove(y);
                                      line2.remove(b);
                                    }     
                                }
                            }
                        }
                        if(line1.isEmpty() || line2.isEmpty()) {
                          System.out.println(clauses);
                          return "no";
                        }
                        else {
                          if(!clauses.contains(line1)) {clauses.add(line1);}
                          if(!clauses.contains(line2)) {clauses.add(line2);}
                        }
                    }
                //else clause1 is not negated
                } else {
                    //find another collection of clauses to compare
                    for(int a = 0; a < clauses.size(); a++) {
                        line2 = new ArrayList<String>();
                        line2.addAll(clauses.get(a));
                        for(int b = 0; b < line2.size(); b++) {
                            //clause2 is the second clause to compare
                            String clause2 = line2.get(b);
                            //if clause 1 is not negated and clause2 is
                            if(clause2.contains("!")) {
                                //if the predicates match
                                if(getPredicate(clause1).equals(getPredicate(clause2))) {
                                  //if the clauses contain constants/variables
                                  if(clause1.contains("(") && clause2.contains("(")) {
                                    String constant1 = getConstant(clause1);
                                    String constant2 = getConstant(clause2);
                                    //if the constants match
                                    if(constant1.equals(constant2)) {
                                      if(line1.get(y) == clause1) {

                                      }
                                      line1.remove(y);
                                      line2.remove(b);
                                    }
                                  } else {
                                    line1.remove(y);
                                    line2.remove(b);
                                  }
                                }
                            }
                        }
                        if(line1.isEmpty() || line2.isEmpty()) {
                          System.out.println(clauses);
                          return "no";
                        }
                        else {
                          if(!clauses.contains(line1)) {clauses.add(line1);}
                          if(!clauses.contains(line2)) {clauses.add(line2);}
                        }
                    }
                }
            }
        }
        System.out.println(clauses);
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
