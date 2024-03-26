package processor.pipeline;

import configuration.Configuration;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;

public class InstructionFetch {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	int instcount = 0;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	int currentPC;
	public void performIF()
	{
		
		if(IF_EnableLatch.isIF_enable() && IF_OF_Latch.closeIF == false)
		{
			// if(IF_EnableLatch.isIF_busy()){
			// 	// IF_OF_Latch.setOF_busy(true);
			// 	return;
			// }
			if(IF_EnableLatch.isIF_busy() == false){

			}

			// Simulator.getEventQueue().addEvent(
			// 	new MemoryReadEvent(
					
			// 	Clock.getCurrentTime() + Configuration.mainMemoryLatency,
			// 	 this , 
			// 	 containingProcessor.getMainMemory(),
			// 	 containingProcessor.getRegisterFile().getProgramCounter())

			// );
			

			
			// if(IF_OF_Latch.getIsBranchTaken()){
			// 	IF_OF_Latch.setInstruction(0);
			// 	IF_OF_Latch.setOF_enable(true);
			// 	IF_OF_Latch.setIsBRanchTaken(false);
			// 	IF_OF_Latch.instCount++;
			// 	Simulator.setNoofInsts(IF_OF_Latch.instCount);
			// }
			// else{
				// System.out.println(EX_IF_Latch.isIF_enable());
				System.out.println("IF is ON");
				if(EX_IF_Latch.isIF_enable()){

						// System.out.print("is branch taken?");
						// System.out.println(EX_IF_Latch.getIsBranchTaken());
						if(EX_IF_Latch.getIsBranchTaken()){
							containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.getBranchPC()); // doubt
							System.out.print("next pc set in IF:");
							currentPC = containingProcessor.getRegisterFile().getProgramCounter();
							System.out.print("currentPC=");
							System.out.println(currentPC);
							System.out.println(containingProcessor.getRegisterFile().getProgramCounter());
							EX_IF_Latch.setIsBRanchTaken(false);
						}
						// else{
						// 	if(IF_OF_Latch.is_Stall() == true){
						// 		containingProcessor.getRegisterFile().setProgramCounter(currentPC);
						// 	}
						// 	else{

						// 		containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
						// 	}
						// }		
				}
				else
				{
					currentPC = containingProcessor.getRegisterFile().getProgramCounter();
					if(IF_OF_Latch.is_Stall()){
						containingProcessor.getRegisterFile().setProgramCounter(currentPC);
						System.out.println("stalled");
					}
					else{
						
						containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
						System.out.println("not stall");
						IF_OF_Latch.setPC(currentPC);
					}
					System.out.print("currentPC=");
					System.out.println(currentPC);
				}
					// containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
				// instcount++;
				// Simulator.setNoofInsts(instcount);
				IF_OF_Latch.instCount++;
				Simulator.setNoofInsts(IF_OF_Latch.instCount);
				// currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				// System.out.println(currentPC);
				int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
				System.out.print("the instruction in IF is ");
				System.out.println(newInstruction);

				IF_OF_Latch.setInstruction(newInstruction); // pc to be stored in latch or not?
				// System.out.println(IF_OF_Latch.getInstruction());
				
				
				
				// IF_EnableLatch.setIF_enable(false);
				IF_OF_Latch.setOF_enable(true);
				EX_IF_Latch.setIF_enable(false); // doubt
			// }
		}
	}
	// @Override
	// public void handleEvent(Event e){
	// 	if(IF_OF_Latch.isOF_busy()){
	// 		e.setEventTime(Clock.getCurrentTime() + 1);
	// 		Simulator.getEventQueue().addEvent(e);
	// 	}
	// 	else{
	// 		MemoryResponseEvent event = (MemoryResponseEvent) e;
	// 		IF_OF_Latch.setInstruction(event.getValue());
	// 	}
	// }

}
