package json;

import exceptions.InvalidFileException;
import exceptions.InvalidTypeException;
import exceptions.NullException;

import java.io.FileNotFoundException;

public class MapBuilder extends Import{

    public MapBuilder(String file) throws FileNotFoundException, InvalidFileException, NullException, InvalidTypeException {
        super(file);
    }


}
