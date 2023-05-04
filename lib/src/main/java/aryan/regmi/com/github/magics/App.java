
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

  // TODO: Maybe need to synchronize values
  /**
   * Runs the systems in the app.
   */
  public void run() {
    // Sort the stages by their sort order
    stages.sort(new Comparator<Stage>() {
      @Override
      public int compare(Stage s1, Stage s2) {
        return s1.stageOrder - s2.stageOrder;
      }
    });

    // Run all stages first, then run rest of the systems
    for (var stage : stages) {
      // Stages must run sequentially, but the systems inside the stages run in
      // separate threads.
      var stageThreadHandles = new HashSet<Thread>();
      for (var stageSystem : stage.systems()) {
        var systemRunnerThread = new Thread(new SystemRunner(stageSystem, world));
        systemRunnerThread.start();
        stageThreadHandles.add(systemRunnerThread);
      }

      for (var handle : stageThreadHandles) {
        try {
          handle.join();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    var systemThreadHandles = new HashSet<Thread>();
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
