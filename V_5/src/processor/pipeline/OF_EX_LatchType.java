package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	int instruction;
	int op1;
	int op2;
	int bt;
	String opcode;
	int immx;
	int pc;
	int rd;
	int EX_rd;
	int MA_rd;
	int RW_rd;

	boolean isBranchTaken;

	String EX_op;
	String MA_op;
	String RW_op;
	
	public OF_EX_LatchType()
	{
		EX_enable = false;
		// opcode="11111";
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
	public int getOp22() {
		return op2;
	}
	public void setOp22(int op2) {
		this.op2 = op2;
	}

	public int getRd() {
		return rd;
	}
	public void setRd(int rd) {
    	this.rd = rd;
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

	public void setPC(int pc){
		this.pc = pc;
	}
	public int getPC(){
		return pc;
	}

	public void setEX_rd(int ex_rd){
		this.EX_rd = ex_rd;
	}
	public int getEX_rd(){
		return EX_rd;
	}


	public void setMA_rd(int ma_rd){
		this.MA_rd = ma_rd;
	}
	public int getMA_rd(){
		return MA_rd;
	}

	public void setRW_rd(int rw_rd){
		this.RW_rd = rw_rd;
	}
	public int getRW_rd(){
		return RW_rd;
	}




	public void setEX_op(String ex_op){
		this.EX_op = ex_op;
	}
	public String getEX_op(){
		return EX_op;
	}

	public void setMA_op(String ma_op){
		this.MA_op = ma_op;
	}
	public String getMA_op(){
		return MA_op;
	}

	public void setRW_op(String rw_op){
		this.RW_op = rw_op;
	}
	public String getRW_op(){
		return RW_op;
	}

	public boolean getIsBranchTaken() {
		return isBranchTaken;
	}

	public void setIsBRanchTaken(boolean isBranchTaken) {
		this.isBranchTaken = isBranchTaken;
	}
}
