package gridhunters;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Michael Martin
 */
public class EnemyTest {
    Enemy enemy;

    public EnemyTest() {}

    @Before
    public void setUp() {
        enemy = new Enemy(100, 10, false);
    }

    /**
     * Test of reset method, of class Enemy.
     */
    @Test
    public void testReset() {
        System.out.println("reset");
        
        enemy.setHealth(50);
        assertEquals(50, enemy.getHealth());
        
        enemy.reset();
        assertEquals(100, enemy.getHealth());
    }

    /**
     * Test of takeDamage method, of class Enemy.
     */
    @Test
    public void testTakeDamage() {
        System.out.println("takeDamage");
        
        assertEquals(100, enemy.getHealth());
        
        enemy.takeDamage(50);
        assertEquals(50, enemy.getHealth());
    }

    /**
     * Test of getAttack method, of class Enemy.
     */
    @Test
    public void testGetAttack() {
        System.out.println("getAttack");
        
        assertEquals(10, enemy.getAttack());
    }
}
