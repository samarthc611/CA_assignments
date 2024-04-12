package processor.memorysystem;
import configuration.Configuration;
import generic.*;
import processor.Clock;
import processor.Processor;
import processor.pipeline.*;

public class CacheLine{
    int tag;
    int data;
    int offset;
    int counter;

    public CacheLine(){
        this.tag = -1;
        this.data = 0;
        this.offset = -1;
        this.counter = 14;
    }

    public void setTag(int value){
        this.tag = value;
    }
    public int getTag() {
        return this.tag;
    }

    public void setData(int value){
        this.data = value;
    }
    public int getData() {
        return this.data;
    }

    public void setOffset(int value){
        this.offset = value;
    }
    public int getOffset() {
        return this.offset;
    }

    public void incrementCounter(){
        this.counter++;
    }
    public void decrementCounter(){
        this.counter--;
    }
    public int getCounter(){
        return this.counter;
    }
}
