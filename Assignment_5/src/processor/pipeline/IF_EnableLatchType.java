package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean IF_enable, IF_busy;
	
	public IF_EnableLatchType()
	{
		IF_enable = true;
		IF_busy = false;
	}

	public boolean isIF_busy(){
		return IF_busy;
	}
	public void setIF_busy(boolean value){
		IF_busy = value;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

}
