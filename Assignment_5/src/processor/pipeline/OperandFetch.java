package processor.pipeline;

import java.util.HashMap;
import processor.Clock;
import configuration.*;
import generic.MemoryReadEvent;
import generic.Simulator;
import processor.Processor;
import processor.*;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	int instruction;
	int branchTarget;
	int op1;

	int op2,rdI;
	

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


	public static int convertToInt(String binaryString) {
        // Check if the binary number is negative
        if (binaryString.charAt(0) == '1') {
            // Compute the two's complement to get the absolute value
            String twosComplement = computeTwosComplement(binaryString);
            // Convert the absolute value to integer and negate it
            return -1 * Integer.parseInt(twosComplement, 2);
        } else {
            // Convert the positive binary number to integer
            return Integer.parseInt(binaryString, 2);
        }
    }

    // Function to compute the two's complement of a binary string
    private static String computeTwosComplement(String binaryString) {
        StringBuilder result = new StringBuilder();
        boolean carry = true;
        // Invert the bits
        for (int i = binaryString.length() - 1; i >= 0; i--) {
            char bit = binaryString.charAt(i);
            result.insert(0, (bit == '0') ? '1' : '0');
        }
        // Add 1 to the inverted bits
        for (int i = result.length() - 1; i >= 0; i--) {
            char bit = result.charAt(i);
            if (carry) {
                if (bit == '0') {
                    result.setCharAt(i, '1');
                    carry = false;
                } else {
                    result.setCharAt(i, '0');
                }
            }
			
        }
        return result.toString();
    }

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performOF()
	{
		// if(IF_OF_Latch.isOF_busy() == false)
		// {
		// 	if(IF_OF_Latch.isIF_busy())
		// 	{
		// 		System.out.println("IF is busy, OF is free");
		// 		OF_EX_Latch.setEX_enable(false);
		// 		// ()
		// 	}
		// }
		// else 
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			// if(OF_EX_Latch.getIsBranchTaken()){ //unable to understand this part of code. comments daalne chiye the :(
			// 	// OF_EX_Latch.setInstruction(0);
			// 	IF_OF_Latch.setIsBRanchTaken(true);
			// 	IF_OF_Latch.setOF_enable(false);
			// 	OF_EX_Latch.setEX_enable(true);
			// 	OF_EX_Latch.setIsBRanchTaken(false);
			// }
			// else{
				if(IF_OF_Latch.getInstruction() == 0 && (IF_OF_Latch.is_Stall()==false)){
					OF_EX_Latch.setInstruction(0);
					OF_EX_Latch.setEX_enable(true);
					System.out.println("     ************recieved bubble from IF i am in OF");
				
					IF_OF_Latch.setOF_enable(false);
				}
				else{
					if(IF_OF_Latch.is_Stall()==false){

						instruction = IF_OF_Latch.getInstruction();
						OF_EX_Latch.setInstruction(instruction);
						System.out.println("got new INST**");
					}
					// System.out.println(instruction);
					String instString = Integer.toBinaryString(instruction);
					// System.out.println(instString);
					String temp = "";
					int i = instString.length();
					// System.out.print("i is: ");
					// System.out.println(i);
					while(i < 32){
						temp+="0";
						i++;
					}
					// for(int i = instString.length(); i<32;i++)
					// {
					// 	temp+="0";
					// }
					temp+=instString;
					instString = temp;
					// while(instString.length() < 32){
						
					// 	instString = "0" + instString;
					// }
					// instString = String.format("%032d",Long.parseLong(instString));
					System.out.print("instString OF=");
					System.out.println(instString); //working correct
					String opcode = instString.substring(0, 5);

					if(opcode.equals("11000")||
						opcode.equals("11001")||
						opcode.equals("11010")||
						opcode.equals("11011")||
						opcode.equals("11100")){
							IF_OF_Latch.setIsBRanchTaken(true); //misnomer alert. it should be set is branch instruction? then put 1 bubble
							
					
					}
					String immstr = instString.substring(15, 32);
					// if(getType.get(opcode).equals("r2i") || getType.get(opcode).equals("r2i_ldst") || getType.get(opcode).equals("r2i_b")){
					// 	immstr = instString.substring(15, 32);
					// }
					// else{
					// 	immstr = instString.substring(15, 32);
					// }
					String destImm = instString.substring(10, 32);
					int immx = convertToInt(immstr);
					// System.out.println(opcode);
					// System.out.print("immstr OF=");
					// System.out.println(immstr);
					// System.out.print("immx OF=");
					// System.out.println(immx);


					// int pc = containingProcessor.getRegisterFile().getProgramCounter();
					int pc = IF_OF_Latch.getPC();

					if (opcode.equals("11000")) {
						branchTarget = convertToInt(destImm) + pc;
					}
					else{
						branchTarget = immx + pc ;
						// System.out.print("branchTarget OF=");
						// System.out.println(branchTarget);
					}
					
					System.out.println("Immx : pc");
					System.out.println(immx);
					System.out.println(pc);
						
					String rs1,rs2;
					if(opcode.equals("10111")){
						rs1 = instString.substring(10,15);
						rs2 = instString.substring(5,10);
					}
					else{
						rs1 = instString.substring(5,10);
						rs2 = instString.substring(10,15);
					}

					// System.out.print("rs1 and rs2 binary are ");
					// System.out.println(rs1);
					// System.out.println(rs2);


					int rs1_val = Integer.parseUnsignedInt(rs1, 2);  
					int rs2_val = Integer.parseUnsignedInt(rs2, 2);
					System.out.print("rs1 and rs2 are: ");
					System.out.print(rs1_val);
					System.out.println(", ");
					System.out.println(rs2_val);

					String rd;
					if(opcode.equals("10110")){
						rd = instString.substring(10, 15);
						rdI = Integer.parseUnsignedInt(rd, 2);
					}
					else if(opcodeToType.get(opcode).equals("r2i")){
						rd = instString.substring(10, 15);
						rdI = Integer.parseUnsignedInt(rd, 2);
					}
					else{
						rd = instString.substring(15, 20);
						rdI = Integer.parseUnsignedInt(rd, 2);
					}
					System.out.print("rd : ");
					System.out.println(rdI);
					// OF_EX_Latch.setEX_enable(true);
					if(Clock.getCurrentTime() <= Configuration.mainMemoryLatency + 1){
						// OF_EX_Latch = new OF_EX_LatchType();
						// EX_MA_Latch = new EX_MA_LatchType();
						// MA_RW_Latch = new MA_RW_LatchType();
						// OF_EX_Latch.setRd(40);
						// EX_MA_Latch.setRd(90);
						// MA_RW_Latch.setRd(40);
						OF_EX_Latch.setRW_rd(40);
						OF_EX_Latch.setMA_rd(90);
					}
					if(Clock.getCurrentTime() <= Configuration.mainMemoryLatency + 2){
						// EX_MA_Latch = new EX_MA_LatchType();
						// MA_RW_Latch = new MA_RW_LatchType();
						// EX_MA_Latch.setRd(40);
						// MA_RW_Latch.setRd(40);
						OF_EX_Latch.setRW_rd(40);
					}
					// if(Clock.getCurrentTime() == 3){
					// 	MA_RW_Latch = new MA_RW_LatchType();
					// 	MA_RW_Latch.setRd(40);
					// }
					// System.out.println(OF_EX_Latch.getRd());
					// System.out.println(EX_MA_Latch.getRd());
					// System.out.println(MA_RW_Latch.getRd());
					
					System.out.println("EX & RD from of ex latch");
					System.out.println(OF_EX_Latch.getEX_rd());
					System.out.println(OF_EX_Latch.getMA_rd());
					System.out.println(OF_EX_Latch.getRW_rd());
					System.out.println("OPcode in Ex : MA : RW");
					System.out.println(OF_EX_Latch.getEX_op());
					System.out.println(OF_EX_Latch.getMA_op());
					System.out.println(OF_EX_Latch.getRW_op());
					if(OF_EX_Latch.getEX_op() != null){
					System.out.println(((OF_EX_Latch.getEX_op().equals("00110") || OF_EX_Latch.getEX_op().equals("00111")) && (rs1_val == 31 || rs2_val == 31)));
					}
					// if(OF_EX_Latch.getEX_op() != null){


						//bubble adding code:


					// if(
					// 	(OF_EX_Latch.getEX_rd() == rs1_val && rs1_val !=0 )|| 
					// 	(OF_EX_Latch.getEX_rd() == rs2_val && rs2_val !=0) || 
					// 	(OF_EX_Latch.getMA_rd() == rs2_val && rs2_val !=0 )|| 
					// 	(OF_EX_Latch.getMA_rd() == rs1_val && rs1_val !=0) || 
					// 	(OF_EX_Latch.getRW_rd()== rs2_val && rs2_val !=0) || 
					// 	(OF_EX_Latch.getRW_rd() == rs1_val && rs1_val !=0) ||
						
					// 	((OF_EX_Latch.getEX_op() != null) && ((OF_EX_Latch.getEX_op().equals("00110") || OF_EX_Latch.getEX_op().equals("00111")) && (rs1_val == 31 || rs2_val == 31))) ||
						
					// 	((OF_EX_Latch.getMA_op() != null) && (OF_EX_Latch.getMA_op().equals("00110") || OF_EX_Latch.getMA_op().equals("00111")) && (rs1_val == 31 || rs2_val == 31)) || 
						
					// 	((OF_EX_Latch.getRW_op() != null) && (OF_EX_Latch.getRW_op().equals("00110") || OF_EX_Latch.getRW_op().equals("00111")) && (rs1_val == 31 || rs2_val == 31)) 

					// 	// if(OF_EX_Latch.getEX_op().equals("11100") ||)
					
					// ){
					// 	OF_EX_Latch.setInstruction(0);
					// 	System.out.println("Bubble added in OF");
					// 	IF_OF_Latch.set_Stall(true);
					// 	OF_EX_Latch.setBranchTarget(branchTarget);
					// 	IF_OF_Latch.setOF_enable(false);
					// 	OF_EX_Latch.setEX_enable(true);
					// 	// OF_EX_Latch.setRd(40);

					// 	OF_EX_Latch.setRW_rd(OF_EX_Latch.getMA_rd());
					// 	OF_EX_Latch.setMA_rd(OF_EX_Latch.getEX_rd());
					// 	OF_EX_Latch.setEX_rd(40);

					// 	OF_EX_Latch.setRW_op(OF_EX_Latch.getMA_op());
					// 	OF_EX_Latch.setMA_op(OF_EX_Latch.getEX_op());
					// 	OF_EX_Latch.setEX_op("null");
					// }




					// if((OF_EX_Latch.getRd() == rs1_val && rs1_val !=0 )|| (OF_EX_Latch.getRd() == rs2_val && rs2_val !=0) || (EX_MA_Latch.getRd() == rs1_val && rs1_val !=0 )|| (EX_MA_Latch.getRd() == rs2_val && rs1_val !=0) || (MA_RW_Latch.getRd() == rs1_val && rs1_val !=0) || (MA_RW_Latch.getRd() == rs2_val && rs1_val !=0) || (rs1_val != 31 && rs2_val != 31)){
					// 	OF_EX_Latch.setInstruction(0);
					// 	System.out.println("Bubble added in OF");
					// 	IF_OF_Latch.set_Stall(true);
					// 	IF_OF_Latch.setOF_enable(true);
					// 	OF_EX_Latch.setEX_enable(true);
					// 	OF_EX_Latch.setRd(40);

					// 	OF_EX_Latch.setRW_rd(OF_EX_Latch.getMA_rd());
					// 	OF_EX_Latch.setMA_rd(OF_EX_Latch.getEX_rd());
					// 	OF_EX_Latch.setEX_rd(40);
					// }

					// if((EX_MA_Latch.getRd() == rs1_val && EX_MA_Latch.isMA_enable() )|| (EX_MA_Latch.getRd() == rs2_val && EX_MA_Latch.isMA_enable()) || (MA_RW_Latch.getRd() == rs1_val && MA_RW_Latch.isRW_enable()) || (MA_RW_Latch.getRd() == rs2_val && MA_RW_Latch.isRW_enable())){
					// 	OF_EX_Latch.setInstruction(0);
					// 	IF_OF_Latch.set_Stall(true);
					// 	System.out.println("OFstuck");
					// }
					// }
					// else{
						IF_OF_Latch.set_Stall(false);
						System.out.println("no bubble was needed");
						op1 = containingProcessor.getRegisterFile().getValue(rs1_val);
						op2 = containingProcessor.getRegisterFile().getValue(rs2_val);

						System.out.println("op1 and op2 OF are: ");
						System.out.println(op1);
						System.out.println(op2);
						
						
						
						OF_EX_Latch.setOpcode(opcode);
						OF_EX_Latch.setImmx(immx);
						OF_EX_Latch.setBranchTarget(branchTarget);
						OF_EX_Latch.setOp1(op1);
						OF_EX_Latch.setOp2(op2);
						OF_EX_Latch.setInstruction(instruction);
						OF_EX_Latch.setPC(pc);
						OF_EX_Latch.setRd(rdI);
						
						OF_EX_Latch.setRW_rd(OF_EX_Latch.getMA_rd());
						OF_EX_Latch.setMA_rd(OF_EX_Latch.getEX_rd());
						OF_EX_Latch.setEX_rd(rdI);

						OF_EX_Latch.setRW_op(OF_EX_Latch.getMA_op());
						OF_EX_Latch.setMA_op(OF_EX_Latch.getEX_op());
						OF_EX_Latch.setEX_op(opcode);

						IF_OF_Latch.setOF_enable(false);
						OF_EX_Latch.setEX_enable(true);

						if(opcode.equals("11101"))
						{
							IF_OF_Latch.setOF_enable(false);
							IF_OF_Latch.closeIF = true;
						}
						// System.out.println(OF_EX_Latch.getOpcode());
						// System.out.println(OF_EX_Latch.getOp1());
						// System.out.println(OF_EX_Latch.getOp2());
					// }
				}
			// }
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		
		}
	}

}
