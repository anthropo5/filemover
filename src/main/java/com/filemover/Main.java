package com.filemover;

import com.filemover.Model.Application;
import com.filemover.View.CLI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        Application app = new Application();
        app.init();

        CLI ui = new CLI(app);
        ui.runCLI();
//        ui.run();

    }
}
