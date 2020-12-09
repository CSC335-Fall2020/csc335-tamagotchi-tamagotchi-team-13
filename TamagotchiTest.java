import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

class TamagotchiTest {
	
    @Test
    void testBasicMethods() throws IOException {
        TamagotchiModel testModel = new TamagotchiModel();
        TamagotchiController testController = 
                new TamagotchiController(testModel);
        assertFalse(testController.isDead());
        testController.incrementAge(1);
        testController.decrementHealth(1);
        testController.feedPetMeal();
        testController.feedPetSnacks();
        testController.giveMedicine();
        testController.pause();
        testController.unPause();
        testController.getAgeDescription();
        testController.getHealthDescription();
        testController.getWeightDescription();
        testController.getHappinessDescription();
        testController.getWeight();
        testController.getHealth();
        testController.getHappiness();
        testController.getAge();
        testController.petDies();
        testController.resumeGame();
        testController.shouldGetSick();
        testController.setPet("./Pet Images/transparent_panda.png",
                "./Pet Images/sad_panda.png");
        assertEquals(testController.getSadPet(),
                "./Pet Images/sad_panda.png");
        assertEquals(testController.getPet(),
                "./Pet Images/transparent_panda.png");
        
        testController.save();

        assertTrue(testController.isDead());
    }
    
    @Test
    void testAdvMethods() throws IOException {
        TamagotchiModel testModel = new TamagotchiModel("Unnamed",5,50,20,10);
        TamagotchiController testController = 
                new TamagotchiController(testModel);
        
        testController.getHealthDescription();
        testController.getWeightDescription();
        testController.getHappinessDescription();
        
        testController.decrementHealth(31);
        testController.getHealthDescription();
        
        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.getHappinessDescription();
        testController.getWeightDescription();
        
        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.getWeightDescription();

        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.getWeightDescription();
        
        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.feedPetSnacks();
        testController.feedPetSnacks();

        
        testController.load();
        
        testModel.isSick();
        testModel.isPause();
        testModel.decreaseWeight(1);
        testModel.decreaseHappiness(1);
    }
    
}