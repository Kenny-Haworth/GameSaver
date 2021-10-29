import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Backs up the source path to the target path at a set interval.
 * Files are saved in a new folder with a date and time stamp.
 *
 * TODO schtasks /Create /xml "C:\Users\kenny\Desktop\Game Saver\Tasks\This War of Mine AutoSave copy.xml" /tn heresATask
 */
public class GameSaver
{
    private static final int SAVE_INTERVAL = 5*60*1000; //five minutes
    private static final String SRC_PATH = "C:\\Program Files (x86)\\Games\\Steam\\userdata\\161405924\\282070\\remote";
    private static final String TARGET_PATH = "C:\\Users\\kenny\\Desktop\\This War of Mine Backups\\";

    public static void main(String[] args)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

        //create a timer to backup data at a fixed interval
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println("Saving files at: " + dtf.format(LocalDateTime.now()));
                saveFiles();
            }
        },
        0, //no initial delay
        SAVE_INTERVAL);
    }

    /**
     * Creates a new folder with a date and time stamp at the
     * target path and copies in the files at the source path.
     */
    private static void saveFiles()
    {
        //get the timestamp for a unique name
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss");
        String timeStamp = dtf.format(LocalDateTime.now());

        //create a new directory to save to
        File targetDir = new File(TARGET_PATH + timeStamp);
        targetDir.mkdir();

        //copy the contents of the SRC_PATH to the new directory
        for (File srcFile : new File(SRC_PATH).listFiles())
        {
            File targetFile = new File(targetDir.getAbsolutePath() + "\\" + srcFile.getName());

            try
            {
                Files.copy(srcFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException e)
            {
                System.out.println("Error saving the following file: " + srcFile.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }
}
