package com.filecopier;

import com.filecopier.Model.Application;
import com.filecopier.View.UserInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
	// write your code here
        Application app = new Application();
//        app.run();
        UserInterface ui = new UserInterface(app);

        log.info("Starting application");
        ui.run();
        log.info("Exiting application");
    }
}
