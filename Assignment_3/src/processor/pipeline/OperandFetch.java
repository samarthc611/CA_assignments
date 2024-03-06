package processor.pipeline;

import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	int instruction;
	int branchTarget;
	int op1;
	int op2;


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
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			instruction = IF_OF_Latch.getInstruction();
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
			System.out.print("immstr OF=");
			System.out.println(immstr);
			System.out.print("immx OF=");
			System.out.println(immx);


			// int pc = containingProcessor.getRegisterFile().getProgramCounter();
			int pc = IF_OF_Latch.getPC();

			if (opcode.equals("11000")) {
				branchTarget = convertToInt(destImm) + pc;
			}
			else{
				branchTarget = immx + pc;
				System.out.print("branchTarget OF=");
				System.out.println(branchTarget);
			}
				
				
			String rs1,rs2;
			if(opcode.equals("10111")){
				rs1 = instString.substring(10,15);
				rs2 = instString.substring(5,10);
			}
			else{
				rs1 = instString.substring(5,10);
				rs2 = instString.substring(10,15);
			}

			System.out.print("rs1 and rs2 binary are ");
			System.out.println(rs1);
			System.out.println(rs2);


			int rs1_val = Integer.parseUnsignedInt(rs1, 2);  
			int rs2_val = Integer.parseUnsignedInt(rs2, 2);
			System.out.print("rs1 and rs2 are: ");
			System.out.println(rs1_val);
			System.out.println(rs2_val);
			
			
			op1 = containingProcessor.getRegisterFile().getValue(rs1_val);
			op2 = containingProcessor.getRegisterFile().getValue(rs2_val);

			OF_EX_Latch.setOpcode(opcode);
			OF_EX_Latch.setImmx(immx);
			OF_EX_Latch.setBranchTarget(branchTarget);
			OF_EX_Latch.setOp1(op1);
			OF_EX_Latch.setOp2(op2);
			OF_EX_Latch.setInstruction(instruction);
			OF_EX_Latch.setPC(pc);

			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);


			// System.out.println(OF_EX_Latch.getOpcode());
			// System.out.println(OF_EX_Latch.getOp1());
			// System.out.println(OF_EX_Latch.getOp2());

		}
	}

}
