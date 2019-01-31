import static org.junit.Assert.*;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.Test;
public class GameTest {
	final JLabel status = new JLabel("You Ready?");
	@Test
    public void nullArgumentOneGameCourt() throws IOException{
        try {
            GameCourt court = new GameCourt(null, "src");
            fail("Expected an IllegalArgumentException - cannot create FileCorrector with null.");
        } catch (IllegalArgumentException f) {    
        }
	}
	
	@Test
    public void nullArgumentTwoGameCourt() throws IOException{
        try {
            GameCourt court = new GameCourt(status, null);
            fail("Expected an IllegalArgumentException - cannot create FileCorrector with null.");
        } catch (IllegalArgumentException f) {    
        }
	}
	
	
	@Test
	public void startMethodWorks() {
		final GameCourt court = new GameCourt(status, "Files/Levels.txt");
		court.start(); 
		 assertEquals(0, court.getScore()); 
		 assertEquals(2, court.getEnemies().size()); 
		 assertFalse(court.getIsSaved()); 
		 assertFalse(court.getLevelDone()); 
		 assertNotEquals(null, court.getPlayer()); 
		 assertEquals(60, court.getPlayer().getHealth());
		 assertEquals(Color.BLACK, court.getPlayer().getColor());
		 assertEquals(0, (int)court.getPlayer().getVx());
		 assertEquals(0, (int)court.getPlayer().getVy());
		 assertEquals(1, court.getLevel());
	}
	
	@Test
	public void testReset() {
		final GameCourt court = new GameCourt(status, "Files/Levels.txt"); 
		court.start(); 
		court.nextLevel();
		court.nextLevel(); 
		court.reset();
		 assertEquals(0, court.getScore()); 
		 assertEquals(2, court.getEnemies().size()); 
		 assertFalse(court.getIsSaved()); 
		 assertFalse(court.getLevelDone()); 
		 assertNotEquals(null, court.getPlayer()); 
		 assertEquals(60, court.getPlayer().getHealth());
		 assertEquals(Color.BLACK, court.getPlayer().getColor());
		 assertEquals(0, (int)court.getPlayer().getVx());
		 assertEquals(0, (int)court.getPlayer().getVy());
		 assertEquals(1, court.getLevel());
	}
	
	@Test
	 public void nullNextLevel() throws NullPointerException{
        try {
        	final GameCourt court = new GameCourt(status, "Files/Levels.txt"); 
            court.nextLevel(); 
            fail("Expected a NullPointerException - need to call reset before advancing levels");
        } catch (NullPointerException f) {  
        	
        }
	}
	
	@Test
	 public void nextLevelValidTest() throws NullPointerException{
       try {
       	final GameCourt court = new GameCourt(status, "Files/Levels.txt"); 
       	   court.start(); 
           court.nextLevel(); 
           court.nextLevel(); 
           assertFalse(court.getLevelDone()); 
           assertEquals(6, court.getEnemies().size());
           court.nextLevel(); 
           court.nextLevel(); 
           assertEquals(4, court.getEnemies().size());
           for(int i = 0; i < 5; i++) {
        	   court.nextLevel(); 
           }
           court.nextLevel(); 
           assertTrue(court.getGameDone()); 
       } catch (NullPointerException f) {  
       	
       }
	}
	
	@Test
	public void testSave() throws IOException, InterruptedException {
		final GameCourt court = new GameCourt(status, "Files/Levels.txt"); 
		court.start(); 
		court.nextLevel(); 
		court.nextLevel(); 
		court.nextLevel();
		TimeUnit.SECONDS.sleep(4);
		court.save(); 
		BufferedReader r = new BufferedReader(new FileReader("Files/SavedGame.txt")); 
		assertTrue(r.readLine().contains(String.valueOf(court.getLevelDone()))); 
		assertTrue(r.readLine().contains(String.valueOf(court.getScore()))); 
		assertTrue(r.readLine().contains(String.valueOf(court.getHealth())));
		assertTrue(r.readLine().contains(String.valueOf(court.getLevel())));
		assertTrue(r.readLine().contains(String.valueOf(court.getPlayer().getPx()))); 
		assertTrue(r.readLine().contains(String.valueOf(court.getPlayer().getPy()))); 
		assertTrue(r.readLine().contains(String.valueOf(court.getPlayer().getVx()))); 
		assertTrue(r.readLine().contains(String.valueOf(court.getPlayer().getVy()))); 
		r.close(); 
	}
	
