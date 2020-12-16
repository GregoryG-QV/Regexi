package nz.co.gregs.regexi;

/*
 * Copyright 2019 Gregory Graham.
 *
 * Commercial licenses are available, please contact info@gregs.co.nz for details.
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/ 
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * 
 * You are free to:
 *     Share - copy and redistribute the material in any medium or format
 *     Adapt - remix, transform, and build upon the material
 * 
 *     The licensor cannot revoke these freedoms as long as you follow the license terms.               
 *     Under the following terms:
 *                 
 *         Attribution - 
 *             You must give appropriate credit, provide a link to the license, and indicate if changes were made. 
 *             You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 *         NonCommercial - 
 *             You may not use the material for commercial purposes.
 *         ShareAlike - 
 *             If you remix, transform, or build upon the material, 
 *             you must distribute your contributions under the same license as the original.
 *         No additional restrictions - 
 *             You may not apply legal terms or technological measures that legally restrict others from doing anything the 
 *             license permits.
 * 
 * Check the Creative Commons website for any details, legalese, and updates.
 */

import java.util.HashMap;
import java.util.List;
import  static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gregorygraham
 */
public class RegexTest {

	public RegexTest() {
	}

	@Test
	public void testFindingANegativeNumber() {
		Regex negativeInteger = Regex.startingAnywhere().negativeInteger();
		Assert.assertTrue(negativeInteger.matchesEntireString("-1"));
		Assert.assertTrue(negativeInteger.matchesWithinString("-1"));
		Assert.assertFalse(negativeInteger.matchesEntireString("1"));
		Assert.assertFalse(negativeInteger.matchesWithinString("1"));
		Assert.assertFalse(negativeInteger.matchesEntireString("below zero there are negative and -1 is the first"));
		Assert.assertTrue(negativeInteger.matchesWithinString("below zero there are negative and -1 is the first"));
	}

	@Test
	public void testFindingAPositiveNumber() {
		Regex positiveInteger = Regex.startingAnywhere().positiveInteger();
		assertThat(positiveInteger.matchesEntireString("-1"), is(false));
		assertThat(positiveInteger.matchesWithinString("-1"), is(false));
		assertThat(positiveInteger.matchesEntireString("1"), is(true));
		assertThat(positiveInteger.matchesWithinString("1"), is(true));
		assertThat(positiveInteger.matchesEntireString("+1"), is(true));
		assertThat(positiveInteger.matchesWithinString("+1"), is(true));
		assertThat(positiveInteger.matchesEntireString("below zero there are negatives and -1 is the first"), is(false));
		assertThat(positiveInteger.matchesWithinString("below zero there are negatives and -1 is the first"), is(false));
		assertThat(positiveInteger.matchesEntireString("above zero there are positives and 1 is the first"), is(false));
		assertThat(positiveInteger.matchesWithinString("above zero there are positives and 1 is the first"), is(true));
		assertThat(positiveInteger.matchesEntireString("above zero there are positives and +1 is the first"), is(false));
		assertThat(positiveInteger.matchesWithinString("above zero there are positives and +1 is the first"), is(true));
	}

