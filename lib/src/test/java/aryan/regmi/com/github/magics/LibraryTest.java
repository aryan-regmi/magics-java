package aryan.regmi.com.github.magics;

import org.junit.jupiter.api.Test;

import aryan.regmi.com.github.magics.Magics.Component;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {
  static class SpawnEntityTest {
    private record Health(int val) implements Component {
    }

    private record Age(int val) implements Component {
    }

    @Test
    void canSpawnEntity() {
      var world = new World();

      world.spawnEntity(new Health(22), new Age(45));
      var entity1 = world.entities.get(0);
      for (var component : entity1.components) {
        if (component.getClass() == Health.class) {
          assertEquals(((Health) component).val(), 22, "Health of Entity 1 is 22");
        }

        if (component.getClass() == Age.class) {
          assertEquals(((Age) component).val(), 45, "Age of Entity 1 is 45");
        }
      }

      world.spawnEntity(new Health(100));
      var entity2 = world.entities.get(1);
      var entity2Health = (Health) entity2.components.toArray()[0];
      assertEquals(entity2Health.val(), 100, "Health of Entity 2 is 100");

      assertEquals(world.numEntities, 2, "There are 2 entities in the world");
      assertEquals(world.componentLists.size(), 2);

    }
  }
}
