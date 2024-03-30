package processor.pipeline;

import generic.Instruction.OperationType;
import generic.MemoryReadEvent;
import generic.MemoryWriteEvent;
import generic.MemoryResponseEvent;
import generic.Element;
import generic.Event;
import generic.Simulator;
import processor.Processor;
import processor.Clock;
import configuration.Configuration;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	int ldresult = 0;
	int rd;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	int instruction;
	String opcode;
	public void performMA()
	{
		// System.out.println("Is MA busy?:");
		// System.out.println(EX_MA_Latch.isMA_busy());
		// System.out.println("Is EX busy(as per MA)?:");
		// System.out.println(EX_MA_Latch.isEX_busy());
		//TODO
		if(EX_MA_Latch.isMA_enable()){
			System.out.println("MA is ON");
		if(EX_MA_Latch.isMA_busy()==false){
			System.out.println("MA is not busy");
			if(EX_MA_Latch.getInstruction() == 0){
				MA_RW_Latch.setInstruction(0);
				// MA_RW_Latch.setRd(EX_MA_Latch.getRd());
				// EX_MA_Latch.setRd(OF_EX_Latch.getRd());
				MA_RW_Latch.setRd(40);
				System.out.println("     ************recieved bubble from EX i am in MA");
				EX_MA_Latch.setMA_enable(false);
				MA_RW_Latch.setRW_enable(true);
			}
			else{
				int op2 = EX_MA_Latch.getOp2();
				int op1 = EX_MA_Latch.getOp1();
				rd = EX_MA_Latch.getRd();
				int aluresult = EX_MA_Latch.getAluResult();
				opcode = EX_MA_Latch.getOpcode();
				instruction = EX_MA_Latch.getInstruction();
				
				if (opcode.equals("10110")) {
					Simulator.getEventQueue().addEvent(
							new MemoryReadEvent(
									Clock.getCurrentTime() + Configuration.mainMemoryLatency,
									this,
									containingProcessor.getMainMemory(),
									aluresult)
					);
					System.out.println("MA Load Event Added");
					EX_MA_Latch.setMA_busy(true);
					MA_RW_Latch.setRW_enable(false);
					EX_MA_Latch.setMA_enable(false);
					EX_MA_Latch.setEX_busy(true);
					// return;
				} else if (opcode.equals("10111")) {
					// int stWord = op2;
					// Simulator.getEventQueue().addEvent(
					// 		new MemoryWriteEvent(
					// 				Clock.getCurrentTime() + Configuration.mainMemoryLatency,
					// 				this,
					// 				containingProcessor.getMainMemory(),
					// 				aluresult,
					// 				stWord)
					// );
					containingProcessor.getMainMemory().setWord(aluresult,op2);
					System.out.println("MA Store Event Added");
					// EX_MA_Latch.setMA_busy(true);
					// EX_MA_Latch.setEX_busy(true);
					MA_RW_Latch.setRW_enable(true);
					EX_MA_Latch.setMA_enable(false);
					// return;
				}
				else{
				// if(opcode.equals("10110")){
				// 	ldresult = containingProcessor.getMainMemory().getWord(aluresult);
				// 	// System.out.print("ldresult=");
				// 	// System.out.println(ldresult);
				// }


				// if(opcode.equals("10111")){
				// 	System.out.print("aluresult for store inst: ");
				// 	System.out.println(aluresult);
				// 	System.out.println("op2 MA=");
				// 	System.out.println(op2);
				// 	containingProcessor.getMainMemory().setWord(aluresult,op2);
				// 	System.out.print("store inst MA ");
				// 	System.out.println(containingProcessor.getMainMemory().getWord(aluresult));
				// }
				System.out.println("MAAAAAAa");
				EX_MA_Latch.setMA_enable(false);
				MA_RW_Latch.setRW_enable(true);
				MA_RW_Latch.setInstruction(instruction);
				MA_RW_Latch.setOpcode(opcode);
				MA_RW_Latch.setAluResult(aluresult);
				// System.out.print("ldresult befor marw set=");
				// System.out.println(ldresult);
				// MA_RW_Latch.setLdResult(ldresult);
				MA_RW_Latch.setx31(EX_MA_Latch.getx31());
				MA_RW_Latch.setRd(rd);

				// System.out.println(MA_RW_Latch.getInstruction());
				// System.out.println(MA_RW_Latch.getAluResult());
				// System.out.print("ma  rw latch ldresult=");
				// System.out.println(MA_RW_Latch.getLdResult());
				// System.out.println(MA_RW_Latch.getOpcode());
			}
			}
		}
		else{
			EX_MA_Latch.setEX_busy(true);
			EX_MA_Latch.setMA_enable(false);
		}
		EX_MA_Latch.setMA_enable(false);
	}
	}
	@Override
	public void handleEvent(Event e) {
		if(e.getEventType() == Event.EventType.MemoryResponse) {
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			int ldResult = event.getValue();
			MA_RW_Latch.setLdResult(ldResult);
			MA_RW_Latch.setInstruction(instruction);
			MA_RW_Latch.setx31(EX_MA_Latch.getx31());
			MA_RW_Latch.setRd(EX_MA_Latch.getRd());
			MA_RW_Latch.setOpcode(opcode);

			EX_MA_Latch.setMA_busy(false);
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setEX_busy(false);
			// OF_EX_Latch.setEX_Busy(false);
			System.out.println("MA Load Event Handled");
			EX_MA_Latch.setMA_enable(false);
		}
		else{
			MA_RW_Latch.setInstruction(0);
			MA_RW_Latch.setRW_enable(true);
		// EX_MA_Latch.setMA_busy(false);
		// EX_MA_Latch.setEX_busy(false);
		// EX_MA_Latch.setMA_enable(false);
		// System.out.println("MA Store Event Handled");
		}
		// EX_MA_Latch.setMA_busy(false);
		// EX_MA_Latch.setEX_busy(false);
		// EX_MA_Latch.setMA_enable(false);
		// System.out.println("MA Store Event Handled");
	}

}
