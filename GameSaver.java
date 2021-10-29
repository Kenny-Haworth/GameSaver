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
    public static void main(String[] args)
    {
        //process input arguments
        if (args.length != 3)
        {
            System.err.println("This program requires exactly 3 input arguments to run: The save" +
                               "interval (seconds), the source path, and the target path.");
            System.exit(1);
        }

        long saveInterval = Long.valueOf(args[0]);
        String srcPath = args[1];
        String targetPath = args[2] + "\\";

        //create a timer to backup data at a fixed interval
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public void run()
            {
                System.out.println("Saving files at: " + dtf.format(LocalDateTime.now()));
                saveFiles(srcPath, targetPath);
            }
        },
        0, //no initial delay
        saveInterval);
    }

    /**
     * Creates a new folder with a date and time stamp at the
     * target path and copies in the files at the source path.
     *
     * @param srcPath The path to copy the files to save
     * @param targetPath The path to save the copied files to
     */
    private static void saveFiles(String srcPath, String targetPath)
    {
        //get the timestamp for a unique name
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss");
        String timeStamp = dtf.format(LocalDateTime.now());

        //create a new directory to save to
        File targetDir = new File(targetPath + timeStamp);
        targetDir.mkdir();

        //copy the contents of the SRC_PATH to the new directory
        for (File srcFile : new File(srcPath).listFiles())
        {
            File targetFile = new File(targetDir.getAbsolutePath() + "\\" + srcFile.getName());

            try
            {
                Files.copy(srcFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException e)
            {
                System.err.println("Error saving the following file: " + srcFile.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }
}
