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
			String instString = Integer.toBinaryString(instruction);
			while(instString.length() < 32){
				
				instString = "0" + instString;
			}

			String opcode = instString.substring(0, 5);
			String immstr = instString.substring(15, 32);
			String destImm = instString.substring(10, 32);
			int immx = convertToInt(immstr);

			int pc = containingProcessor.getRegisterFile().getProgramCounter();

			if (opcode.equals("11000")) {
				branchTarget = convertToInt(destImm) + pc;
			}
			else
				branchTarget = immx + pc;

				
			String rs1,rs2;
			if(opcode.equals("10111")){
				rs1 = instString.substring(10,15);
				rs2 = instString.substring(5,10);
			}
			else{
				rs1 = instString.substring(5,10);
				rs2 = instString.substring(10,15);
			}


			int rs1_val = Integer.parseInt(rs1, 2);  
			int rs2_val = Integer.parseInt(rs2, 2);
			
			op1 = containingProcessor.getRegisterFile().getValue(rs1_val);
			op2 = containingProcessor.getRegisterFile().getValue(rs2_val);

			OF_EX_Latch.setOpcode(opcode);
			OF_EX_Latch.setImmx(immx);
			OF_EX_Latch.setBranchTarget(branchTarget);
			OF_EX_Latch.setOp1(op1);
			OF_EX_Latch.setOp2(op2);
			OF_EX_Latch.setInstruction(instruction);

			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
