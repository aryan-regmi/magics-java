package aryan.regmi.com.github.magics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import aryan.regmi.com.github.magics.Magics.AppSystem;
import aryan.regmi.com.github.magics.Magics.Component;
import aryan.regmi.com.github.magics.Utils.Age;
import aryan.regmi.com.github.magics.Utils.Health;

class RunQueriesTest {
  private static record Npc() implements Component {
  }

  private static class SetupSystem implements AppSystem {
    @Override
    public void run(MContext ctx) {
      ctx.spawnEntity(new Npc(), new Health(67)); // NPC

      // Players
      ctx.spawnEntity(new Health(80), new Age(40));
      ctx.spawnEntity(new Health(95), new Age(29));
    }
  }

  private static class RunQuerySystem implements AppSystem {
    @Override
    public void run(MContext ctx) {
      System.out.println("RUnning runquerysystem");

      var playersQuery = ctx.query(Health.class, Age.class);
      var npcQuery = ctx.query(Npc.class, Health.class).single();

      // Check and update players
      var players = playersQuery.iterator();
      var player1 = players.next();
      assertEquals(player1.getComponent(Health.class).get().val(), 80);
      assertEquals(player1.getComponent(Age.class).get().val(), 40);
      ctx.updateEntityComponent(player1.getId(), new Health(100));

      var player2 = players.next();
      assertEquals(player2.getComponent(Health.class).get().val(), 95);
      assertEquals(player2.getComponent(Age.class).get().val(), 29);
      ctx.updateEntityComponent(player2.getId(), new Health(50));
      ctx.updateEntityComponent(player2.getId(), new Age(35));

      // Check npc
      assertEquals(npcQuery.getComponent(Health.class).get().val(), 67);
    }
  }

  private static class CheckUpdatedQueriesSystem implements AppSystem {
    @Override
    public void run(MContext ctx) {
      System.out.println("Running CheckUpdatedQueriesSystem system...");

      var playersQuery = ctx.query(Health.class, Age.class);
      var players = playersQuery.iterator();

      var player1 = players.next();
      assertEquals(player1.getComponent(Health.class).get().val(), 100);

      var player2 = players.next();
      assertEquals(player2.getComponent(Health.class).get().val(), 50);
      assertEquals(player2.getComponent(Age.class).get().val(), 35);
    }
  }

  @Test
  void canQueryWorld() {
    new App()
        .addStage(0, new SetupSystem())
        .addStage(1, new RunQuerySystem())
        .addSystem(new CheckUpdatedQueriesSystem())
        .run();
  }
}
