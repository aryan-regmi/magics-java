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

  private static class AnotherSystem implements AppSystem {
    @Override
    public void run(MContext ctx) {
      System.out.println("I ran second");
      System.out.println(ctx.getEntities());
    }
  }

  private static class YetAnotherSystem implements AppSystem {
    @Override
    public void run(MContext ctx) {
      System.out.println("I run by myself!");
      System.out.println(ctx.getEntities());
    }
  }

  @Test
  void canRunEcs() {
    var setupSystem = new SetupSystem();
    var app = new App()
        .addStage(0,
            setupSystem,
            new AnotherSystem())
        .addSystem(new YetAnotherSystem());

    assertEquals(app.stages.get(0).systems().contains(setupSystem), true);

    app.run();
  }
}
