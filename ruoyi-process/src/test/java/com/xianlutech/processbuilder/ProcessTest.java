package com.xianlutech.processbuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProcessTest {

    public static void main(String[] args) throws IOException {
        List<String> cmdList = new ArrayList<>();
        cmdList.add("C:/devSoft/Java/jdk1.8.0_171/bin/java.exe");
        cmdList.add("-version");
        ProcessBuilder builder = new ProcessBuilder(cmdList);
        Process start = builder.start();
        OutputStream stream = start.getOutputStream();
    }

}
