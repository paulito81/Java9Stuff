package no.phasfjo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class JavaTaskBar {

    public JavaTaskBar() throws InterruptedException {
        runTaskBar();
    }

    private void runTaskBar() throws InterruptedException {
        Taskbar taskbar = Taskbar.getTaskbar();
        if(taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)){
            Image icon = null;
            try {
                icon = ImageIO.read(new File("src/images/icon.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            taskbar.setIconImage(icon);
        }

        Thread.sleep(1000);
        for(int i = 0; i <= 100; i++){
            taskbar.setProgressValue(i);
            Thread.sleep(30);
        }

        // taskbar.setIconBadge("Done!"); NOT SUPPORTED ON MAC
        taskbar.requestUserAttention(true, true); // Must be clicked by user to quit.
    }
}
