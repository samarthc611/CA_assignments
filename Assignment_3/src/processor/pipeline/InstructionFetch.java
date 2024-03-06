package processor.pipeline;

import generic.Simulator;
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
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable())
		{
			
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			System.out.println(currentPC);
			if(EX_IF_Latch.isIF_enable() && instcount != 0){
				
				if(EX_IF_Latch.getIsBranchTaken()){
					containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.getBranchPC()); // doubt
					EX_IF_Latch.setIsBRanchTaken(false);
				}
				else
					containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
			}
			instcount++;
			Simulator.setNoofInsts(instcount);
			currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			// System.out.println(currentPC);
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			// System.out.print("the instruction is ");
			// System.out.println(newInstruction);
			IF_OF_Latch.setInstruction(newInstruction); // pc to be stored in latch or not?
		    // System.out.println(IF_OF_Latch.getInstruction());
			IF_OF_Latch.setPC(currentPC);
			
			IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);
			EX_IF_Latch.setIF_enable(false); // doubt
		}
	}

}
