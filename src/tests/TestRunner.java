package tests;
/**
    Simple test runner, to run all of our unit tests.
**/
public class TestRunner {
    public static void main( String[] args ){
        Test[] tests = {};
        int failed=0;
        for( Test test : tests ){
            boolean res = test.run();
            System.out.println(test.name + ":= " + res);
            if(!res){ failed++; }
        }
        if(failed>0){
            System.out.println(failed+" Tests failed!");
        }else{
            System.out.println("Success!");
        }
    }
}

