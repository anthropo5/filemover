package com.filemover;

import com.filemover.Model.Application;
import com.filemover.View.UserInterface;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Application app = new Application();
//        app.run();
        UserInterface ui = new UserInterface(app);
        ui.run();
    }
}
