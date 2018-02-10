import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utility
{
    public static void log()
    {
        log("");
    }

    public static void log( String value )
    {
        BufferedWriter writer = null;
        try
        {
            //create a temporary file
            DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            File logFile = new File( Laura.DIRECTORY + "logs/log_" + timeStampPattern.format(LocalDateTime.now()) + ".txt" );
            writer = new BufferedWriter( new FileWriter( logFile, true ) );
            writer.write( value );
            writer.newLine();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                // Close the writer regardless of what happens...
                writer.close();
            }
            catch ( Exception e )
            {
            }
        }
    }
}
