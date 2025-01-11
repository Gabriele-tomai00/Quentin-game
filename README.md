![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-007396?style=for-the-badge&logo=java&logoColor=white)

# **Quentin**

[![Java CI with Gradle](https://github.com/Gabriele-tomai00/Quentin-game/actions/workflows/gradle.yml/badge.svg)](https://github.com/Gabriele-tomai00/Quentin-game/actions/workflows/gradle.yml)

Quentin is a drawless connection game for two players: Black and White. It's played on the intersections (points) of a square board, which is initially empty. The top and bottom edges of the board are colored black; the left and right edges are colored white. Luis Bolaños Mures designed Quentin in April, 2012.

---

## **Features**
- Play as either Black or White.
- Simple rules with deep strategic gameplay.
- Designed to run on any platform with Java.

---

## **Instructions**
For detailed instructions on how to play Quentin, refer to the [Instructions File](instructions.md).

---

## **Installation**

### **Prerequisites**
1. Ensure you have [Java 17 or higher](https://adoptopenjdk.net/) installed.
2. Install [Gradle](https://gradle.org/install/) or use the Gradle wrapper provided in the project.

### **Steps**
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/quentin.git
   cd quentin
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

---

## **Execution**

1. Run the application using Gradle:
   ```bash
   ./gradlew run
   ```

2. Alternatively, execute the JAR file (after building):
   ```bash
   java -jar app/build/libs/app.jar
   ```

---

## **Uninstalling**

To uninstall Quentin, simply delete the cloned repository:
```bash
rm -rf quentin
```

If you also built the project, you can remove the Gradle build files:
```bash
rm -rf ~/.gradle/caches
```