	@Test
	public void testLoadWorking() throws FileNotFoundException, InterruptedException{
		final GameCourt court = new GameCourt(status, "Files/Levels.txt");
		court.start();
		court.nextLevel(); 
		court.nextLevel(); 
		TimeUnit.SECONDS.sleep(4);
		int px = (int) court.getEnemies().get(0).getAccX();
		int py = (int) court.getEnemies().get(1).getAccY();
		court.save(); 
		court.load(); 
		assertEquals(0, (int)court.getPlayer().getVx()); 
		assertEquals(0, (int)court.getPlayer().getVy()); 
		assertEquals(px, (int) court.getEnemies().get(0).getAccX());
		assertEquals(py, (int) court.getEnemies().get(1).getAccY());
		
	}
	
	
	@Test 
	public void checkWhenLevelDone() throws InterruptedException{
		final GameCourt court = new GameCourt(status, "Files/Levels.txt");
		court.start();
		TimeUnit.SECONDS.sleep(1);
		court.testLevelDone();
		TimeUnit.SECONDS.sleep(1);
		assertTrue(court.getLevelDone());
	}
	
	@Test 
	public void levelDoneIsFalse() throws InterruptedException{
		final GameCourt court = new GameCourt(status, "Files/Levels.txt");
		court.start();
		TimeUnit.SECONDS.sleep(1);
		assertFalse(court.getLevelDone());
	}
	
	@Test 
	public void testGameDoneWhenPlayerDiesWithHit() throws InterruptedException {
		final GameCourt court = new GameCourt(status, "Files/Levels.txt");
		court.start();
		for(int i = 0; i < 7; i++){
			court.testHealth(); 
		}
		TimeUnit.SECONDS.sleep(1);
		assertTrue(court.getGameDone());	
	}
	@Test 
	
	public void testGameDoneWhenPlayerDiesWithCollisions() throws InterruptedException {
		final GameCourt court = new GameCourt(status, "Files/Levels.txt");
		court.start();
		court.testCollisions(); 
		TimeUnit.SECONDS.sleep(1);
		assertTrue(court.getGameDone());	
	}
	
	@Test
	public void testGameDoneWhenPlayerWins() {
		final GameCourt court = new GameCourt(status, "Files/Levels.txt");
		court.start();
		for(int i = 0; i < 10; i++) {
     	   court.nextLevel(); 
        }
        assertTrue(court.getGameDone()); 
	}

	@Test
	public void testGameNotDoneWhenPlayerDoesntWin() {
		final GameCourt court = new GameCourt(status, "Files/Levels.txt");
		court.start();
		for(int i = 0; i < 8; i++) {
     	   court.nextLevel(); 
        }
        assertFalse(court.getGameDone()); 
	}
	@Test
	public void testResetFailWhenLevelDone() throws InterruptedException {
		final GameCourt court = new GameCourt(status, "Files/Levels.txt");
		court.start();
		TimeUnit.SECONDS.sleep(1);
		court.testLevelDone();
		TimeUnit.SECONDS.sleep(1);
		court.reset(); 
		assertEquals(0, court.getEnemies().size()); 
	}
	
	@Test
	public void testResetStartWorksWhenLevelDone() throws InterruptedException {
		final GameCourt court = new GameCourt(status, "Files/Levels.txt");
		court.start();
		TimeUnit.SECONDS.sleep(1);
		court.testLevelDone();
		TimeUnit.SECONDS.sleep(1);
		court.start(); 
		assertEquals(2, court.getEnemies().size()); 
	}
	
	@Test
	public void testResetStartWorksWhenGameDone() {
		final GameCourt court = new GameCourt(status, "Files/Levels.txt");
		court.start();
		for(int i = 0; i < 10; i++) {
     	   court.nextLevel(); 
        }
		court.start(); 
		assertEquals(2, court.getEnemies().size()); 
		assertFalse(court.getGameDone());
	}
	
	@Test
	public void testTickFailWhenPlayingFalse() throws InterruptedException {
		final GameCourt court = new GameCourt(status, "Files/Levels.txt");
		court.start();
		court.nextLevel();
		LinkedList<Asteroid> tempList = court.getEnemies();
		double oldPosition = tempList.get(0).getPx(); 
		court.setPlaying(false);
		TimeUnit.SECONDS.sleep(1);
		double newPosition = tempList.get(0).getPx(); 
		assertEquals((int) oldPosition, (int) newPosition); 
	}
	
	@Test
	public void testTickTrueWhenPlayingTrue() throws InterruptedException {
		final GameCourt court = new GameCourt(status, "Files/Levels.txt");
		court.start();
		court.nextLevel();
		LinkedList<Asteroid> tempList = court.getEnemies();
		double oldPosition = tempList.get(0).getPx(); 
		TimeUnit.SECONDS.sleep(1);
		double newPosition = tempList.get(0).getPx(); 
		assertNotEquals((int) oldPosition, (int) newPosition); 
	}
	
	//@Test
	//public 
	
}