	@Test
	public void testFindingPostgresIntervalValues() {
		// -2 days 00:00:00
		// 1 days 00:00:5.5
		// 0 days 00:00:-5.5
		//
		//(-?[0-9]+)([^-0-9]+)(-?[0-9]+):(-?[0-9]+):(-?[0-9]+)(\.\d+)?

		final Regex allowedValue
				= Regex.startingAnywhere()
						.literal('-').onceOrNotAtAll()
						.anyBetween('0', '9').atLeastOnce();

		final Regex allowedSeconds
				= allowedValue.add(
						Regex.startingAnywhere().dot().digits()
				).onceOrNotAtAll();

		final Regex separator
				= Regex.startingAnywhere().openRange('0', '9').includeMinus().negated().closeRange().atLeastOnce();
//				= new Regex.Range('0', '9')
//						.includeMinus()
//						.negated()
//						.atLeastOnce();

		Regex pattern
				= Regex.startingAnywhere()
						.add(allowedValue).add(separator)
						.add(allowedValue).literal(':')
						.add(allowedValue).literal(':')
						.add(allowedSeconds);

//		System.out.println("PASS: " + pattern.matchesWithinString("-2 days 00:00:00"));
//		System.out.println("PASS: " + pattern.matchesWithinString("2 days 00:00:00"));
//		System.out.println("PASS: " + pattern.matchesWithinString("2 days 00:00:00.0"));
//		System.out.println("PASS: " + pattern.matchesWithinString("1 days 00:00:5.5"));
//		System.out.println("PASS: " + pattern.matchesWithinString("2 days 00:00:00"));
//		System.out.println("PASS: " + pattern.matchesWithinString("1 days 00:00:5.5"));
//		System.out.println("PASS: " + pattern.matchesWithinString("0 days 00:00:-5.5"));
//		System.out.println("PASS: " + pattern.matchesWithinString("0 00:00:-5.5"));
//		System.out.println("FAIL: " + pattern.matchesWithinString("00:00:-5.5"));
//		System.out.println("FAIL: " + pattern.matchesWithinString("-2 days"));
		assertThat(pattern.matchesWithinString("-2 days 00:00:00"), is(true));
		assertThat(pattern.matchesWithinString("2 days 00:00:00"), is(true));
		assertThat(pattern.matchesWithinString("2 days 00:00:00.0"), is(true));
		assertThat(pattern.matchesWithinString("1 days 00:00:5.5"), is(true));
		assertThat(pattern.matchesWithinString("2 days 00:00:00"), is(true));
		assertThat(pattern.matchesWithinString("1 days 00:00:5.5"), is(true));
		assertThat(pattern.matchesWithinString("0 days 00:00:-5.5"), is(true));
		assertThat(pattern.matchesWithinString("0 00:00:-5.5"), is(true));
		assertThat(pattern.matchesWithinString("00:00:-5.5"), is(false));
		assertThat(pattern.matchesWithinString("-2"), is(false));

	}

	@Test
	public void testFindingPostgresIntervalValuesWithAOneliner() {
		// -2 days 00:00:00
		// 1 days 00:00:5.5
		// 0 days 00:00:-5.5
		//
		//-?[0-9]+([^-0-9])+-?[0-9]+:{1}-?[0-9]+:{1}-?[0-9]+(\.\d+)?
		Regex pattern
				= Regex.startingAnywhere()
						.literal('-').onceOrNotAtAll()
						.anyBetween('0', '9').atLeastOnce()
						.openRange('0', '9')
						.includeMinus()
						.negated()
						.closeRange()
						.atLeastOnce()
						.literal('-').onceOrNotAtAll()
						.anyBetween('0', '9').atLeastOnce()
						.literal(':').once()
						.literal('-').onceOrNotAtAll()
						.anyBetween('0', '9').atLeastOnce()
						.literal(':').once()
						.literal('-').onceOrNotAtAll()
						.anyBetween('0', '9').atLeastOnce().add(Regex.startingAnywhere().dot().digits()
				).onceOrNotAtAll();

//		System.out.println("PASS: " + pattern.matchesWithinString("-2 days 00:00:00"));
//		System.out.println("PASS: " + pattern.matchesWithinString("2 days 00:00:00"));
//		System.out.println("PASS: " + pattern.matchesWithinString("2 days 00:00:00.0"));
//		System.out.println("PASS: " + pattern.matchesWithinString("1 days 00:00:5.5"));
//		System.out.println("PASS: " + pattern.matchesWithinString("2 days 00:00:00"));
//		System.out.println("PASS: " + pattern.matchesWithinString("1 days 00:00:5.5"));
//		System.out.println("PASS: " + pattern.matchesWithinString("0 days 00:00:-5.5"));
//		System.out.println("PASS: " + pattern.matchesWithinString("0 00:00:-5.5"));
//		System.out.println("FAIL: " + pattern.matchesWithinString("00:00:-5.5"));
//		System.out.println("FAIL: " + pattern.matchesWithinString("-2 days"));
		assertThat(pattern.matchesWithinString("-2 days 00:00:00"), is(true));
		assertThat(pattern.matchesWithinString("2 days 00:00:00"), is(true));
		assertThat(pattern.matchesWithinString("2 days 00:00:00.0"), is(true));
		assertThat(pattern.matchesWithinString("1 days 00:00:5.5"), is(true));
		assertThat(pattern.matchesWithinString("2 days 00:00:00"), is(true));
		assertThat(pattern.matchesWithinString("1 days 00:00:5.5"), is(true));
		assertThat(pattern.matchesWithinString("0 days 00:00:-5.5"), is(true));
		assertThat(pattern.matchesWithinString("0 00:00:-5.5"), is(true));
		assertThat(pattern.matchesWithinString("00:00:-5.5"), is(false));
		assertThat(pattern.matchesWithinString("-2"), is(false));

	}

