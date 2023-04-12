package aryan.regmi.com.github.magics;

import java.util.LinkedHashSet;
import java.util.Set;

public class Magics {
  public static interface Component {
  }

  public static interface AppSystem {
    void run(MContext ctx);
  }

  public static class App {
    private World world;
    Set<AppSystem> systems;

    public App() {
      this.world = new World();
      this.systems = new LinkedHashSet<AppSystem>();
    }

    /**
     * Adds a system to the app/ECS.
     * 
     * @param system The function/system to add.
     * @return This {@link App} instance.
     */
    App addSystem(AppSystem system) {
      systems.add(system);
      return this;
    }

    // TODO: Add way to run specific functions in certain orders
    /**
     * Runs the systems in the app.
     */
    public void run() {
      for (var appSystem : systems) {
        var ctx = new MContext(world);
        appSystem.run(ctx);
      }
    }
  }
}
