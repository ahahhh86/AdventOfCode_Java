package aoc;

public class Date {
	static public enum Day {
		DAY01,
		DAY02,
		DAY03,
		DAY04,
		DAY05,
		DAY06,
		DAY07,
		DAY08,
		DAY09,
		DAY10,
		DAY11,
		DAY12,
		DAY13,
		DAY14,
		DAY15,
		DAY16,
		DAY17,
		DAY18,
		DAY19,
		DAY20,
		DAY21,
		DAY22,
		DAY23,
		DAY24,
		DAY25;

		@Override
		public String toString() {
			return "Day " + name().substring(3);
		}

		public String toFilename() {
			return name() + ".txt";
		}
	}



	static public enum Year {
		YEAR2015,
		YEAR2016,
		YEAR2017,
		YEAR2018,
		YEAR2019,
		YEAR2020,
		YEAR2021,
		YEAR2022,
		YEAR2023;

		@Override
		public String toString() {
			return "Year " + name().substring(4);
		}

		public String toFilename() {
			return name().substring(4) + "/";
		}
	}



	public Year year;
	public Day day;

	public Date(Year year, Day day) {
		this.year = year;
		this.day = day;
	}

	@Override
	public String toString() {
		return year + " | " + day;
	}
}