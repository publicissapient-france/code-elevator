package elevator.server;

import elevator.logging.ElevatorLogger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlainFileStorage implements StorageService {
	/* NB: les méthodes privées ne sont pas thread-safe ; s'il y a risque de concurrence, il faut les appeler depuis une méthode thread-safe. */
	private static Logger LOG = new ElevatorLogger("PlainFileStorage").logger();
	static final File SCORE_FILE = new File("flat_scores");

	private static final int DEFAULT_SYNC_DELAY = 30000; //milliseconds
	private static final int DEFAULT_WBUFFER_SZ = 65536;

	final Map<String, Record> cache = new HashMap<>();
	private long lastSync = System.currentTimeMillis();
	long syncDelay = DEFAULT_SYNC_DELAY;
	int wBufferSize = DEFAULT_WBUFFER_SZ;

	private RandomAccessFile scoreFile = null;

	static class Record{
		private static final String DATE_FORMAT = "yyyyMMdd HH:mm:ss";
		private static final Pattern RECORD_REGX = Pattern.compile("^(.*?)\\t(.*?)\\t(.*?)$");
		private final Score score;
		private final String playerEmail;

		Record(Score s, Player p){
			this(s, p.email);
		}

		Record(Score s, String playerEmail){
			this.playerEmail = playerEmail;
			this.score = new Score(s.score, s.started); //must keep a COPY for latter comparison
		}

		public Record(Record existing) {
			this.score = existing.score;
			this.playerEmail = existing.playerEmail;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Record record = (Record) o;

			if (playerEmail != null ? !playerEmail.equals(record.playerEmail) : record.playerEmail != null) return false;

			return true;
		}

		@Override
		public int hashCode() {
			return playerEmail != null ? playerEmail.hashCode() : 0;
		}

		public String toString(){
			return String.format("%s\t%s\t%s\n", playerEmail, score.score, DateTimeFormat.forPattern(DATE_FORMAT).print(score.started));
		}

		public static Record fromString(final String recordLine){
			final Matcher recordMatcher = RECORD_REGX.matcher(recordLine);
			try {
				if(recordMatcher.matches()){
					return new Record(
							new Score(Integer.valueOf(recordMatcher.group(2)), DateTime.parse(recordMatcher.group(3), DateTimeFormat.forPattern(DATE_FORMAT))),
							recordMatcher.group(1)
					);
				}
			} catch (NumberFormatException e) {
				LOG.log(Level.SEVERE, "Cannot parse line to record: " + recordLine, e);
			}
			return null;
		}

	}

	public PlainFileStorage(){
		try{
			if(!SCORE_FILE.createNewFile()){
				readScoreFile();
			}
		}catch (IOException e){
			LOG.log(Level.SEVERE, "PlainFileStorage unworkable !", e);
		}

	}

	private void readScoreFile() {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(SCORE_FILE), "UTF-8"))){
			String line;
			while((line = reader.readLine()) != null){
				final Record record = Record.fromString(line);
				cache.put(record.playerEmail, record);
			}
		}catch (IOException e){
			LOG.log(Level.SEVERE, "Could not read score file", e);
		}
	}

	@Override
	public synchronized void saveScoreIfBetter(final Score score, final Player p) {
		Record existing = cache.get(p.email);
		if(existing==null){
			cache.put(p.email, new Record(score, p));
			writeScoreFileIfNeeded();
		}else if(existing.score.score < score.score){
			cache.put(p.email, new Record(score, existing.playerEmail));
			writeScoreFileIfNeeded();
		}
	}

	private boolean syncNeeded(){
		return System.currentTimeMillis() - lastSync >= syncDelay;
	}

	private FileChannel getWriteChannel() throws IOException{
		if(scoreFile == null){
			scoreFile = new RandomAccessFile(SCORE_FILE, "rw");
		}
		return  scoreFile.getChannel();
	}

	public synchronized void forceScoreReload(){
		try{
			if(scoreFile!=null){
				scoreFile.close();
				scoreFile = null;
				cache.clear();
			}
			readScoreFile();
		}catch (IOException ex){
			LOG.log(Level.SEVERE, "Failed to reload score file", ex);
		}
	}

	private void writeScoreFileIfNeeded() {
		if(syncNeeded()){
			try{
				FileChannel chan = getWriteChannel();
				chan.position(0);
				ByteBuffer buff = ByteBuffer.allocate(wBufferSize);
				buff.clear();

				for(Record rec: cache.values()){
					String recLine = rec.toString();
					if(recLine.length() > buff.capacity()){
						LOG.log(Level.WARNING, "writeScoreFileIfNeeded: discarded too long record: "+recLine);
					}
					if(recLine.length()>buff.remaining() && buff.position() > 0){
						buff.flip();
						chan.write(buff);
						buff.clear();
					}
					buff.put(recLine.getBytes("UTF-8"));
				}
				if(buff.position()>0){
					buff.flip();
					chan.write(buff);
				}
				lastSync = System.currentTimeMillis();
				if(LOG.isLoggable(Level.FINE)){
					LOG.fine("writeScoreFileIfNeeded: wrote " + chan.position() + " Bytes");
				}
			}catch (IOException e){
				LOG.log(Level.SEVERE, "Could not write score file", e);
			}
		}
	}

	@Override
	public boolean hasScored(Player p) {
		return cache.containsKey(p.email);
	}

	@Override
	public Score getScore(Player p) {
		return getScore(p.email);
	}

	@Override
	public Score getScore(String playerId) {
		if(cache.containsKey(playerId)){
			return cache.get(playerId).score;
		}
		return null;
	}

	@Override
	public Map<String, Score> getAllScores() {
		final Collection<Record> records = cache.values();
		//My kinkdom for a functionnal language !! -snif
		final Map<String, Score> scores = new HashMap<>(records.size());
		for(Record rec: records){
			scores.put(rec.playerEmail, rec.score);
		}
		return scores;
	}
}
