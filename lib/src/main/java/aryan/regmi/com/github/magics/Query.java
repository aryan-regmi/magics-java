package aryan.regmi.com.github.magics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Query implements Iterable<Entity> {
  private MContext ctx;
  private Class<?>[] componentTypes;
  private List<Entity> matchingEntities;

  Query(MContext ctx, Class<?>[] componentTypes) {
    this.ctx = ctx;
    this.componentTypes = componentTypes;
    this.matchingEntities = new ArrayList<Entity>();

    buildQuery();
  }

  void buildQuery() {
    // Loop through world, check for entities with these components, add them
    // to mathcingEntities list
    var entities = ctx.getEntities();
    for (var entity : entities) {
      if (entity.componentTypes.containsAll(Arrays.asList(componentTypes))) {
        matchingEntities.add(entity);
      }
    }
  }

  public Entity single() {
    return matchingEntities.get(0);
  }

  public static class QueryIterator implements Iterator<Entity> {
    private List<Entity> entities;
    private int currentEntity;
    private int numEntities;

    private QueryIterator(List<Entity> entities) {
      this.entities = entities;
      this.currentEntity = 0;
      this.numEntities = entities.size();
    }

    @Override
    public boolean hasNext() {
      if (currentEntity < numEntities) {
        return true;
      }

      return false;
    }

    @Override
    public Entity next() {
      var entity = entities.get(currentEntity);
      currentEntity++;
      return entity;
    }
  }

  @Override
  public QueryIterator iterator() {
    return new QueryIterator(matchingEntities);
  }
}
