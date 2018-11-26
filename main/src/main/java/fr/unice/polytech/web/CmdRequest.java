package fr.unice.polytech.web;

import java.util.List;

public class CmdRequest {

    private String cmd;
    private List<String> args;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
