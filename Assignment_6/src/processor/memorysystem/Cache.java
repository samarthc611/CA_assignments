package processor.memorysystem;
import configuration.Configuration;
import generic.*;
import processor.Clock;
import processor.Processor;
import processor.pipeline.MemoryAccess;
import processor.pipeline.*;

public class Cache implements Element {
    Processor containingProcessor;
    int cacheSize;
    int cacheLatency;

    int cache_addr;
    Element cacheRequestingElement;
    Boolean readMiss;
    int writeData;

    //array of CacheLine initiated
    CacheLine[] Cachearray;
    int num_lines;
    int num_sets;

    public Cache(Processor processor, int size){
        this.containingProcessor = processor;
        this.cacheSize = size;
        this.num_lines = size / 4;
        //Associativity = 2
        //hence no. of sets = no of line / associativity
        // this.num_sets = num_lines / 2;
        // this.num_sets = 1; //remove afterwards

        // switch(size){
        //     case 16:
        //         cacheLatency = 1;
        //         break;
        //     case 128:
        //         cacheLatency = 2;
        //         break;
        //     case 512:
        //         cacheLatency = 3;
        //         break;
        //     case 1024:
        //         cacheLatency = 4;
        //         break;
        // }
        if(size == 1024){
            cacheLatency = 4;
        }
        else if(size == 512){
            cacheLatency = 3;
        }
        else if(size == 128){
            cacheLatency = 2;
        }
        else if(size == 16){
            cacheLatency = 1;
        }
        Cachearray = new CacheLine[num_lines];
        for(int i = 0; i < num_lines; i++){
            Cachearray[i] = new CacheLine();
        }
    }


    // public static String toBinary(int x, int len){
    //     if (len > 0) {
    //         return String.format("%" + len + "s",
    //                 Integer.toBinaryString(x)).replace(" ", "0");
    //     }
    //     return null;
    // }

    public void handleCacheMiss(int address, Element requestingElement){
        Simulator.getEventQueue().addEvent(
                new MemoryReadEvent(
                        Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                        this,
                        containingProcessor.getMainMemory(),
                        address
                )
        );
        cache_addr = address;
        cacheRequestingElement = requestingElement;
    }

    public void handleResponse(int value){
        // String addressString = toBinary(cache_addr, 32); //Replaced
        // String addressString = Integer.toBinaryString(cache_addr); // change variable name
        // while(addressString.length() != 32){
        //     addressString = '0' + addressString;
        // }
        // int indexBits = (int) (Math.log(num_lines) / Math.log(2));
        // // int cacheAddress;
        // if(indexBits == 0){
        //     cacheAddress = 0;
        // }
        // else{
        //     // assert addressString != null;
        //     cacheAddress = Integer.parseInt(addressString.substring((32 - indexBits), 32), 2);
        // }
        int min = 0;
        for(int i = 0; i < num_lines; i++){
            if (Cachearray[i].getCounter() < Cachearray[i].getCounter()){
                min = i;
            }
        }
        Cachearray[min].setData(value);
        Cachearray[min].setTag(cache_addr);
        if(readMiss){
            Simulator.getEventQueue().addEvent(
                    new MemoryResponseEvent(
                            Clock.getCurrentTime(),
                            this,
                            cacheRequestingElement,
                            value
                    )
            );
        }
        else{
            // cacheWrite(cache_addr, writeData, cacheRequestingElement);
            // Simulator.getEventQueue().addEvent(
            //             new MemoryWriteEvent(
            //                     Clock.getCurrentTime(),
            //                     this,
            //                     containingProcessor.getMainMemory(),
            //                     cache_addr,
            //                     writeData
            //             )
            //     );
            containingProcessor.getMainMemory().setWord(cache_addr, writeData);
                // ((MemoryAccess)requestingElement).EX_MA_Latch.setMA_busy(false);
                // ((MemoryAccess)requestingElement).MA_RW_Latch.setRW_enable(true);
        }
    }
    //the function as specified in tips of Assignment-6
    public void cacheRead(int address, Element requestingElement){
        // String addressString = toBinary(address, 32);
        String addressString = Integer.toBinaryString(cache_addr); // change variable name
        while(addressString.length() != 32){
            addressString = '0' + addressString;
        }
        // int addressTag = Integer.parseInt(addressString.substring(0, 26));
        int cacheTag;
        for(int i = 0; i < num_lines; i++)
        {
            cacheTag = Cachearray[i].getTag();
            if(cacheTag == address){
                Simulator.getEventQueue().addEvent(
                        new MemoryResponseEvent(
                                Clock.getCurrentTime(),
                                this,
                                requestingElement,
                                Cachearray[i].getData()
                        )
                );
                Cachearray[i].incrementCounter();
                break;
            }
            else if(i == num_lines - 1){
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
        // int cacheTag = Cachearray[cacheAddress].getTag();
        
    }

    public void cacheWrite(int address, int value, Element requestingElement){
        String addressString = Integer.toBinaryString(address); // change variable name
        while(addressString.length() != 32){
            addressString = '0' + addressString;
        }
        // int addressTag = Integer.parseInt(addressString.substring(0, 26));
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
                                value
                        )
                );
                // Simulator.setNoofInsts(Simulator.getNoofInsts() - 4);
                Cachearray[i].incrementCounter();
                ((MemoryAccess)requestingElement).EX_MA_Latch.setMA_busy(false);
                ((MemoryAccess)requestingElement).MA_RW_Latch.setRW_enable(true);
                break;
            }
            else if(i == num_lines - 1){
                readMiss = false;
                writeData = value;
                handleCacheMiss(address, requestingElement);
            }
        }
    }

    public int getCacheLatency() {
        return cacheLatency;
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
            cacheRead(addr, req_ele); //change name
        }

        else if(e.getEventType() == Event.EventType.MemoryWrite){
            MemoryWriteEvent event = (MemoryWriteEvent) e;
            int addr = event.getAddressToWriteTo();
            Element req_ele = event.getRequestingElement();
            int val = event.getValue();
            cacheWrite(addr, val, req_ele); //change name
        }
    }
}