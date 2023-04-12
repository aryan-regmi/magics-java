
package aryan.regmi.com.github.magics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aryan.regmi.com.github.magics.Magics.AppSystem;

public class App {
  private World world;
  Set<AppSystem> systems;
  List<Stage> stages;

  record Stage(List<AppSystem> systems, int stageOrder) {
  }

  public App() {
    this.world = new World();
    this.systems = new HashSet<AppSystem>();
    this.stages = new ArrayList<Stage>();
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

  App addStage(int stageOrder, AppSystem... systems) {
    this.stages.add(new Stage(Arrays.asList(systems), stageOrder));
    return this;
  }

  private class SystemRunner implements Runnable {
    private AppSystem system;
    private World world;

    SystemRunner(AppSystem system, World world) {
      this.system = system;
      this.world = world;
    }

    @Override
    public void run() {
      system.run(new MContext(world));
    }
  }

  /**
   * Runs the systems in the app.
   */
  public void run() {
    // TODO: Run all systems in a stage in any order -> Only stages are run in their
    // orders, not the systems inside them
    //
    // Run all stages first, then run rest of the systems (eventually run rest in a
    // separate thread)
    stages.sort(new Comparator<Stage>() {
      @Override
      public int compare(Stage s1, Stage s2) {
        return s1.stageOrder - s2.stageOrder;
      }
    });
    for (var stage : stages) {
      for (var stageSystem : stage.systems()) {
        stageSystem.run(new MContext(world));
      }
    }

    var systemThreadHandles = new ArrayList<Thread>();
    for (var appSystem : systems) {
      var systemRunnerThread = new Thread(new SystemRunner(appSystem, world));
      systemRunnerThread.start();
      systemThreadHandles.add(systemRunnerThread);
    }

    for (var handle : systemThreadHandles) {
      try {
        handle.join();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }
}
