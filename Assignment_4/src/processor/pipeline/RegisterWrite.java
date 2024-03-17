package processor.pipeline;

import java.util.HashMap;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_OF_LatchType IF_OF_Latch = new IF_OF_LatchType();
	IF_EnableLatchType IF_EnableLatch;
	String opcode;
	int rdI;
	int instruction;
	String rd;
	// int x31;
	
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

	public static HashMap<String, String> opcodeToType = new HashMap<String, String>() {{
		put("00000", "r3");  // add
		put("00010", "r3");  // sub
		put("00100", "r3");  // mul
		put("00110", "r3");  // div
		put("01000", "r3");  // and
		put("01010", "r3");  // or
		put("01100", "r3");  // xor
		put("01110", "r3");  // slt
		put("10000", "r3");  // sll
		put("10010", "r3");  // srl
		put("10100", "r3");  // sra
		put("00001", "r2i");  // addi
		put("00011", "r2i");  // subi
		put("00101", "r2i");  // muli
		put("00111", "r2i");  // divi
		put("01001", "r2i");  // andi
		put("01011", "r2i");  // ori
		put("01101", "r2i");  // xori
		put("01111", "r2i");  // slti
		put("10001", "r2i");  // slli
		put("10011", "r2i");  // srli
		put("10101", "r2i");  // srai
		put("10110", "r2i_ldst");  // load
		put("10111", "r2i_ldst");  // store
		put("11001", "r2i_b");  // beq
		put("11010", "r2i_b");  // bne
		put("11011", "r2i_b");  // blt
		put("11100", "r2i_b");  // bgt
		put("11000", "ri");  // jmp
		put("11101", "end");	// end
	}};

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
	int ldResult;
	int aluResult;
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			if(MA_RW_Latch.getInstruction() == 0){
				System.out.println("     ************recieved bubble from MA i am in RW");
				
				MA_RW_Latch.setRW_enable(true);
				IF_OF_Latch.setOF_enable(true);
				
			}
			else{
				opcode = MA_RW_Latch.getOpcode();
				instruction = MA_RW_Latch.getInstruction();
				
				aluResult = MA_RW_Latch.getAluResult();
				ldResult = MA_RW_Latch.getLdResult();
				// System.out.print("ldresult RW=");
				// System.out.println(ldResult);

				String instString = Integer.toBinaryString(instruction);
				// instString = String.format("%032s", instString);
				while(instString.length() < 32){
					instString = "0" + instString;
				}
				System.out.println("Inst in RW");
				System.out.println(instString);
				// System.out.println(instString.length());
				System.out.print("WB: ");
				System.out.println(isWb());
				if(isWb()){
					if(opcode.equals("10110")){
						String rd = instString.substring(10, 15);
						rdI = Integer.parseInt(rd,2); 
						System.out.print("rdI is: ");
						System.out.println(rdI);
						System.out.print("ldresult before writing=");
						System.out.println(ldResult);
						containingProcessor.getRegisterFile().setValue(rdI, ldResult);
						// IF_OF_Latch.set_Stall(false);
						System.out.print("ld writing in memory:");
						System.out.println(containingProcessor.getRegisterFile().getValue(rdI));
					}
					else if(opcodeToType.get(opcode).equals("r2i")){
						String rd = instString.substring(10, 15);
						rdI = Integer.parseInt(rd,2);
						// System.out.print("rdI is: ");
						// System.out.println(rdI);
						containingProcessor.getRegisterFile().setValue(rdI, aluResult);
						// IF_OF_Latch.set_Stall(false);
						System.out.print("r2i RW=");
						System.out.println(containingProcessor.getRegisterFile().getValue(rdI));
					}
					else{
						String rd = instString.substring(15, 20);
						rdI = Integer.parseInt(rd,2); 
						System.out.print("rdI is: ");
						System.out.println(rdI);
						System.out.println("Alu result: ");
						System.out.println(aluResult);
						containingProcessor.getRegisterFile().setValue(rdI, aluResult);
						// IF_OF_Latch.set_Stall(false);
					}
					// rd = MA_RW_Latch.getRd();
					// rdI = Integer.parseInt(rd,2); 
					if(opcode.equals("00110") || opcode.equals("00111")){
					containingProcessor.getRegisterFile().setValue(31, MA_RW_Latch.getx31());
					System.out.print("x31 RW=");
					// IF_OF_Latch.set_Stall(false);
					System.out.println(containingProcessor.getRegisterFile().getValue(31));
					}
				}

				if(opcodeToType.get(opcode).equals("end")){
					Simulator.setSimulationComplete(true);
				}

				MA_RW_Latch.setRW_enable(true);
				IF_EnableLatch.setIF_enable(true);
			}

			
		}
		
	}

}
