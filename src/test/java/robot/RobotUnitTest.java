
package robot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static robot.Direction.*;

public class RobotUnitTest {
    private LandSensor capteurSol;
    private Battery piles;
    private RoadBookCalculator routeCalculator;

    @Before
    public void setUp(){
        piles= Mockito.mock(Battery.class);
        routeCalculator= Mockito.mock(RoadBookCalculator.class);
        capteurSol= Mockito.mock(LandSensor.class);
    }

    @Test
    public void testLand() throws UnlandedRobotException, LandSensorDefaillance {
        //---DEFINE---
        Robot robot = new Robot(1, piles, routeCalculator);
        //---WHEN---
        robot.land(new Coordinates(3,0), capteurSol);
        //---THEN---
        Assert.assertEquals(NORTH, robot.getDirection());
        Assert.assertEquals(3, robot.getXposition());
        Assert.assertEquals(0, robot.getYposition());
    }


    // tester l'apparition d'une exception, l'annotation @Test intègre expected suivi de la classe de l'exception attendue
    // Attention : il est parfois nécessaire de s'assurer que l'exception n'apparaît pas avant la dernière instruction du test
    @Test (expected = UnlandedRobotException.class)
    public void testRobotMustBeLandedBeforeMoveForward() throws Exception {
        Robot robot = new Robot(1, piles, routeCalculator);
        robot.moveForward();
    }


    @Test
    public void testMoveForward() throws Exception {
        //---DEFINE---
        Robot robot = new Robot(1, piles, routeCalculator);
        robot.land(new Coordinates(5, 5), capteurSol);
        int currentXposition = robot.getXposition();
        int currentYposition = robot.getYposition();
        //---WHEN---
        Mockito.when(piles.canDeliver(anyDouble())).thenReturn(true);
        robot.moveForward();
        //---THEN---
        Assert.assertEquals(5, robot.getXposition());
        Assert.assertEquals(4, robot.getYposition());
    }


    @Test (expected = UnlandedRobotException.class)
    public void testRobotMustBeLandedBeforeMoveBackward() throws Exception {
        Robot robot = new Robot(1, piles, routeCalculator);
        robot.moveBackward();
    }

    @Test
    public void testMoveBackward() throws Exception {
        //---DEFINE---
        Mockito.when(piles.canDeliver(anyDouble())).thenReturn(true);
        Robot robot = new Robot(1, piles, routeCalculator);
        robot.land(new Coordinates(3, 0), capteurSol);
        int currentXposition = robot.getXposition();
        int currentYposition = robot.getYposition();
        //---WHEN---
        robot.moveBackward();
        //---THEN---
        Assert.assertEquals(currentXposition, robot.getXposition());
        Assert.assertEquals(currentYposition+1, robot.getYposition());
    }

    @Test (expected = UnlandedRobotException.class)
    public void testRobotMustBeLandedBeforeTurnLeft() throws Exception {
        Robot robot = new Robot(1, piles, routeCalculator);
        robot.turnLeft();
    }

    @Test
    public void testTurnLeft() throws Exception {
        Robot robot = new Robot(1, piles, routeCalculator);
        robot.land(new Coordinates(3, 0), capteurSol);
        robot.turnLeft();
        Assert.assertEquals(WEST, robot.getDirection());
        robot.turnLeft();
        Assert.assertEquals(SOUTH, robot.getDirection());
        robot.turnLeft();
        Assert.assertEquals(EAST, robot.getDirection());
        robot.turnLeft();
        Assert.assertEquals(NORTH, robot.getDirection());
    }

    @Test (expected = UnlandedRobotException.class)
    public void testRobotMustBeLandedBeforeTurnRight() throws Exception {
        Robot robot = new Robot(1, piles, routeCalculator);
        robot.turnRight();
    }

    @Test
    public void testTurnRight() throws Exception {
        Robot robot = new Robot(1, piles, routeCalculator);
        robot.land(new Coordinates(3, 0), capteurSol);
        robot.turnRight();
        Assert.assertEquals(EAST, robot.getDirection());
        robot.turnRight();
        Assert.assertEquals(SOUTH, robot.getDirection());
        robot.turnRight();
        Assert.assertEquals(WEST, robot.getDirection());
        robot.turnRight();
        Assert.assertEquals(NORTH, robot.getDirection());
    }

    @Test (expected = UndefinedRoadbookException.class)
    public void testLetsGoWithoutRoadbook() throws Exception {
        Robot robot = new Robot(1, piles, routeCalculator);
        robot.land(new Coordinates(3, 0), capteurSol);
        robot.letsGo();
    }

    @Test
    public void testLetsGo() throws Exception {
        Mockito.when(piles.canDeliver(anyDouble())).thenReturn(true);
        Robot robot = new Robot(1, piles, routeCalculator);
        robot.land(new Coordinates(5, 7), capteurSol);
        robot.setRoadBook(new RoadBook(Arrays.asList(Instruction.FORWARD, Instruction.FORWARD, Instruction.TURNLEFT, Instruction.FORWARD)));
        robot.letsGo();
        Assert.assertEquals(7, robot.getXposition());
        Assert.assertEquals(6, robot.getYposition());
    }

    @Test (expected = UnlandedRobotException.class)
    public void testComputeRoadToWithUnlandedRobot() throws Exception {
        Robot robot = new Robot(1, piles, routeCalculator);
        robot.computeRoadTo(new Coordinates(3, 5));
    }

    @Test
    public void testComputeRoadTo() throws Exception {
        Robot robot = new Robot(1, piles, routeCalculator);
        robot.land(new Coordinates(3, 0), capteurSol);
        robot.computeRoadTo(new Coordinates(7, 5));
        // le problème ici est l'observation.
        // La méthode computeRoadTo calcule et met à jour un attribut du robot sans donner accès au résultat.
        // La seule méthode permettant l'accès au roadbook est letsGo et l'observation se limite à la position finale du robot
        //robot.letsGo();
        Assert.assertEquals(3, robot.getXposition());
        Assert.assertEquals(0, robot.getYposition());

    }}

