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
class RegexCombination extends PartialRegex {

	private final PartialRegex first;
	private final PartialRegex second;

	protected RegexCombination(PartialRegex first, PartialRegex second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public String getRegex() {
		return first.getRegex() + second.getRegex();
	}

	@Override
	public List<PartialRegex> getRegexParts() {
		List<PartialRegex> result = new ArrayList<PartialRegex>(1);
		result.addAll(first.getRegexParts());
		result.addAll(second.getRegexParts());
		return result;
	}

}
