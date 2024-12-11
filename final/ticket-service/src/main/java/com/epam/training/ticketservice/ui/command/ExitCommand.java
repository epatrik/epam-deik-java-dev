package com.epam.training.ticketservice.ui.command;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class ExitCommand {

        @ShellMethod(key = "exit", value = "Exit the application")
        public void exitApplication() {
                System.exit(0);
        }
}
