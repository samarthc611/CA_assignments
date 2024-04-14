package processor.memorysystem;
import java.util.HashMap;

import configuration.Configuration;
import generic.*;
import processor.pipeline.*;
import processor.*;

public class Cache implements Element {
    Processor containingProcessor;
    int cacheLatency;

    int cache_addr;
    Element cacheRequestingElement;
    // String addressString;
    Boolean readMiss;
    int dataMissed;
    CacheLine[] Cachearray;
    int num_lines;

    public static HashMap<Integer, Integer> Latency = new HashMap<Integer, Integer>() {{
        put(1024, 4);
        put(512, 3);
        put(128,2);
        put(16,1);
    }};

    public Cache(Processor processor, int size){
        this.containingProcessor = processor;
        this.num_lines = size / 4;
        Cachearray = new CacheLine[num_lines];

        for(int i = 0; i < num_lines; i++){
            Cachearray[i] = new CacheLine();
        }

        cacheLatency = Latency.get(size);
        System.out.print("Cache Latency is set to: ");
        System.out.println(cacheLatency);
        
    }

    public int getCacheLatency(){
        return cacheLatency;
    }

    

    public void handleResponse(int value){
        int min = 0;
        for(int i = 0; i < num_lines; i++){
            if (Cachearray[i].getCounter() < Cachearray[i].getCounter()){
                min = i;
            }
        }
        // System.out.print("min: ");
        // System.out.println(min);
        Cachearray[min].setData(value);
        Cachearray[min].setTag(cache_addr);
        Cachearray[min].setCounter(14);
        // System.out.println(Cachearray[min].getData());
        // System.out.println(Cachearray[min].getTag());
        if(readMiss == true){
            // System.out.println("Cache read miss handling");
            Simulator.getEventQueue().addEvent(
                    new MemoryResponseEvent(
                            Clock.getCurrentTime(),
                            this,
                            cacheRequestingElement,
                            value)
            );
            // System.out.println("Read response handled");
        }
        else{
            // System.out.println("Cache write miss handling");

            // readMiss = false;
        //     Simulator.getEventQueue().addEvent(
        //         new MemoryWriteEvent(
        //                 Clock.getCurrentTime() + Configuration.mainMemoryLatency,
        //                 this,
        //                 containingProcessor.getMainMemory(),
        //                 cache_addr,
        //                 value)
        //          );
            System.out.println("Write response handled");
            cacheWrite(cache_addr, dataMissed, cacheRequestingElement);
        }
    }
    public void cacheRead(int address, Element requestingElement){
        int cacheTag;
        for(int i = 0; i < num_lines; i++)
        {
            cacheTag = Cachearray[i].getTag();
            if(cacheTag == address){
                System.out.print("CacheTag is: ");
                System.out.println(cacheTag);
                Simulator.getEventQueue().addEvent(
                        new MemoryResponseEvent(
                                Clock.getCurrentTime(),
                                this,
                                requestingElement,
                                Cachearray[i].getData())
                    );
                System.out.println("Read event response added by cache");
                Cachearray[i].incrementCounter();
                break;
            }
            else if(i == num_lines - 1){
                System.out.println("Not present in  cache");
                if(Cachearray[i].getCounter() >= 0){
                    Cachearray[i].decrementCounter();
                }
                readMiss = true;
                handleCacheMiss(address, requestingElement);
            }
            else if(Cachearray[i].getCounter() >= 0){
                Cachearray[i].decrementCounter();
            }
        }
    }

    public void cacheWrite(int address, int value, Element requestingElement){
        int cacheTag;
        for(int i = 0; i < num_lines; i++)
        {
            cacheTag = Cachearray[i].getTag();
            if(cacheTag == address){
                Cachearray[i].setData(value);
                Simulator.getEventQueue().addEvent(
                        new MemoryWriteEvent(
                                Clock.getCurrentTime(),
                                this,
                                containingProcessor.getMainMemory(),
                                address,
                                value)
                        );
                Cachearray[i].incrementCounter();
                MemoryAccess MAStage = (MemoryAccess)requestingElement;
                MAStage.EX_MA_Latch.setMA_busy(false);
                MAStage.MA_RW_Latch.setRW_enable(true);
                break;
            }
            else if(i == num_lines - 1){
                readMiss = false;
                dataMissed = value;
                handleCacheMiss(address, requestingElement);
            }
        }
    }

    public void handleCacheMiss(int address, Element reqElement){
        Simulator.getEventQueue().addEvent(
                new MemoryReadEvent(
                        Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                        this,
                        containingProcessor.getMainMemory(),
                        address)
            );
        // System.out.println("Cache miss. Added to queue");
        cache_addr = address;
        cacheRequestingElement = reqElement;
    }

    @Override
    public void handleEvent(Event e) {
        
        if(e.getEventType() == Event.EventType.MemoryResponse){
            MemoryResponseEvent event = (MemoryResponseEvent) e;
            handleResponse(event.getValue());
        }
        else if(e.getEventType() == Event.EventType.MemoryRead){
            MemoryReadEvent event = (MemoryReadEvent) e;
            int addr = event.getAddressToReadFrom();
            Element req_ele = event.getRequestingElement();
            cacheRead(addr, req_ele);
        }

        else{ 
            //if e.getEventType() == Event.EventType.MemoryWrite
            MemoryWriteEvent event = (MemoryWriteEvent) e;
            int addr = event.getAddressToWriteTo();
            Element req_ele = event.getRequestingElement();
            int val = event.getValue();
            cacheWrite(addr, val, req_ele);
        }
    }
}