=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: akhilvch
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Inheritance
 
  Inheritance is used when I extend two types of enemies, i.e. EnemyShip and Advanced ship from the main, basic enemy, which is an Asteroid. So basically, every EnemyShip and AdvancedShip moves
  the same way an Asteroid moves but they have different shooting methods and different drawing methods. In the case of the Asteroid, there is no shooting method at all. In the case of EnemyShip, the 
  shooting method works by aiming a bullet randomly every single second. In the case of the AdvancedShip, the bullets are slower and they are aimed directly at the player. The reason Inheritance was
  used with respect to these game characters is that they are all enemies and therefore they all use the same isHit() methods and also use a LinkedList to keep track of Bullets(). 
  But because of their differences in attacking, Inheritance was used to make the code more efficient. 
  
  2. Collections
  A LinkedList was used to store enemies and to store the bullets for the player spaceship and for each of the enemy spaceships. The reason for using linkedlists was that they keep track of which element
   was first put and in and removes the element that was last put in (when we do a simple .remove() method rather than the specific remove(int index)). The reason this was convenient
  in the case of bullets was that all bullets would continuously be displayed and removed only when the bullets exited the screen. Everytime a mouse is clicked a bullet gets added onto the screen 
  from the player's spaceship in the direction of the mouseclick and once it exited the screen, it was removed from the linkedlist. Likewise bullets were added every second for the enemy ships and 
  were removed in a similar manner as the bullets the player shot.  In the case of the enemies, I added to the linkedlist in the order of difficulty. In other words, the asteroids were added first, 
  then the EnemyShips, and then the AdvancedShips. This allowed for easier JUnit testing because I could know exactly what type the first element of the list was. Furthermore, enemy removal was relatively efficient. 
  

  3. FileIO
  FileIO is used to save the game state to a text file, load the game from a text file, and to read the number of enemies of each type off a text file. When the game state is saved to a file, the 
  velocities and the positions of the player and the velocities and positions of each of the enemies are saved to a text file. The use of a BufferedWriter enables this to happen while a BufferedReader
  rereads the same textfile, parsing through each line with a BufferedReader. The fileformat in the save is structured with a word denoting what character's attributes are actually saved 
  followed by numbers split with a colon. The string method .split() then splits the line into different strings, many of which have numbers. Those which have numbers are then parsed. 

  4. JUnit Test Cases
  JUnited Test Cases are used to test the game state. I ensure that the levels are being updated, that the player dies when he is supposed to, that when all the enemies die the game is over, etc. 
  For more information on individual test cases, feel free to refer to the GameTest.java class

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  GameObj.java: This is the class that stores many of the same methods and fields that are inherent to all of the objects, such as set position, set velocity, intersects, etc. The function is to store methods and use inheritance
  to avoid redundant code and also make the game more efficient. 
  
  GameCourt.java: This is the class that handles the game state itself. It checks whether a player is dead, whether a game is done, whether a level is done, contains the tick method etc. It's function, again, is to handle the 
  game state. 

  Game.java: This is the class that creates all of the JButtons and JLabels that are displayed onto the screen as well as the frame itself. The function is to allow the player to interact with the game itself and to see the 
  score and health of the spaceship he plays as. 
  
  Asteroid.java: This is the class that instantiates the primary enemy in the game, which is a simple asteroid. If it hits the player, the player loses 30 health. It also has some graphic methods such as isHit() which decreases the
  health of the asteroid by 10 everytime a bullet hits it. EnemyShip and AdvancedShip inherit this class for such methods.  
  
  EnemyShip.java: This class instantiates the second hardest enemy in a game, which is an enemyship that shoots green bullets randomly every single second. It's purpose is to kill the player through colliding with him or by shooting him down. 
  
  AdvancedShip.java: This class instantiates the third hardest enemy in the game, which is an enemyship that shoots green bullets at the player every single second. It's purpose is to kill the player through colliding with him or by shooting him down. 
  
  SpaceShip.java: This class instantiates the spaceShip that the player plays as throughout the game. There are some similar methods to an enemy such as the isHit method which subtracts the health by 10 and the updateCollision method which
  subtracts the health by 30. 
  
  Bullet.java: This class instantiates every single bullet that is fired, either by an Enemy or by the player. The purpose is to update the position of the bullet and to instantiate each bullet. 
  
  GameTest.java: This class is where all of the JUNit tests are stored. Purpose again, is to test the gamestate. 
  
  
- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
 
 Not really. One problem that recurred was how to configure the nextLevel button to show up only when the next level was pressed. I fixed this issue using a timer, similar to tick, and checking when a boolean field in the game 
 switched state (levelDone) and display the Button based on that. 

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I believe that there is a good separation of functionality. The GameCourt class handles most of the state while the game objects contain most of the methods that pertain to them. Furthermore. the Game.java class also handles most of
the Swing components such as the Buttons and the Labels. The private state is encapsulated quite well, and most fields require a getter/setter to access them. I would not change much to be honest if given the chance. Maybe add more to
the game itself and make specific methods/components more efficient? 




========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
 
 Google Images
 
 
