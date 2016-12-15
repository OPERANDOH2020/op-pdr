package eu.operando.pdr.dan.registry;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@Component
public class RepositoryManagersRegistry {
				
	private final Map<String, RepositoryManager> map = new HashMap<String, RepositoryManager>();
	
	@PostConstruct
	public void setup() {
		Constructor constructor = new Constructor(RepositoryManagers.class);
		
		TypeDescription RepositoryManagersDescription = new TypeDescription(RepositoryManagers.class);
		RepositoryManagersDescription.putListPropertyType("repositoryManagers", RepositoryManager.class);
		constructor.addTypeDescription(RepositoryManagersDescription);		
		
		Yaml yaml = new Yaml(constructor);		
		
		try{
			RepositoryManagers rms= (RepositoryManagers)yaml.load(new ClassPathResource("repositoryManagersRegistry.yml").getInputStream());
			for (RepositoryManager rm: rms.getRepositoryManagers()) {
				map.put(rm.getName(), rm);
			}			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public RepositoryManager get(String key) {	
			return map.get(key);
	}
}
