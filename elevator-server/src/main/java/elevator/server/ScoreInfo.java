package elevator.server;

import elevator.logging.ElevatorLogger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Public immutable info for hall of fame.
 */
public class ScoreInfo {
	private static final Pattern READ_REGX = Pattern.compile("^(.*?)\\t(.*?)\\t(.*?)\\t(.*?)$");
	private static final String DATE_FORMAT = "yyyyMMdd HH:mm:ss";
	private static final Pattern WRITE_FORMAT = Pattern.compile("%s\t%s\t%s\t%s\n");
	private static Logger LOG = new ElevatorLogger("ScoreInfo").logger();

	String pseudo;
	String email;
	Integer score;
	DateTime startTime;

	public ScoreInfo(String pseudo, String email, Integer score, DateTime startTime) {
		this.pseudo = pseudo == null ? "" : pseudo;
		this.email = email;
		this.score = score;
		this.startTime = startTime;
	}

	public ScoreInfo(Player p, Score s){
		this(p.pseudo, p.email, s.score, s.started);
	}

	public ScoreInfo(String pseudo, String email, Score s){
		this(pseudo, email, s.score, s.started);
	}

	public String getPseudo() {
		return pseudo;
	}

	public String getEmail() {
		return email;
	}

	public Integer getScore() {
		return score;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ScoreInfo scoreInfo = (ScoreInfo) o;

		if (!email.equals(scoreInfo.email)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return email.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s\t%s\t%s\t%s\n", pseudo, email, score, DateTimeFormat.forPattern(DATE_FORMAT).print(startTime));
	}

	public static ScoreInfo fromString(String line){
		final Matcher infoMatcher = READ_REGX.matcher(line);
		try {
			if(infoMatcher.matches()){
				return new ScoreInfo(infoMatcher.group(1), infoMatcher.group(2), Integer.parseInt(infoMatcher.group(3)),
						DateTime.parse(infoMatcher.group(4), DateTimeFormat.forPattern(DATE_FORMAT)));
			}
		} catch (IllegalArgumentException e) {
			LOG.log(Level.SEVERE, "Cannot parse line: " + line, e);
		}
		LOG.log(Level.SEVERE, "Bad line format (expected: \"%s\\t%s\\t%s\\t%s\\n\"): " + line);
		return null;
	}

	public static class ByScore implements Comparator<ScoreInfo>{
		boolean ascending;
		public ByScore ascending(){
			this.ascending = true;
			return this;
		}
		public ByScore descending(){
			this.ascending = false;
			return this;
		}
		@Override
		public int compare(ScoreInfo o1, ScoreInfo o2) {
			if(o1 == null){
				if(o2 == null){
					return 0;
				}
				return ascending ? Integer.MIN_VALUE : Integer.MAX_VALUE;
			}
			if(o2 == null){
				return ascending ? Integer.MAX_VALUE : Integer.MIN_VALUE;
			}
			return ascending ? (Math.round(o1.score / 2F) - Math.round(o2.score / 2F)) :
					(Math.round(o2.score / 2F) - Math.round(o1.score / 2F));
		}
	}

	public static Comparator<ScoreInfo> byScore(){
		return new ByScore().descending();
	}
}
