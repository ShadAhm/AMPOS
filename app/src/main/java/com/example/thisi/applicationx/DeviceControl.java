package com.example.thisi.applicationx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DeviceControl {
	private BufferedWriter CtrlFile;
	public int gpio = 64;
	public static String powerPathKT="/sys/class/misc/mtgpio/pin";
	public static String powerPathTT="/proc/geomobile/lf";

	public int getGpio() {
		return gpio;
	}

	public void setGpio(int gpio) {
		this.gpio = gpio;
	}

	public DeviceControl(String powerPath)  {
		File DeviceName = new File(powerPath);
		try {
			CtrlFile = new BufferedWriter(new FileWriter(DeviceName, false));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // open
																			// file
	}

	public void PowerOnDevice() throws IOException // poweron barcode device
	{
		CtrlFile.write("on");
		CtrlFile.flush();
	}

	public void PowerOffDevice() throws IOException // poweroff barcode device
	{
		CtrlFile.write("off");
		CtrlFile.flush();
	}

	public void PowerOnMTDevice() throws IOException // poweron barcode device
	{
		CtrlFile.write("-wdout" + gpio + " 1");
		CtrlFile.flush();
	}

	public void PowerOffMTDevice() throws IOException // poweroff barcode device
	{
		CtrlFile.write("-wdout" + gpio + " 0");
		CtrlFile.flush();
	}

	public void TriggerOnDevice() throws IOException // make barcode begin to
														// scan
	{
		CtrlFile.write("trig");
		CtrlFile.flush();
	}

	public void TriggerOffDevice() throws IOException // make barcode stop scan
	{
		CtrlFile.write("trigoff");
		CtrlFile.flush();
	}

	public void DeviceClose() throws IOException // close file
	{
		CtrlFile.close();
	}
}