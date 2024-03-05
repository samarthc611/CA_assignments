package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	int instruction;
	int op1;
	int op2;
	int bt;
	String opcode;
	int immx;
	
	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public int getInstruction() {
		return instruction;
	}
	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public int getOp1() {
		return op1;
	}
	public void setOp1(int op1) {
		this.op1 = op1;
	}

	public int getOp2() {
		return op2;
	}
	public void setOp2(int op2) {
		this.op2 = op2;
	}

	public int getBranchTarget() {
		return bt;
	}
	public void setBranchTarget(int bt) {
		this.bt = bt;
	}

	public String getOpcode() {
		return opcode;
	}
	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public int getImmx() {
		return immx;
	}
	public void setImmx(int immx) {
		this.immx = immx;
	}

}
