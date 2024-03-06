package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	int instruction;
	String opcode;
	int aluResult;
	int ldResult;
	int x31;

	public MA_RW_LatchType()
	{
		RW_enable = false;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
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

	public int getLdResult() {
		return aluResult;
	}
	public void setLdResult(int ldResult) {
		this.ldResult= ldResult;
	}

	public void setx31(int x31){
		this.x31 = x31;
	}
	public int getx31(){
		return x31;
	}

}
