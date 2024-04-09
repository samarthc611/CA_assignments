package processor.pipeline;

import java.util.HashMap;

import processor.Clock;
import processor.Processor;
import processor.pipeline.OF_EX_LatchType;
public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	// IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	MA_RW_LatchType MA_RW_Latch;
	static String opcode;
	static boolean isBranchTaken = false;
	static boolean isimm = false;
	int x31 = 0;
	int rd,rdI,instruction;
	// String OPcode = OF_EX_Latch.getOpcode();

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
		put("11101", "end");  // end
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

	private static boolean isImmediate(String opcode){

    	String type = opcodeToType.get(opcode);
		
		if(type == null)
		{
			System.out.println("type is null");
		}
    	if (type != null && (type.equals("r2i") || type.equals("r2i_ldst"))) {
			// if(type.equals("r2i_b"))
			// 	isimm = false;
			// else
        		isimm = true;
    	}
		else
			isimm = false;
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

	int aluresult;

	public void performEX()
	{
		// System.out.println("Is MA busy?(as per EX):");
		// System.out.println(EX_MA_Latch.isMA_busy());
		// System.out.println("Is EX busy?:");
		// System.out.println(EX_MA_Latch.isEX_busy());

		if(EX_MA_Latch.isEX_busy() == false){
			OF_EX_Latch.setOF_busy(false);
		}
		else{
			OF_EX_Latch.setOF_busy(true);
		}
		//TODO
		// System.out.println("skbdi");
		// System.out.println(OF_EX_Latch.isEX_enable());
		// System.out.println("test1 :");
		if(OF_EX_Latch.isEX_enable()){
			System.out.println("EX is ON");
			OF_EX_Latch.setEX_busy(EX_MA_Latch.isEX_busy());
			if(EX_MA_Latch.isEX_busy() == false){
			// 	OF_EX_Latch.setOF_busy(false);
			if(OF_EX_Latch.getInstruction() == 0){
				
				EX_MA_Latch.setInstruction(0);
				// int temp1 = OF_EX_Latch.getRd();
				// System.out.println("temp1 :");
				// System.out.println(temp1);
				// rd = OF_EX_Latch.getRd();
				EX_MA_Latch.setMA_enable(true);
				// EX_MA_Latch.setRd(rd);

				// System.out.println(EX_MA_Latch.getRd());
				// OF_EX_Latch.setRd(40);
				// System.out.println(OF_EX_Latch.getRd());
				System.out.println("     ************recieved bubble from OF i am in Ex");
				
				OF_EX_Latch.setEX_enable(false);
				// IF_OF_Latch.setOF_enable(true);
			}
			else{
				// if(EX_MA_Latch.getIsBranchTaken()){
				// 	System.out.println("test2 :");
				// 	EX_MA_Latch.setInstruction(0);
				// 	EX_MA_Latch.setMA_enable(true);
				// 	OF_EX_Latch.setEX_enable(false);
				// 	EX_MA_Latch.setIsBRanchTaken(false);
				// }
				// else{
					// instruction = OF_EX_Latch.getInstruction();
					// String instString = Integer.toBinaryString(instruction);
					// String temp = "";
					// int i = instString.length();
					// System.out.print("i is: ");
					// System.out.println(i);
					// while(i < 32){
					// 	temp+="0";
					// 	i++;
					// }
					// temp+=instString;
					// instString = temp;
					// String rd;
					String opcode = OF_EX_Latch.getOpcode();
					// if(opcode.equals("10110")){
					// 	rd = instString.substring(10, 15);
					// 	rdI = Integer.parseUnsignedInt(rd, 2);
					// }
					// else if(opcodeToType.get(opcode).equals("r2i")){
					// 	rd = instString.substring(10, 15);
					// 	rdI = Integer.parseUnsignedInt(rd, 2);
					// }
					// else{
					// 	rd = instString.substring(15, 20);
					// 	rdI = Integer.parseUnsignedInt(rd, 2);
					// }

					
					
					
					// System.out.print("opcode in EX=");
					// System.out.println(opcode);
					int imm = OF_EX_Latch.getImmx();
					int branchtarget = OF_EX_Latch.getBranchTarget();
					int op1 = OF_EX_Latch.getOp1();
					int op2 = OF_EX_Latch.getOp2();
					
					
					rd = OF_EX_Latch.getRd();
					System.out.println("rd in EX is :");
					System.out.println(rd);
					// EX_MA_Latch.setRd(rdI);
					EX_MA_Latch.setRd(rd);
					System.out.println(EX_MA_Latch.getRd());
					
					System.out.print("op1 & op2 EX=");
					System.out.println(op1);
					System.out.println(op2);
					int instruction = OF_EX_Latch.getInstruction();

					System.out.print("EX isImm=");
					System.out.println(isImmediate(opcode));
					
					if(opcode.equals("10111")){
						aluresult = ALU(op1, imm);
					}
					if(isImmediate(opcode) && opcode.equals("10111")== false){
						
						op2 = imm;
					}
					if(opcode.equals("10111") == false){
						aluresult = ALU(op1, op2);
					}
					System.out.print("ALU result EX=");
					System.out.println(aluresult);
					isBranchTaken = false;
					checkBranchtaken(op1, op2);
					System.out.print("isbranchTaken=");
					System.out.println(isBranchTaken);
					OF_EX_Latch.setIsBRanchTaken(isBranchTaken);

					OF_EX_Latch.setEX_enable(false);
					EX_MA_Latch.setMA_enable(true);
					if(isBranchTaken)
						EX_IF_Latch.setIF_enable(true);
					else
						EX_IF_Latch.setIF_enable(false);
					// System.out.print("is if enabled by EX::");
					// System.out.println(EX_IF_Latch.isIF_enable());
					// System.out.print("branchTarget EX=");
					// System.out.println(branchtarget);


					System.out.println("pc in ex:");
					// System.out.println(containingProcessor.getRegisterFile().getProgramCounter());
					System.out.println(OF_EX_Latch.getPC());
				
					EX_IF_Latch.setBranchPC(branchtarget);
					OF_EX_Latch.setBranchPC(branchtarget);
					EX_IF_Latch.setIsBRanchTaken(isBranchTaken);
					
					System.out.println("branchtarget :");
					System.out.println(branchtarget);

					EX_MA_Latch.setOp1(op1);
					EX_MA_Latch.setOp2(op2);
					EX_MA_Latch.setAluResult(aluresult);
					EX_MA_Latch.setOpcode(opcode);
					System.out.println("putting below inst in EX_MA latch");
					System.out.println(instruction);
					EX_MA_Latch.setInstruction(instruction);
					EX_MA_Latch.setx31(x31);
					// EX_MA_Latch.setRd(rd);


					EX_MA_Latch.setIsBRanchTaken(isBranchTaken);
					OF_EX_Latch.setIsBRanchTaken(isBranchTaken);
					// System.out.println("Instruction is");
					// System.out.println(EX_MA_Latch.getInstruction());
					// System.out.println(EX_MA_Latch.getOpcode());
					// System.out.println(EX_MA_Latch.getAluResult());
					// System.out.println(EX_MA_Latch.getOp2());
				// }
			}

		}
		else{
			OF_EX_Latch.setOF_busy(true);
		}
		}
	}

}