	@Test
	public void testGroupBuilding() {

		Regex pattern
				= Regex.startGroup().literal("Amy").or().literal("Bob").or().literal("Charlie").closeGroup();

		assertThat(pattern.matchesWithinString("Amy"), is(true));
		assertThat(pattern.matchesWithinString("Bob"), is(true));
		assertThat(pattern.matchesWithinString("Charlie"), is(true));
		assertThat(pattern.matchesEntireString("Amy"), is(true));
		assertThat(pattern.matchesEntireString("Bob"), is(true));
		assertThat(pattern.matchesEntireString("Charlie"), is(true));
		assertThat(pattern.matchesWithinString("David"), is(false));
		assertThat(pattern.matchesWithinString("Emma"), is(false));
		assertThat(pattern.matchesWithinString("Try with Amy in the middle"), is(true));
		assertThat(pattern.matchesWithinString("End it with Bob"), is(true));
		assertThat(pattern.matchesWithinString("Charlie at the start"), is(true));
		assertThat(pattern.matchesEntireString("Try with Amy in the middle"), is(false));
		assertThat(pattern.matchesEntireString("End it with Bob"), is(false));
		assertThat(pattern.matchesEntireString("Charlie at the start"), is(false));
		assertThat(pattern.matchesWithinString("Still can't find David"), is(false));
		assertThat(pattern.matchesWithinString("Emma doesn't do any better"), is(false));

	}

	@Test
	public void testNumberElement() {
		// -2 days 00:00:00
		// 1 days 00:00:5.5
		// 0 days 00:00:-5.5
		//
		// ([-+]?\b[1-9]+\d*(\.{1}\d+)?){1}
		Regex pattern
				= Regex.startingAnywhere()
						.number().once();

//		System.out.println("REGEX: " + pattern.getRegex());
		assertThat(pattern.matchesWithinString("before -1 after"), is(true));
		assertThat(pattern.matchesWithinString("before 2 after"), is(true));
		assertThat(pattern.matchesWithinString("before -234 after"), is(true));
		assertThat(pattern.matchesWithinString("before +4 after"), is(true));
		assertThat(pattern.matchesWithinString("before -4 after"), is(true));
		assertThat(pattern.matchesWithinString("before 4.5 after"), is(true));
		assertThat(pattern.matchesWithinString("before -4.5 after"), is(true));
		assertThat(pattern.matchesWithinString("before -4.05 after"), is(true));
		assertThat(pattern.matchesWithinString("before 02 after"), is(false));
		assertThat(pattern.matchesWithinString("before -0234 after"), is(false));
		assertThat(pattern.matchesWithinString("before 004 after"), is(false));
		assertThat(pattern.matchesWithinString("before _4 after"), is(false));
		assertThat(pattern.matchesWithinString("before A4 after"), is(false));
		assertThat(pattern.matchesWithinString("before A4after"), is(false));
		assertThat(pattern.matchesWithinString("before 2*E10"), is(false));

	}

