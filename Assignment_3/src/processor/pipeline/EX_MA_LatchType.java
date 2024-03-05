package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int op2;
	int instruction;
	int aluResult;
	String opcode;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}
	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public int getOp2() {
		return op2;
	}
	public void setOp2(int op2) {
		this.op2 = op2;
	}

	public int getInstruction() {
		return instruction;
	}
	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public String getOpcode() {
		return opcode;
	}
	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public int getAluResult() {
		return aluResult;
	}
	public void setAluResult(int aluResult) {
		this.aluResult= aluResult;
	}

}
