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
    }
  }

  @Test
  void canRunEcs() {
    var setupSystem = new SetupSystem();
    var app = new Magics.App().addSystem(setupSystem);

    assertEquals(app.systems.contains(setupSystem), true);
  }
}
