package seven.ebatong;

public interface YbaHandleCallback<T> {

	void call(T request, Object attachment);
	
}
