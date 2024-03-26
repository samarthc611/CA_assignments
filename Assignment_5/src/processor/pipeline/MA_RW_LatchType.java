package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	int instruction;
	String opcode;
	int aluResult;
	int ldResult;
	int x31;
	int op1 , op2;
	int rd ;

	boolean  RW_busy;

	public MA_RW_LatchType()
	{
		RW_enable = false;
		RW_busy = false;
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
	public int getRd() {
		return rd;
	}

	public void setRd(int rd) {
    	this.rd = rd;
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
		return ldResult;
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

	public boolean isRW_busy(){
		return RW_busy;
	}
	public void setRW_busy(boolean value){
		RW_busy = value;
	}

}
