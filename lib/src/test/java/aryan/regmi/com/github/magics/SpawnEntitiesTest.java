package aryan.regmi.com.github.magics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static aryan.regmi.com.github.magics.Utils.Age;
import static aryan.regmi.com.github.magics.Utils.Health;

class SpawnEntitiesTest {
  @Test
  void canSpawnEntity() {
    var world = new World();

    world.spawnEntity(new Health(22), new Age(45));
    world.spawnEntity(new Health(100));
    assertEquals(world.numEntities, 2, "There are 2 entities in the world");
    assertEquals(world.componentLists.size(), 2);

    var entity1 = world.entities.get(0);
    var entity1Health = entity1.getComponent(Health.class);
    var entity1Age = entity1.getComponent(Age.class);
    assertEquals(entity1Health.get().val(), 22, "Health of Entity 1 is 22");
    assertEquals(entity1Age.get().val(), 45, "Age of Entity 1 is 45");

    var entity2 = world.entities.get(1);
    var entity2Health = entity2.getComponent(Health.class);
    assertEquals(entity2Health.get().val(), 100, "Health of Entity 2 is 100");
  }
}
