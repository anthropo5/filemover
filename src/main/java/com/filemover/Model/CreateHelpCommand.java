package com.filemover.Model;

import com.filemover.View.CLI;

public class CreateHelpCommand implements Command {
    private CLI cli;

    public CreateHelpCommand(CLI cli) {
        this.cli = cli;
    }

    @Override
    public String execute() {
        return cli.createHelpAll();
    }
}
