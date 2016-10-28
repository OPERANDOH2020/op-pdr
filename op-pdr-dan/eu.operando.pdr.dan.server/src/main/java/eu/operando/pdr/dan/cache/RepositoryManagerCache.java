package eu.operando.pdr.dan.cache;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@Component
public class RepositoryManagerCache {
				
	private final Map<String, RepositoryManager> cacheMap = new HashMap<String, RepositoryManager>();
	
	@PostConstruct
	public void setup() {
		Constructor constructor = new Constructor(RepositoryManagers.class);
		
		TypeDescription RepositoryManagersDescription = new TypeDescription(RepositoryManagers.class);
		RepositoryManagersDescription.putListPropertyType("repositoryManagers", RepositoryManager.class);
		constructor.addTypeDescription(RepositoryManagersDescription);		
		
		Yaml yaml = new Yaml(constructor);		
		
		try{
			RepositoryManagers rms= (RepositoryManagers)yaml.load(new ClassPathResource("repository-managers.yml").getInputStream());
			for (RepositoryManager rm: rms.getRepositoryManagers()) {
				cacheMap.put(rm.getName(), rm);
			}			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void put(String key, RepositoryManager value) {
		synchronized (cacheMap) {
			cacheMap.put(key, value);
		}
	}

	public RepositoryManager get(String key) {
		synchronized (cacheMap) {
			RepositoryManager value = (RepositoryManager) cacheMap.get(key);
			return value; 
		}
	}
}