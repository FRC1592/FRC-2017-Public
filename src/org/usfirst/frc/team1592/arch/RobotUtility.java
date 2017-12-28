package org.usfirst.frc.team1592.arch;

import java.util.Collection;
import java.util.Collections;

/**
 * Static Utility Class
 */
public final class RobotUtility {
	
	/** Static Class */
	private RobotUtility() {};
	
	
	
	//==========================//
	//      String Methods      //
	//==========================//
	
	/**
	 * Ensures that the resulting string from this method is a unique string which is not
	 * contained in the supplied collection. This method will first simply return the input string
	 * if it is not found in the collection. After that, additional actions are taken until a
	 * string is generated that is unique to the collection. The string is NOT added to the 
	 * collection in this method.
	 *
	 * @param in  the string input
	 * @param collection  the collection to test against for uniqueness
	 * @return the original string, if not in the collection, or a generated string based off of the
	 * 			input string that would be unique in the collection
	 */
	public static final String ensureUnique(String in, Collection< ? extends String> collection) {
		if (collection==null) {collection = Collections.emptySet();}
		if (in==null) {in = "";}
		if (collection.contains(in)) {
			long v = 1;
			String nameMod = in+"_"+Long.toString(v++);
			while (collection.contains(nameMod)) {
				nameMod = in+"_"+Long.toString(v++);
			}
			in = nameMod;
		}
		return in;
	}
	

}
