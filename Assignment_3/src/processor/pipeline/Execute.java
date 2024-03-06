package processor.pipeline;

import java.util.HashMap;

import processor.Processor;
import processor.pipeline.OF_EX_LatchType;
public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	static String opcode;
	static boolean isBranchTaken = false;
	static boolean isimm = false;

	// String OPcode = OF_EX_Latch.getOpcode();
	int x31 = 0;

	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
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


	private static boolean isImmediate()
	{
		if(getType.get(opcode)=="r2i" || getType.get(opcode)=="r2i_ldst"){
			isimm = true;
		}
		return isimm;
		
	}
	// String opCode = OF_EX_Latch.getOpcode();
	private void checkBranchtaken(int operand1,int operand2)
	{
		if ((OF_EX_Latch.getOpcode().equals("11001") && operand1 == operand2) ||
			(OF_EX_Latch.getOpcode().equals("11010") && operand1 != operand2) ||
			(OF_EX_Latch.getOpcode().equals("11011") && operand1 < operand2)  ||
			(OF_EX_Latch.getOpcode().equals("11100") && operand1 > operand2)  ||
			(OF_EX_Latch.getOpcode().equals("11000"))){
				isBranchTaken = true;
			}
				
	}

	
	
	private int ALU(int operand1, int operand2)
	{
		int aluResult = 0;
		// System.out.println(OF_EX_Latch.getOpcode());
		if(OF_EX_Latch.getOpcode().equals("00001") || OF_EX_Latch.getOpcode().equals("00000") || OF_EX_Latch.getOpcode().equals("10110") || OF_EX_Latch.getOpcode().equals("10111")){
			aluResult = operand1 + operand2;
		}
		else if(OF_EX_Latch.getOpcode().equals("00011") || OF_EX_Latch.getOpcode().equals("00010")){
			aluResult = operand1 - operand2;
		}
		else if(OF_EX_Latch.getOpcode().equals("00101") || OF_EX_Latch.getOpcode().equals("00100")){
			aluResult = operand1 * operand2;
		}
		else if(OF_EX_Latch.getOpcode().equals("00111") || OF_EX_Latch.getOpcode().equals("00110")){
			aluResult = operand1 / operand2;
			x31 = operand1 % operand2;
		}
		else if(OF_EX_Latch.getOpcode().equals("01001") || OF_EX_Latch.getOpcode().equals("01000")){
			aluResult = operand1 & operand2;
		}
		else if(OF_EX_Latch.getOpcode().equals("01011") || OF_EX_Latch.getOpcode().equals("01010")){
			aluResult = operand1 | operand2;
		}
		else if(OF_EX_Latch.getOpcode().equals("01101") || OF_EX_Latch.getOpcode().equals("01100")){
			aluResult = operand1 ^ operand2;
		}
		else if(OF_EX_Latch.getOpcode().equals("10001") || OF_EX_Latch.getOpcode().equals("10000")){
			aluResult = operand1 << operand2;
		}
		else if(OF_EX_Latch.getOpcode().equals("10011") || OF_EX_Latch.getOpcode().equals("10010")){
			aluResult = operand1 >>> operand2;
		}
		else if(OF_EX_Latch.getOpcode().equals("10101") || OF_EX_Latch.getOpcode().equals("10100")){
			aluResult = operand1 >> operand2;
		}
		// else if(OPcode.equals("10111")){
		// 	aluResult = operand1 + operand2;
		// }
		return aluResult;
	}

	

	public void performEX()
	{
		//TODO
		String opcode = OF_EX_Latch.getOpcode();
		// System.out.println(opcode);
		int imm = OF_EX_Latch.getImmx();
		int branchtarget = OF_EX_Latch.getBranchTarget();
		int op1 = OF_EX_Latch.getOp1();
		int op2 = OF_EX_Latch.getOp2();
		int instruction = OF_EX_Latch.getInstruction();

		if(isImmediate()){
			op2 = imm;
		}
		int aluresult = ALU(op1, op2);
		checkBranchtaken(op1, op2);

		OF_EX_Latch.setEX_enable(false);
		EX_MA_Latch.setMA_enable(true);
		EX_IF_Latch.setIF_enable(false);

		EX_IF_Latch.setBranchPC(branchtarget);
		EX_IF_Latch.setIsBRanchTaken(isBranchTaken);

		EX_MA_Latch.setOp2(op2);
		EX_MA_Latch.setAluResult(aluresult);
		EX_MA_Latch.setOpcode(opcode);
		EX_MA_Latch.setInstruction(instruction);
	}

}
