package aryan.regmi.com.github.magics;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import aryan.regmi.com.github.magics.Magics.Component;

class Entity {
  Set<Component> components;
  Set<Type> componentTypes;

  Entity() {
    components = new HashSet<Component>();
    componentTypes = new HashSet<Type>();
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

  <T extends Component> Optional<T> getComponent(Type classType) {
    for (var component : components) {
      if (component.getClass() == classType) {
        return Optional.of((T) component);
      }
    }

    return Optional.empty();
  }
}