	@Test
	public void testNumberIncludingScientificNotationElement() {
		// -2 days 00:00:00
		// 1 days 00:00:5.5
		// 0 days 00:00:-5.5
		//
		// ([-+]?\b[1-9]+\d*(\.{1}\d+)?){1}
		Regex pattern
				= Regex.startingAnywhere()
						.numberIncludingScientificNotation().once();

//		System.out.println("REGEX: " + pattern.getRegex());
		assertThat(pattern.matchesWithinString("before -1 after"), is(true));
		assertThat(pattern.matchesWithinString("before 2 after"), is(true));
		assertThat(pattern.matchesWithinString("before -234 after"), is(true));
		assertThat(pattern.matchesWithinString("before +4 after"), is(true));
		assertThat(pattern.matchesWithinString("before -4 after"), is(true));
		assertThat(pattern.matchesWithinString("before 4.5 after"), is(true));
		assertThat(pattern.matchesWithinString("before -4.5 after"), is(true));
		assertThat(pattern.matchesWithinString("before -4.05 after"), is(true));
		assertThat(pattern.matchesWithinString("before 02 after"), is(false));
		assertThat(pattern.matchesWithinString("before -0234 after"), is(false));
		assertThat(pattern.matchesWithinString("before 004 after"), is(false));
		assertThat(pattern.matchesWithinString("before _4 after"), is(false));
		assertThat(pattern.matchesWithinString("before A4 after"), is(false));
		assertThat(pattern.matchesWithinString("before A4after"), is(false));
		assertThat(pattern.matchesWithinString("before 2*E10"), is(false));
		//2E10, -2.89E-7.98, or 1.37e+15

		assertThat(pattern.matchesWithinString("before 2E10 after"), is(true));
		assertThat(pattern.matchesWithinString("before -2.89E-7.98 after"), is(true));
		assertThat(pattern.matchesWithinString("before 1.37e+15 after"), is(true));
		assertThat(pattern.matchesWithinString("before 2E10"), is(true));
		assertThat(pattern.matchesWithinString("before -2.89E-7.98"), is(true));
		assertThat(pattern.matchesWithinString("before 1.37e+15"), is(true));
		assertThat(pattern.matchesWithinString("2E10 after"), is(true));
		assertThat(pattern.matchesWithinString("-2.89E-7.98 after"), is(true));
		assertThat(pattern.matchesWithinString("1.37e+15 after"), is(true));

		assertThat(pattern.matchesWithinString("INTERVAL -1.999999999946489E-6 SECOND"), is(true));
		final List<Match> allMatches = pattern.getAllMatches("INTERVAL -1.999999999946489E-6 SECOND");
		assertThat(allMatches.size(), is(1));
		final Double value = Double.valueOf(allMatches.get(0).getEntireMatch());
		assertThat(value, is(Double.valueOf("-1.999999999946489E-6")));
		assertThat(Math.round(value * 1000000), is(-2L));

	}

	@Test
	public void testAllGroups() {
		// -2 days 00:00:00
		// 1 days 00:00:5.5
		// 0 days 00:00:-5.5
		//
		//  ^((?i)interval(?-i)){1} {1}([-+]?\b[1-9]+\d*(\.{1}\d+)?(((?i)E(?-i)){1}[-+]?[1-9]+\d*(\.{1}\d+)?)?(?!\S)){1} {1}(\w+)$
		Regex pattern
				= Regex.startingFromTheBeginning()
						.literalCaseInsensitive("interval").once()
						.space().once()
						.numberIncludingScientificNotation().once()
						.space().once()
						.openGroup().word().closeGroup()
						.endOfInput();

		System.out.println("REGEX: " + pattern.getRegex());
		String intervalString = "INTERVAL -1.999999999946489E-6 SECOND";
		assertThat(pattern.matchesEntireString(intervalString), is(true));
		final List<String> allMatches = pattern.getAllGroups(intervalString);
		allMatches.stream().forEach(t -> System.out.println("MATCH: " + t));
		assertThat(allMatches.size(), is(6));
		final Double value = Double.valueOf(allMatches.get(2));
		assertThat(value, is(Double.valueOf("-1.999999999946489E-6")));
		assertThat(Math.round(value * 1000000), is(-2L));

	}
	
	@Test
	public void testNamedGroups() {
		// -2 days 00:00:00
		// 1 days 00:00:5.5
		// 0 days 00:00:-5.5
		//
		// ^(?<interval>((?i)interval(?-i)){1}) {1}(?<value>([-+]?\b[1-9]+\d*(\.{1}\d+)?(((?i)E(?-i)){1}[-+]?[1-9]+\d*(\.{1}\d+)?)?(?!\S)){1}) {1}(?<unit>\w+)$
		Regex pattern
				= Regex.startingFromTheBeginning()
						.namedCapture("interval").literalCaseInsensitive("interval").once().endCapture()
						.space().once()
						.namedCapture("value").numberIncludingScientificNotation().once().endCapture()
						.space().once()
						.namedCapture("unit").word().endCapture()
						.endOfInput();

		System.out.println("REGEX: " + pattern.getRegex());
		String intervalString = "INTERVAL -1.999999999946489E-6 SECOND";
		assertThat(pattern.matchesEntireString(intervalString), is(true));
		final HashMap<String, String> allGroups = pattern.getAllNamedGroups(intervalString);
		for (var entry : allGroups.entrySet()) {
			System.out.println("GROUP: "+entry.getKey()+"="+entry.getValue());
		}
		assertThat(allGroups.size(), is(3));
		final Double value = Double.valueOf(allGroups.get("value"));
		assertThat(value, is(Double.valueOf("-1.999999999946489E-6")));
		assertThat(Math.round(value * 1000000), is(-2L));
	}

