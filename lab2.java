import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
public class lab2 {
    public static int nextVar = 99;
    public static ArrayList<String> varList;
    public static ArrayList<String> constantList;

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
    public static String getVariable(String clause) {
      String constant = clause;
      constant = clause.split("\\(")[1];
      constant = constant.substring(0, constant.length() - 1);
      return constant;
  }
    public static String getConstant(String clause) {
        String constant = clause;
        int index = 0;
        while(!(varList.contains(constant) || constantList.contains(constant))) {
          constant = clause.split("\\(")[index];
          index++;
        }
        constant = constant.substring(0, constant.length() - 1);
        return constant;
    }

  public static String getNextVariable() {
    String newVar = "M" + nextVar;
    return newVar;
  }
  public static String resolve(String[] predicates, ArrayList<String> variables, ArrayList<String> constants,
    ArrayList<ArrayList<String>> clauses) {
    ArrayList<String> line1 = new ArrayList<>();
    ArrayList<String> line2 = new ArrayList<>();
    for(int x=0; x < clauses.size(); x++) {
      for(int a=0; a < clauses.size(); a++) {
        if(a == x) {continue;}
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
                    //fnc
                    if(clause1.contains("SKF")) {
                      //clause1 has a fnc
                      if(variables.contains(getConstant(clause2))) {
                        line1.remove(y);
                        line2.remove(b);
                        resolve.addAll(line1);
                        resolve.addAll(line2);
                        resolved = true;
                      }
                    } else if(clause2.contains("SKF")) {
                      if(variables.contains(getConstant(clause1))) {
                        line1.remove(y);
                        line2.remove(b);
                        resolve.addAll(line1);
                        resolve.addAll(line2);
                        resolved = true;
                      }            
                    //not fnc
                    } else {
                      String constant1 = getConstant(clause1);
                      String constant2 = getConstant(clause2);
                      //if the constants match
                      if(constant1.equals(constant2)) {
                        line1.remove(y);
                        line2.remove(b);
                        resolve.addAll(line1);
                        resolve.addAll(line2);
                        resolved = true;
                      } else if(variables.contains(constant1) && variables.contains(constant2)) {
                        //!buy(x2) or x2,x1 which will never
                        //buy(x3)
                        line1.remove(y);
                        line2.remove(b);
                        resolve.addAll(line1);
                        resolve.addAll(line2);
                        resolved = true;
                      //universal and constants
                      //buy( x3  )  
                      //!buy( x1   ) flip(y2)
                      //KB: 
                      //clause 1 has a constant and clause 2 has a variable
                      } else if(constants.contains(getConstant(clause1)) && variables.contains(getConstant(clause2))) {
                        String newClause = getConstant(line1.get(y));
                        line2.remove(clause2);
                        String newClausePred = getPredicate(line2.get(0));
                        newClause = "!" + newClausePred + "(" + newClause + ")" ;
                        resolve.add(newClause);
                        resolved = true;
                      //clause1 has a var and clause 2 has a constant
                      } else if(variables.contains(getConstant(clause1)) && constants.contains(getConstant(clause2))) {
                        if(line1.size() == 1  && line2.size() == 1) {
                          return "no";
                        }
                        //replace with constant 2 (the constant)
                        line1.remove(clause1);
                        line2.remove(clause2);
                        String varFinder = getConstant(line1.get(0));
                        for(String str : line1) {
                          varFinder = getConstant(str);
                          while(!variables.contains(varFinder)) {
                            varFinder = varFinder.substring(varFinder.indexOf("\\("), varFinder.indexOf("\\)"));
                          }
                        }
                        for(int i=0; i < line1.size(); i++) {
                          int index = line1.get(i).indexOf(varFinder);
                          if(index != -1) {
                            line1.set(i, changeString(line1.get(i),varFinder, constant2));
                          }
                        }
                        //String newClausePred = getPredicate(line1.get(0));
                        //if(line1.get(0).indexOf("!") != -1) {
                        //  newClause = "!" + newClausePred + "(" + newClause + ")" ;
                        //} else { newClause = newClausePred + "(" + newClause + ")" ;}
                        resolve.addAll(line1);
                        resolved = true;
                        //buy(x,y)
                      } else if (clause1.split(",").length > 1) {
                        //System.out.println(clause1.split(",").length);
                        String preVar1one, preVar1two, preVar2one, preVar2two;
                        //clause1 contains multiple vars "buy(x1,x2)"
                        String preVar1onetemp = clause1.split(",")[0]; //yields "buy(x1"
                        preVar1one = preVar1onetemp.split("\\(")[1]; // yields "x1"
                        preVar1two = clause1.split(",")[1]; // yields "x2)"
                        preVar1two = preVar1two.split("\\)")[0]; // yields "x2
                        //clause 1 & 2 contain multiple vars
                        if(clause2.split(",").length > 1) {
                          String preVar2onetemp = clause2.split(",")[0]; //yields ["buy(x1","x2)"]
                          preVar2one = preVar1onetemp.split("\\(")[1]; // yields "x1"
                          preVar2two = clause2.split(",")[1]; // yields "x2)"
                          preVar2two = preVar2two.split("\\)")[0]; // yields "x2"
                          if(variables.contains(preVar1one) && variables.contains(preVar2one)) {
                            //buy(x1,x2)
                            //buy(x3,x4)   if x1 and x3
                            line1.remove(y);
                            line2.remove(b);
                            resolve.addAll(line1);
                            resolve.addAll(line2);
                            resolved = true;
                          } else if(variables.contains(preVar1two) && variables.contains(preVar2two)) {
                            //buy(x1,x2)
                            //buy(x3,x4)   if x2 and x4
                            line1.remove(y);
                            line2.remove(b);
                            resolve.addAll(line1);
                            resolve.addAll(line2);
                            resolved = true;
                          }
                        } else {
                        //buy(x1,x2)
                        //buy(x3)   if x1 and x3
                          preVar2one = clause2.split("\\)")[0]; //yields "buy(x1"
                          preVar2one = preVar2one.split("\\(")[1]; // yields "x1"
                          if(variables.contains(preVar1two) && variables.contains(preVar2one)) {
                            //buy(x1,x2)
                            //buy(x3,x4)   if x2 and x4
                            line1.remove(y);
                            line2.remove(b);
                            resolve.addAll(line1);
                            resolve.addAll(line2);
                            resolved = true;
                          }
                        }
                      }
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
            } else if(!clause1.contains("!")) {
              //if clause1 is negated and clause 2 is not
              if(clause2.contains("!")) {
                //if the predicated match
                if(getPredicate(clause1).equals(getPredicate(clause2))) {
                  //if the clauses contain constants/variables
                  if(clause1.contains("(") && clause2.contains("(")) {
                    String constant1 = getConstant(clause1);
                    String constant2 = getConstant(clause2);
                    //if the constants match x2 = x2
                    if(constant1.equals(constant2)) {
                      line1.remove(y);
                      line2.remove(b);
                      resolve.addAll(line1);
                      resolve.addAll(line2);
                      resolved = true;
                    } else if(variables.contains(constant1) && variables.contains(constant2)) {
                      //buy(x1) 
                      //buy(x2)
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

  public static String changeString(String root, String var, String replace) {
    return root.split(var)[0] + replace + root.split(var)[1];
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
            ArrayList<String> variables = new ArrayList<>();
            for(int i = 1; i < line.length; i++) {
                variables.add(line[i]);
            }

            line = sc.nextLine().strip().split(" ");
            ArrayList<String> constants = new ArrayList<>();
            for(int i = 1; i < line.length; i++) {
                constants.add(line[i]);
            }
        
            line = sc.nextLine().strip().split(" ");
            ArrayList<String> functions = new ArrayList<>();
            for(int i = 1; i < line.length; i++) {
                functions.add(line[i]);
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
