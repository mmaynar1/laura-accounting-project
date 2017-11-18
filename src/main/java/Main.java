public class Main
{
    //Start here :)
    public static void main(String[] args)
    {
        String filePath = "laura3.xlsx";
        if( args.length > 0 )
        {
            filePath = args[0];
        }
        new Laura().isCute( filePath );
    }
}