	@Test
	public void testNumberLike() {
		// -2 days 00:00:00
		// 1 days 00:00:5.5
		// 0 days 00:00:-5.5
		//
		// ([-+]?\b[1-9]+\d*(\.{1}\d+)?){1}
		Regex pattern
				= Regex.startingAnywhere()
						.numberLike().once();

//		System.out.println("REGEX: " + pattern.getRegex());
		//-1 2 -234 +4 -4 4.5 FAIL 02 -0234 004 _4 A4
		assertThat(pattern.matchesWithinString("before -1 after"), is(true));
		assertThat(pattern.matchesWithinString("before 2 after"), is(true));
		assertThat(pattern.matchesWithinString("before -234 after"), is(true));
		assertThat(pattern.matchesWithinString("before +4 after"), is(true));
		assertThat(pattern.matchesWithinString("before -4 after"), is(true));
		assertThat(pattern.matchesWithinString("before 4.5 after"), is(true));
		assertThat(pattern.matchesWithinString("before -4.5 after"), is(true));
		assertThat(pattern.matchesWithinString("before 02 after"), is(true));
		assertThat(pattern.matchesWithinString("before -0234 after"), is(true));
		assertThat(pattern.matchesWithinString("before 004 after"), is(true));
		assertThat(pattern.matchesWithinString("before _4 after"), is(true));
		assertThat(pattern.matchesWithinString("before A4 after"), is(true));
		assertThat(pattern.matchesWithinString("before A4after"), is(true));
		assertThat(pattern.matchesWithinString("before 2*E10"), is(true));
		assertThat(pattern.matchesWithinString("before"), is(false));

	}

	@Test
	public void testGetAllMatches() {
		// -2 days 00:00:00
		// 1 days 00:00:5.5
		// 0 days 00:00:-5.5
		//
		// ([-+]?\b[1-9]+\d*(\.{1}\d+)?){1}
		Regex regex
				= Regex.startingAnywhere()
						.numberLike().once();

//		System.out.println("REGEX: " + pattern.getRegex());
		List<Match> matches = regex.getAllMatches("-1 2 -234 +4 -4 4.5 FAIL 02 -0234 004 _4 A4");
		assertThat(matches.size(), is(11));
		for (Match match : matches) {
			assertThat(match.getEntireMatch(), isOneOf("-1", "2", "-234", "+4", "-4", "4.5", "02", "-0234", "004", "4", "4"));
		}

		regex
				= Regex.startingAnywhere()
						.number().once();

//		System.out.println("REGEX: " + pattern.getRegex());
		matches = regex.getAllMatches("-1 2 -234 +4 -4 4.5 FAIL 02 -0234 004 _4 A4");
		assertThat(matches.size(), is(6));
		for (Match match : matches) {
			assertThat(match.getEntireMatch(), isOneOf("-1", "2", "-234", "+4", "-4", "4.5"));
		}
//		assertThat(matches, contains("-1", "2", "-234", "+4", "-4", "4.5"));
	}

