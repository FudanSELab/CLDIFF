public class AddTryCatchException{
    public TestException(){}
    boolean testEx() throws Exception{
        boolean ret = true;
        try{
            ret = testEx1();
        }
        catch (Exception e){
            System.out.println("testEx, catch exception");
            ret = false;
            throw e;
        }
        finally{
            System.out.println("testEx, finally; return value=" + ret);
            return ret;
        }
    }
}