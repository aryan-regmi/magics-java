package aryan.regmi.com.github.magics;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import aryan.regmi.com.github.magics.Magics.Component;

class World {
  int numEntities;
  List<Entity> entities;
  Map<Type, List<Optional<Component>>> componentLists;

  World() {
    numEntities = 0;
    entities = new ArrayList<Entity>();
    componentLists = new HashMap<Type, List<Optional<Component>>>();
  }

  int spawnEntity(Component... components) {
    var entityIdx = numEntities;
    numEntities++;

    var entity = new Entity();
    for (var component : components) {
      // Add to list if it exists
      if (componentLists.containsKey(component.getClass())) {
        var componentList = componentLists.get(component.getClass());
        componentList.add(Optional.of(component));
      }

      // Create a new list and add it to the world if one doesn't exist
      var newComponentList = new ArrayList<Optional<Component>>();
      for (int i = 0; i < numEntities; i++) {
        newComponentList.add(Optional.empty());
      }
      newComponentList.add(Optional.of(component));
      componentLists.put(component.getClass(), newComponentList);

      entity.addComponent(component);
    }
    entities.add(entity);

    return entityIdx;
  }
}
