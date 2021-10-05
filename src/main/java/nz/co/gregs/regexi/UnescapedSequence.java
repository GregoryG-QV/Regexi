/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.gregs.regexi;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gregorygraham
 */
public class UnescapedSequence extends Regex {
	
	private final String literal;

	protected UnescapedSequence(String literals) {
		if (literals == null) {
			this.literal = "";
		} else {
			this.literal = literals;
		}
	}

	@Override
	public String getRegex() {
		return literal;
	}

	@Override
	public List<String> testAgainst(String testStr) {
		// these are general regex control sequences and shouldn't be dismantled
		return new ArrayList<String>(0);
	}
}
