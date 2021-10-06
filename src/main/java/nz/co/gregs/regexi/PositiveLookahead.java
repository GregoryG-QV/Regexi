/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.gregs.regexi;

/**
 *
 * @author gregorygraham
 * @param <REGEX> the type returned by {@link #endGroup() }
 */
public class PositiveLookahead<REGEX extends HasRegexFunctions<REGEX>> extends RegexGroup<PositiveLookahead<REGEX>, REGEX> {
	

	protected PositiveLookahead(REGEX original) {
		super(original);
	}

	@Override
	public String getRegex() {
		final String regexp = getCurrent().getRegex();
		return "(?="+regexp+")";
	}

	public REGEX endLookahead() {
		return super.endGroup();
	}
	
}