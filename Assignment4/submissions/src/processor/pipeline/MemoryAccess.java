package processor.pipeline;

import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	int ldresult = 0;
	int rd;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO
		if(EX_MA_Latch.isMA_enable()){
			if(EX_MA_Latch.getInstruction() == 0){
				MA_RW_Latch.setInstruction(0);
			}
			else{
				int op2 = EX_MA_Latch.getOp2();
				int op1 = EX_MA_Latch.getOp1();
				rd = EX_MA_Latch.getRd();
				int aluresult = EX_MA_Latch.getAluResult();
				String opcode = EX_MA_Latch.getOpcode();
				int instruction = EX_MA_Latch.getInstruction();

				if(opcode.equals("10110")){
					ldresult = containingProcessor.getMainMemory().getWord(aluresult);
					// System.out.print("ldresult=");
					// System.out.println(ldresult);
				}
				if(opcode.equals("10111")){
					System.out.print("aluresult for store inst: ");
					System.out.println(aluresult);
					System.out.println("op2 MA=");
					System.out.println(op2);
					containingProcessor.getMainMemory().setWord(aluresult,op2);
					System.out.print("store inst MA ");
					System.out.println(containingProcessor.getMainMemory().getWord(aluresult));
				}

				EX_MA_Latch.setMA_enable(false);
				MA_RW_Latch.setRW_enable(true);
				MA_RW_Latch.setInstruction(instruction);
				MA_RW_Latch.setOpcode(opcode);
				MA_RW_Latch.setAluResult(aluresult);
				// System.out.print("ldresult befor marw set=");
				// System.out.println(ldresult);
				MA_RW_Latch.setLdResult(ldresult);
				MA_RW_Latch.setx31(EX_MA_Latch.getx31());
				MA_RW_Latch.setRd(rd);

				// System.out.println(MA_RW_Latch.getInstruction());
				// System.out.println(MA_RW_Latch.getAluResult());
				// System.out.print("ma  rw latch ldresult=");
				// System.out.println(MA_RW_Latch.getLdResult());
				// System.out.println(MA_RW_Latch.getOpcode());
			}
		}
	}
	

}
