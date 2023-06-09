package aryan.regmi.com.github.magics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import aryan.regmi.com.github.magics.Magics.Component;

class Entity {
  int id;
  List<Component> components;
  Set<Class<?>> componentTypes;

  Entity(int id) {
    this.id = id;
    components = new ArrayList<Component>();
    componentTypes = new HashSet<Class<?>>();
  }

  /**
   * Adds component to this entity.
   * 
   * @param component The component to add. NOTE: The component has to be unique
   *                  i.e. can't have the same type of components in an entity.
   */
  void addComponent(Component component) {
    // Add component only if it doesn't exist in the entity
    if (!componentTypes.contains(component.getClass())) {
      components.add(component);
      componentTypes.add(component.getClass());
      return;
    } else {
      throw new RuntimeException(
          "An entity can only have one component of a type: There are multiple instances of " + component.getClass()
              + " passed to this entity.");
    }
  }

  public <T extends Component> Optional<T> getComponent(Class<T> classType) {
    for (var component : components) {
      if (component.getClass() == classType) {
        T ret = classType.cast(component);
        return Optional.of(ret);
      }
    }

    return Optional.empty();
  }

  public int getId() {
    return id;
  }
}
