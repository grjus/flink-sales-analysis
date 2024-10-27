package com.grjus.output;

import org.apache.flink.api.common.io.OutputFormat;
import org.apache.flink.configuration.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenericFileOutput<T> implements OutputFormat<T> {
    private final String filePath;
    private FileWriter fileWriter;

    public GenericFileOutput(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void configure(Configuration configuration) {
    }

    @Override
    public void open(int taskNumber, int numTasks) throws IOException {
        File file = new File(filePath);
        fileWriter = new FileWriter(file, true); // Open file in append mode
    }

    @Override
    public void writeRecord(T record) throws IOException {
        fileWriter.write(record.toString() + "\n");

    }

    @Override
    public void close() throws IOException {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}