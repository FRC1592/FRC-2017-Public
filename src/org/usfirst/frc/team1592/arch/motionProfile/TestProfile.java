package org.usfirst.frc.team1592.arch.motionProfile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TestProfile {

	public static void main(String[] args) throws IOException {
		File out = new File("C:\\Users\\ddyer\\Desktop\\test\\output.csv");
		FileWriter fwriter = new FileWriter(out,false);
		PrintWriter pwriter = new PrintWriter(fwriter);
		TrapezoidProfile tp = new TrapezoidProfile(0.1,2,1,1.5);
		
		double[] t = {0,0.25,0.5,0.75,1,1.25,1.5,1.75,2,2.25,2.5,2.75,3,3.25,3.5,3.75,4};
		TrajectoryPoint pnt = new TrajectoryPoint();
		
		System.out.println(tp.tAccelStop);
		System.out.println(tp.pAccelStop);
		System.out.println(tp.tDecelStart);
		System.out.println(tp.pDecelStart);
		
		for (int i = 0; i < t.length; i++) {
			pnt = tp.getTrajPoint(t[i]);
			pwriter.println(pnt.toString());
		}
		
		pwriter.close();

	}

}
