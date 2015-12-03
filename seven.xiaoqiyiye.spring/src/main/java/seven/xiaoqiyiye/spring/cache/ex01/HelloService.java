package seven.xiaoqiyiye.spring.cache.ex01;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

	@CachePut(value="hello", key="#hello.name")
	public Hello put(Hello hello){
		System.out.println("put Hello:" + hello.getName());
		return hello;
	}
	
	@Cacheable(value="hello", key="#name")
	public Hello get(String name){
		System.out.println("new Hello:" + name);
		return new Hello(name);
	}
}
