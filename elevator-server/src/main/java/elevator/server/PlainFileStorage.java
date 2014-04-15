package elevator.server;

import elevator.logging.ElevatorLogger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlainFileStorage implements StorageService {
	/* NB: les méthodes privées ne sont pas thread-safe ; s'il y a risque de concurrence, il faut les appeler depuis une méthode thread-safe. */
	private static Logger LOG = new ElevatorLogger("PlainFileStorage").logger();
	static final File SCORE_FILE = new File("flat_scores");

	private static final int DEFAULT_SYNC_DELAY = 30000; //milliseconds
	private static final int DEFAULT_WBUFFER_SZ = 65536;

	final Map<String, ScoreInfo> cache = new HashMap<>();
	private long lastSync = System.currentTimeMillis();
	long syncDelay = DEFAULT_SYNC_DELAY;
	int wBufferSize = DEFAULT_WBUFFER_SZ;

	private RandomAccessFile scoreFile = null;

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
				final ScoreInfo record = ScoreInfo.fromString(line);
				if(record!=null){
					cache.put(record.email, record);
				}else{
					LOG.log(Level.WARNING, "Didn't returned a ScoreInfo: "+line);
				}
			}
		}catch (IOException e){
			LOG.log(Level.SEVERE, "Could not read score file", e);
		}
	}

	@Override
	public synchronized void saveScoreIfBetter(final Score score, final Player p) {
		ScoreInfo existing = cache.get(p.email);
		if(existing==null){
			cache.put(p.email, new ScoreInfo(p, score));
			writeScoreFileIfNeeded();
		}else if(existing.score < score.score){
			cache.put(p.email, new ScoreInfo(existing.pseudo, existing.email, score));
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

				for(ScoreInfo record: cache.values()){
					byte [] recBytes = record.toString().getBytes("UTF-8");
					if(recBytes.length > buff.capacity()){
						LOG.log(Level.WARNING, "writeScoreFileIfNeeded: discarded too long record: "+record);
						continue;
					}
					if(recBytes.length>buff.remaining() && buff.position() > 0){
						buff.flip();
						chan.write(buff);
						buff.clear();
					}
					buff.put(recBytes);
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
	public ScoreInfo getScore(Player p) {
		return getScore(p.email);
	}

	@Override
	public ScoreInfo getScore(String playerId) {
		if(cache.containsKey(playerId)){
			return cache.get(playerId);
		}
		return null;
	}

	@Override
	public List<ScoreInfo> getAllScores() {
		final Collection<ScoreInfo> records = cache.values();
		return new ArrayList<>(records);
	}
}
