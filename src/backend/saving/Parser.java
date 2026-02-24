package backend.saving;

import java.io.File;

public interface Parser<T> {
	
	public T parse(File in);

}
