package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable,stall;
	int instruction;
	boolean isBranchTaken;
	int pc;
	boolean closeIF;
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
		closeIF = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}
	public boolean is_Stall() {
		return stall;
	}

	public void set_Stall(boolean stall) {
		this.stall = stall;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public void setPC(int pc){
		this.pc = pc;
	}
	public int getPC(){
		return pc;
	}
	public boolean getIsBranchTaken() {
		return isBranchTaken;
	}

	public void setIsBRanchTaken(boolean isBranchTaken) {
		this.isBranchTaken = isBranchTaken;
	}

}
