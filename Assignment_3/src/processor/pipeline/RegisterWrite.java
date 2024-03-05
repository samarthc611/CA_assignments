package processor.pipeline;

import java.util.HashMap;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	String opcode;
	int rdI;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	private boolean isWb(){
		if(opcode.equals("10111") || opcode.equals("11001") || opcode.equals("11010") || opcode.equals("11011") || opcode.equals("11100") || opcode.equals("11000") || opcode.equals("11101") ) {
			return false;
		}
		return true;
	}

	public static HashMap<String,String> getType = new HashMap<String, String>(){{
		put("add","r3");
		put("sub","r3");
		put("mul","r3");
		put("div","r3");
		put("and","r3");
		put("or","r3");
		put("xor","r3");
		put("slt","r3");
		put("sll","r3");
		put("srl","r3");
		put("sra","r3");
		put("addi","r2i");
		put("subi","r2i");
		put("muli","r2i");
		put("divi","r2i");
		put("andi","r2i");
		put("ori","r2i");
		put("xori","r2i");
		put("slti","r2i");
		put("slli","r2i");
		put("srli","r2i");
		put("srai","r2i");
		put("load","r2i_ldst");
		put("store","r2i_ldst");
		put("beq","r2i_b");
		put("bgt","r2i_b");
		put("bne","r2i_b");
		put("blt","r2i_b");
		put("jmp","ri");
		put("end","end");
	}};

	public static HashMap<String,String> opTable = new HashMap<String, String>(){{
		put("add", "00000");
		put("sub", "00010");
		put("mul", "00100");
		put("div", "00110");
		put("and", "01000");
		put("or", "01010");
		put("xor", "01100");
		put("slt", "01110");
		put("sll", "10000");
		put("srl", "10010");
		put("sra", "10100");
		put("addi", "00001");
		put("subi", "00011");
		put("muli", "00101");
		put("divi", "00111");
		put("andi", "01001");
		put("ori", "01011");
		put("xori", "01101");
		put("slti", "01111");
		put("slli", "10001");
		put("srli", "10011");
		put("srai", "10101");
		put("load", "10110");
		put("store", "10111");
		put("beq", "11001");
		put("bne", "11010");
		put("blt", "11011");
		put("bgt", "11100");
		put("jmp", "11000");
		put("end","11101");

	}};
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{

			opcode = MA_RW_Latch.getOpcode();
			int instruction = MA_RW_Latch.getInstruction();
			int aluResult = MA_RW_Latch.getAluResult();
			int ldResult = MA_RW_Latch.getLdResult();

			String instString = Integer.toBinaryString(instruction);

			if(isWb()){
				if(opcode.equals("10110")){
					String rd = instString.substring(10, 15);
					rdI = Integer.parseInt(rd,2); 
					containingProcessor.getRegisterFile().setValue(rdI, ldResult);
				}
				else if(getType.get(opcode) == "r2i"){
					String rd = instString.substring(10, 15);
					rdI = Integer.parseInt(rd,2); 
					containingProcessor.getRegisterFile().setValue(rdI, aluResult);
				}
				else{
					String rd = instString.substring(15, 20);
					rdI = Integer.parseInt(rd,2); 
					containingProcessor.getRegisterFile().setValue(rdI, aluResult);
				}
			}

			if(getType.get(opcode) == "end"){
				Simulator.setSimulationComplete(true);
			}

			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);

			
		}
	}

}