	@Test
	public void testCaseInsensitiveAndEndOfString() {
		// -2 days 00:00:00
		// 1 days 00:00:5.5
		// 0 days 00:00:-5.5
		//
		// ([-+]?\b[1-9]+\d*(\.{1}\d+)?){1}
		Regex regex
				= Regex.startingAnywhere()
						.wordBoundary()
						.caseInsensitiveGroup()
						.literal("day").once()
						.literal("s").onceOrNotAtAll()
						.caseInsensitiveEnd()
						.wordBoundary()
						.endOfTheString();

		System.out.println("REGEX: " + regex.getRegex());

		assertThat(regex.matchesWithinString("day"), is(true));
		assertThat(regex.matchesWithinString("days"), is(true));
		assertThat(regex.matchesWithinString("DAY"), is(true));
		assertThat(regex.matchesWithinString("DAYS"), is(true));
		assertThat(regex.matchesWithinString("before day"), is(true));
		assertThat(regex.matchesWithinString("before days"), is(true));
		assertThat(regex.matchesWithinString("before middleday"), is(false));
		assertThat(regex.matchesWithinString("before middledays"), is(false));
		assertThat(regex.matchesWithinString("before day after"), is(false));
		assertThat(regex.matchesWithinString("before days after"), is(false));
		assertThat(regex.matchesWithinString("day after"), is(false));
		assertThat(regex.matchesWithinString("days after"), is(false));
		assertThat(regex.matchesWithinString("before"), is(false));
	}

	@Test
	public void testLiteralCaseInsensitive() {
		// -2 days 00:00:00
		// 1 days 00:00:5.5
		// 0 days 00:00:-5.5
		//
		// ([-+]?\b[1-9]+\d*(\.{1}\d+)?){1}
		Regex regex
				= Regex.startingAnywhere()
						.wordBoundary()
						.literalCaseInsensitive("day").once()
						.literalCaseInsensitive("s").onceOrNotAtAll()
						.wordBoundary()
						.endOfTheString();

		System.out.println("REGEX: " + regex.getRegex());

		assertThat(regex.matchesWithinString("day"), is(true));
		assertThat(regex.matchesWithinString("days"), is(true));
		assertThat(regex.matchesWithinString("DAY"), is(true));
		assertThat(regex.matchesWithinString("DAYS"), is(true));
		assertThat(regex.matchesWithinString("before day"), is(true));
		assertThat(regex.matchesWithinString("before days"), is(true));
		assertThat(regex.matchesWithinString("before middleday"), is(false));
		assertThat(regex.matchesWithinString("before middledays"), is(false));
		assertThat(regex.matchesWithinString("before day after"), is(false));
		assertThat(regex.matchesWithinString("before days after"), is(false));
		assertThat(regex.matchesWithinString("day after"), is(false));
		assertThat(regex.matchesWithinString("days after"), is(false));
		assertThat(regex.matchesWithinString("before"), is(false));
	}
	
	@Test
	public void testLotsOfMatchesAndNamedGroups() {
		System.out.println("nz.co.gregs.regexi.RegexTest.testLotsOfMatchesAndNamedGroups()");
		// -2 days 00:00:00
		// 1 days 00:00:5.5
		// 0 days 00:00:-5.5
		//
		// ^(?<interval>((?i)interval(?-i)){1}) {1}(?<value>([-+]?\b[1-9]+\d*(\.{1}\d+)?(((?i)E(?-i)){1}[-+]?[1-9]+\d*(\.{1}\d+)?)?(?!\S)){1}) {1}(?<unit>\w+)$
		Regex pattern
				= Regex.startingAnywhere()
						.namedCapture("interval").literalCaseInsensitive("interval").once().endCapture()
						.space().once()
						.namedCapture("value").numberIncludingScientificNotation().once().endCapture()
						.space().once()
						.namedCapture("unit").word().endCapture();

		System.out.println("REGEX: " + pattern.getRegex());
		String intervalString = "INTERVAL -1.999999999946489E-6 SECOND, INTERVAL 2 DAY, interval 3 month, interval 3 minutes";
		assertThat(pattern.matchesWithinString(intervalString), is(true));
		
		final List<Match> allMatches = pattern.getAllMatches(intervalString);
		for (var entry : allMatches) {
			System.out.println("MATCH: "+entry.getEntireMatch());
		}
		final HashMap<String, String> allGroups = pattern.getAllNamedGroups(intervalString);
		for (var entry : allGroups.entrySet()) {
			System.out.println("GROUP: "+entry.getKey()+"="+entry.getValue());
		}
		assertThat(allGroups.size(), is(3));
		final Double value = Double.valueOf(allGroups.get("value"));
		assertThat(value, is(Double.valueOf("-1.999999999946489E-6")));
		assertThat(Math.round(value * 1000000), is(-2L));
	}
}
