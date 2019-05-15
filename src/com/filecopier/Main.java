package com.filecopier;

import com.filecopier.Model.Application;
import com.filecopier.View.UserInterface;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Application app = new Application();
//        app.run();
        UserInterface ui = new UserInterface(app);
        ui.run();
    }
}
