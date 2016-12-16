package com.example.thisi.applicationx.printutil;

import rego.printlib.export.regoPrinter;
import com.example.thisi.applicationx.printutil.preDefiniation.TransferMode;
import android.app.Application;

public class ApplicationContext extends Application {
	private regoPrinter printer;
	private int myState = 0;
	private String printName="RG-E487";//"RG-MTP58B";
	private int alignTypetext;

	private TransferMode printmode = TransferMode.TM_NONE;
	private boolean labelmark = true;
	

	public regoPrinter getObject() {
		return printer;
	}
	
	public int setAlignType(int n){
		 alignTypetext=n;
		return alignTypetext;
		
	}
	public int getAlignType(){
		return alignTypetext;
	}

	public void setObject() {
		printer = new regoPrinter(this);
	}
	
	public String getName() {
		return printName;
	}

	public void setName(String name) {
		printName = name;
	}
	public void setState(int state) {
		myState = state;
	}

	public int getState() {
		return myState;
	}

	public void setPrintway(int printway) {

		switch (printway) {
		case 0:
			printmode = TransferMode.TM_NONE;
			break;
		case 1:
			printmode = TransferMode.TM_DT_V1;
			break;
		default:
			printmode = TransferMode.TM_DT_V2;
			break;
		}

	}

	public int getPrintway() {
		return 0;
	}

	public boolean getlabel() {
		return labelmark;
	}

	public void setlabel(boolean labelprint) {
		labelmark = labelprint;
	}

	public void PrintNLine(int lines) {
		// TODO Auto-generated method stub
		printer.ASCII_PrintBuffer(myState, new byte[] { 0x1B, 0x66, 1,
				(byte) lines }, 4);
		
	}

	public void printSettings() {
		// 1D 28 41 00 00 00 02
		byte[] printdata = { 0x1d, 0x28, 0x41, 0x00, 0x00, 0x00, 0x02 };
		printer.ASCII_PrintBuffer(myState, printdata, printdata.length);
	}



}
