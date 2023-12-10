package com.robey.tale;

import java.io.File;

public class TaleContext {
    Boolean forever;
    File file;

    public TaleContext(String filename, boolean forever){
        this.forever = forever;
        file = new File(filename);
    }

    public File getFile(){
        return file;
    }

    public boolean isForever(){
        return forever.booleanValue();
    }
}
