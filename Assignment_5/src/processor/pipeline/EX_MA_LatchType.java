package processor.pipeline;

public class EX_MA_LatchType {
    
    boolean MA_enable ;
    int op1;
    int op2;
    int instruction;
    int aluResult;
    String opcode;
    int x31;
    int rd ;
    boolean isBranchTaken, MA_busy, EX_busy;

    // Constructor
    public EX_MA_LatchType() {
        MA_enable = false;
         MA_busy = false;
    }

    // Getter and setter methods
    public boolean isMA_enable() {
        return MA_enable;
    }
    public void setMA_enable(boolean mA_enable) {
        MA_enable = mA_enable;
    }

    public boolean isEX_busy(){
		return EX_busy;
	}
	public void setEX_busy(boolean value){
		EX_busy = value;
	}
    

    public int getOp2() {
        return op2;
    }
    public void setOp2(int op2) {
        this.op2 = op2;
    }

    public int getOp1() {
        return op1;
    }
    public void setOp1(int op1) {
        this.op1 = op1;
    }

    public int getRd() {
        return rd;
    }
    public void setRd(int rd) {
        this.rd = rd;
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
        this.aluResult = aluResult;
    }

    public void setx31(int x31){
        this.x31 = x31;
    }
    public int getx31(){
        return x31;
    }
    public boolean getIsBranchTaken() {
		return isBranchTaken;
	}

	public void setIsBRanchTaken(boolean isBranchTaken) {
		this.isBranchTaken = isBranchTaken;
	}


    public boolean isMA_busy(){
		return MA_busy;
	}
    public void setMA_busy(boolean value){
		MA_busy = value;
	}
}
