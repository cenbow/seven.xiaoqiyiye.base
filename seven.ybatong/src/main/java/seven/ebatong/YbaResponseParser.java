package seven.ebatong;


public interface YbaResponseParser<T extends YbaResponse> {
	
    T parse(String responseString, boolean isSuccess);
    
}
