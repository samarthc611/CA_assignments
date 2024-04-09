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
	int count = 0;
	int samePC = 0;
	public void performIF()
	{
		
		if(IF_EnableLatch.isIF_enable() && IF_OF_Latch.closeIF == false)
		{
			System.out.println("IF is ON");
			// if(IF_OF_Latch.getIsBranchTaken()==true){
			// 	Simulator.getEventQueue().addEvent(
			// 			new MemoryReadEvent(
			// 					Clock.getCurrentTime() - 2,
			// 					this,
			// 					containingProcessor.getMainMemory(),
			// 					65535)
			// 			);
			// 	IF_OF_Latch.setIsBRanchTaken(false);
			// }
			if(IF_EnableLatch.isIF_busy() == false){
				System.out.println("IF is not busy");
				IF_OF_Latch.setOF_enable(false);
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
				// else
				if(EX_IF_Latch.isIF_enable()){

						// System.out.print("is branch taken?");
						// System.out.println(EX_IF_Latch.getIsBranchTaken());
						if(EX_IF_Latch.getIsBranchTaken()){
							containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.getBranchPC()); // doubt
							// System.out.print("next pc set in IF:");
							currentPC = containingProcessor.getRegisterFile().getProgramCounter();
							System.out.print("currentPC(EX enabled IF)=");
							System.out.println(currentPC);
							System.out.println(containingProcessor.getRegisterFile().getProgramCounter());
							EX_IF_Latch.setIsBRanchTaken(false);
							samePC = 1;
						}

						Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
								Clock.getCurrentTime() + containingProcessor.getL1iCache().getCacheLatency() + 4,
								this,
								containingProcessor.getMainMemory(),
								currentPC)
						);
				}
				else
				{
					currentPC = containingProcessor.getRegisterFile().getProgramCounter();
					if(IF_OF_Latch.is_Stall()){
						containingProcessor.getRegisterFile().setProgramCounter(currentPC);
						System.out.println("stalled");
						// count++;
					}
					else{
						// count = 0;
						samePC = 0;
						containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
						System.out.println("not stall");
						// IF_OF_Latch.setPC(currentPC);
						// Simulator.getEventQueue().addEvent(
						// new MemoryReadEvent(
						// 		Clock.getCurrentTime() + Configuration.mainMemoryLatency,
						// 		this,
						// 		containingProcessor.getMainMemory(),
						// 		currentPC)
						// );
					}
					System.out.print("currentPC=");
					System.out.println(currentPC);


					Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
								Clock.getCurrentTime() + Configuration.mainMemoryLatency,
								this,
								containingProcessor.getMainMemory(),
								currentPC)
					);
					
				}
				IF_OF_Latch.instCount++;
				Simulator.setNoofInsts(IF_OF_Latch.instCount);

				// for(int i = 0; i < Simulator.getEventQueue().getSize(); i++)
				// {
					// if(Simulator.getEventQueue().peek())
				// }
				// if(count <= 1)
				// {
				// Simulator.getEventQueue().addEvent(
				// 		new MemoryReadEvent(
				// 				Clock.getCurrentTime() + Configuration.mainMemoryLatency,
				// 				this,
				// 				containingProcessor.getMainMemory(),
				// 				currentPC)
				// );
				// count++;
				// }
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
			IF_EnableLatch.setIF_busy(false);
			IF_OF_Latch.setIF_busy(false);
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
			// MemoryReadEvent event_temp = (MemoryReadEvent) e;
			// int pc = event_temp.getAddressToReadFrom();
			MemoryResponseEvent event = (MemoryResponseEvent) e ;
			System.out.println("IF Event Handled");
			IF_OF_Latch.setInstruction(event.getValue());
			// System.out.println(event.g);
			if(samePC == 0)
				IF_OF_Latch.setPC(containingProcessor.getRegisterFile().getProgramCounter() - 1);
			else if(samePC == 1)
			{
				IF_OF_Latch.setPC(containingProcessor.getRegisterFile().getProgramCounter());
				samePC = 0;
			}

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
