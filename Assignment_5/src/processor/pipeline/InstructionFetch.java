package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
// import generic.*;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;

public class InstructionFetch implements Element {
	
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
			System.out.println("IF is ON");
			if(IF_EnableLatch.isIF_busy() == false){
				System.out.println("IF is not busy");
				// IF_OF_Latch.setOF_enable(false);
				if(IF_OF_Latch.getIsBranchTaken()){
					IF_OF_Latch.setInstruction(0);
					// IF_OF_Latch.setOF_enable(false);
					IF_OF_Latch.setIsBRanchTaken(false);
					IF_OF_Latch.instCount++;
					Simulator.setNoofInsts(IF_OF_Latch.instCount);
		
					IF_EnableLatch.setIF_busy(false);
					IF_OF_Latch.setIF_busy(false);
					IF_OF_Latch.setOF_enable(true);
				}
				else
				if(EX_IF_Latch.isIF_enable()){

						// System.out.print("is branch taken?");
						// System.out.println(EX_IF_Latch.getIsBranchTaken());
						if(EX_IF_Latch.getIsBranchTaken()){
							containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.getBranchPC()); // doubt
							// System.out.print("next pc set in IF:");
							currentPC = containingProcessor.getRegisterFile().getProgramCounter();
							System.out.print("currentPC=");
							System.out.println(currentPC);
							System.out.println(containingProcessor.getRegisterFile().getProgramCounter());
							EX_IF_Latch.setIsBRanchTaken(false);
						}
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
				IF_OF_Latch.instCount++;
				Simulator.setNoofInsts(IF_OF_Latch.instCount);

				Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
								Clock.getCurrentTime() + Configuration.mainMemoryLatency,
								this,
								containingProcessor.getMainMemory(),
								currentPC)
				);
				IF_EnableLatch.setIF_busy(true);
				IF_OF_Latch.setIF_busy(true);
				IF_OF_Latch.setOF_enable(false);
				// IF_OF_Latch.setOF_enable(true);
				// EX_IF_Latch.setIF_enable(false); 
			}

			
				
				// currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				// System.out.println(currentPC);
				// int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
				// System.out.print("the instruction in IF is ");
				// System.out.println(newInstruction);

				// IF_OF_Latch.setInstruction(newInstruction); // pc to be stored in latch or not?
				// System.out.println(IF_OF_Latch.getInstruction());
				
				
				
				// IF_EnableLatch.setIF_enable(false);
				// doubt
			// }
		}
		else{
			// Simulator.setSimulationComplete(true);
			// IF_OF_Latch.setInstruction(0);
			// IF_OF_Latch.setPC(currentPC);
			IF_OF_Latch.setOF_enable(true);
		}
	}
	@Override
	public void handleEvent(Event e) {
		// if(IF_OF_Latch.getIsBranchTaken()){
		// 	IF_OF_Latch.setInstruction(0);
		// 	// IF_OF_Latch.setOF_enable(false);
		// 	IF_OF_Latch.setIsBRanchTaken(false);
		// 	IF_OF_Latch.instCount++;
		// 	Simulator.setNoofInsts(IF_OF_Latch.instCount);

		// 	IF_EnableLatch.setIF_busy(false);
		// 	IF_OF_Latch.setIF_busy(false);
		// 	IF_OF_Latch.setOF_enable(true);
		// }
		 if(IF_OF_Latch.isOF_busy()) {
			System.out.println("IF_OF Latch is Busy");
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		}
		else {
			
			// else{
			MemoryResponseEvent event = (MemoryResponseEvent) e ;
			System.out.println("IF Event Handled");
			IF_OF_Latch.setInstruction(event.getValue());

			IF_EnableLatch.setIF_busy(false);
			IF_OF_Latch.setIF_busy(false);
			IF_OF_Latch.setOF_enable(true);
			// }
		}
	}
	// if (e.getEventType() == Event.EventType.MemoryResponse)
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
