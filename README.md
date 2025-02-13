# **Bouncing Ball**  

Bouncing Ball is my first bigger **JavaFX** project, where almost the entire mathematical background was coded from scratch. It consists of a **game-like simulation** of elastic collisions and a **level creator** for designing custom game boards.  

## **2. How to Run the Application**  

To run the application, ensure you have **JVM 21** installed on your computer. Then:  

1. Download the entire repository.  
2. Run the newest version of the `.jar` file.  

⚠ **Note:** The **level creator** currently only runs properly when executed from **IntelliJ IDEA** (to be fixed). Run it using:  
`org.zeros.bouncy_balls.Applications.CreatorApplication.LevelCreatorApplication.java`  

## **3. Project Structure**  

The project consists of two major parts:  

### **Bouncy Balls Game**  
A game-like simulation of **elastic collisions** and **object movements** in a 2D plane. The physics engine considers:  
- **Gravitational force** and **friction** (optional).  
- **Precise collision calculations** (assuming uniform rectilinear motion between frames).  
- **Level objectives**: Move all **yellow objects** into the target area while preventing **red objects** from entering it.  

### **Level Creator**  
A tool for designing custom game boards. Features include:  
- Creating **simple shapes and closed polylines**.  
- **Boolean algebra operations** on shapes to form complex areas.  
- The resulting game board consists of calculated **vector boundaries** rather than overlapping shapes.  

## **4. Future Enhancements**  
This is the **first test version** of the project, prepared for future extensions, such as:  

- Adding **different shapes** of moving objects.
- Adding **more levels** to make it truely playable game
- Implementing **electromagnetic forces** between objects.  
- Introducing **leaderboards and user accounts**.  
- And more...  

---
