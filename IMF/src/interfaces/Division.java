package interfaces;

import exceptions.InvalidTypeException;
import exceptions.NullException;
import orderedUnorderedList.ArrayUnorderedList;

public interface Division {

    String getName();

    void setName(String name);

    boolean isEntryExit();

    void setEntryExit();

    Enemy[] getEnemies();

    Double getEnemiesPower();

    void setEnemies(Enemy[] enemies);

    Integer sizeEnemies();

    void addEnemy(Enemy newEnemy) throws NullException, InvalidTypeException;

    ArrayUnorderedList<String> getEdges();

    void setItem(Item item);

    Item getItem();

    boolean isValid();

}
