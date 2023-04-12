package aryan.regmi.com.github.magics;

import java.util.List;

import aryan.regmi.com.github.magics.Magics.Component;

public class MContext {
  private World world;

  MContext(World world) {
    this.world = world;
  }

  public int spawnEntity(Component... components) {
    return world.spawnEntity(components);
  }

  public Query query(Class<?>... componentTypes) {
    return new Query(this, componentTypes);
  }

  List<Entity> getEntities() {
    return world.entities;
  }

}
