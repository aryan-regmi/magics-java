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

  synchronized void updateEntity(int entityId, Entity newEntity) {
    world.entities.set(entityId, newEntity);
  }

  synchronized boolean updateEntityComponent(int entityId, Component component) {
    var entity = world.entities.get(entityId);
    if (entity.componentTypes.contains(component.getClass())) {
      var componentCount = 0;
      for (var entityComponent : entity.components) {
        if (entityComponent.getClass() == component.getClass()) {
          entity.components.set(componentCount, component);
        }
        componentCount++;
      }
      return true;
    }

    return false;
  }

  synchronized boolean removeEntity(int entityId) {
    if (entityId < world.numEntities) {
      world.entities.remove(entityId);
      return true;
    }

    return false;
  }
}
