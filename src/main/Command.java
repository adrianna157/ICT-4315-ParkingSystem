package main;

import java.util.Properties;

public interface Command {
    void checkParameters(Properties properties);
    String execute(Properties properties);
    String getCommandName();
    String getDisplayName();
}
