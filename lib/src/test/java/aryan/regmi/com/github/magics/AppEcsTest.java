package aryan.regmi.com.github.magics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import aryan.regmi.com.github.magics.Magics.AppSystem;
import aryan.regmi.com.github.magics.Utils.Health;
import aryan.regmi.com.github.magics.Utils.Age;

class AppEcsTest {
  private static class SetupSystem implements AppSystem {
    @Override
    public void run(MContext ctx) {
      ctx.spawnEntity(new Health(100), new Age(85));
      System.out.println("I ran first");
    }
  }

  private static class StagedSystem implements AppSystem {
    @Override
    public void run(MContext ctx) {
      try {
        Thread.sleep(2);

      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println("I ran second");
    }
  }

  private static class ParSystem1 implements AppSystem {
    @Override
    public void run(MContext ctx) {
      try {
        Thread.sleep(4);
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println("I run by myself!");
    }
  }

  private static class ParSystem2 implements AppSystem {
    @Override
    public void run(MContext ctx) {
      try {
        Thread.sleep(2);
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println("I run by myself TOO!");
    }
  }

  @Test
  void canRunEcs() {
    var setupSystem = new SetupSystem();
    var app = new App()
        .addStage(0,
            setupSystem,
            new StagedSystem())
        .addSystem(new ParSystem1())
        .addSystem(new ParSystem2());

    assertEquals(app.stages.get(0).systems().contains(setupSystem), true);

    app.run();
  }
}
